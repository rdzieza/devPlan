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
import database.DatabaseConnectionManager;
import database.DatabaseInserQueryExecutor;
import dev.rd.devplan.R;

public class NewGroupsDownloader extends BaseNetworkConnector {
	private HttpGet get;

	public NewGroupsDownloader(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Log.v("t", "NewGroups - doInBackground()");
		if (checkConnection()) {
			String url = context.getResources().getString(
					R.string.groups_download_url);
			get = new HttpGet(url);

			try {
				HttpResponse response = client.execute(get);
				String responseContent = readResponse(response);
				Log.v("t", "content has been read");
				DatabaseInserQueryExecutor.addAllGroups(DatabaseConnectionManager.getConnection().getWritableDatabase(), responseContent);

			} catch (ClientProtocolException e) {
				cancelWithMessage("Problems with communicating the server");
				e.printStackTrace();
			} catch (IOException e) {
				cancelWithMessage("Problems with communicating the server");
				e.printStackTrace();
			}catch (JSONException e) {
				cancelWithMessage("Problems with server response content");
				e.printStackTrace();
			}catch (SQLException e) {
				cancelWithMessage("Problem with database");
				e.printStackTrace();
			}

		}else {
			cancelWithMessage("No internet connection");
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		Log.v("t", "onPostExecute()");
		PreferenceHelper.saveBoolean("areGroupsDownloaded", true);
		Toast.makeText(context, "Groups has been downloaded", Toast.LENGTH_LONG).show();
	}
}
