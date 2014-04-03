package network;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;

import prefereces.PreferenceHelper;
import android.content.Context;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;
import classes.DownloadManager;
import classes.GroupsContentUiUpdator;
import database.DatabaseConnectionManager;
import database.DatabaseInserQueryExecutor;
import dev.rd.devplan.R;

public class GroupsDownloader extends BaseNetworkConnector {
	private HttpGet get;

	public GroupsDownloader(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Log.v("t", "NewGroups - doInBackground()");
		if (checkConnection()) {
			DownloadManager.setDowloadingGroups(true);
			String url = context.getResources().getString(
					R.string.groups_download_url);
			get = new HttpGet(url);

			try {
				HttpResponse response = client.execute(get);
				String responseContent = readResponse(response);
				DatabaseInserQueryExecutor.addAllGroups(
						DatabaseConnectionManager.getConnection()
								.getWritableDatabase(), responseContent);

			} catch (Exception e) {
				handleException(e);
			}

		} else {
			cancelWithMessage("No internet connection");
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.v("t", "onPostExecute()");
		PreferenceHelper.saveBoolean("areGroupsDownloaded", true);
		DownloadManager.setDowloadingGroups(false);
		Toast.makeText(context, "Groups has been downloaded", Toast.LENGTH_LONG)
				.show();
		GroupsContentUiUpdator updator = new GroupsContentUiUpdator(context);
		updator.updateUI();
		
	}

	@Override
	public void handleException(Exception e) {
		if (e instanceof ClientProtocolException) {
			message += "Problems with communicating the server";
		} else if (e instanceof IOException) {
			message += "Problems with communicating the server";
		} else if (e instanceof JSONException) {
			message += "Problems with server response content";
		} else if (e instanceof SQLException) {
			message += "Problem with database";
		}else {
			message += "Something is wrong";
		}
		cancelWithMessage(message);
		DownloadManager.setDowloadingGroups(false);
	}

}
