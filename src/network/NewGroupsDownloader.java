package network;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;

import prefereces.PreferenceHelper;
import adapters.GroupsListAdapter;
import adapters.SelectedGroupsAdapter;
import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import classes.DownloadManager;
import database.DatabaseConnectionManager;
import database.DatabaseDataProvider;
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
			DownloadManager.setDowloadingGroups(true);
			String url = context.getResources().getString(
					R.string.groups_download_url);
			get = new HttpGet(url);

			try {
				HttpResponse response = client.execute(get);
				String responseContent = readResponse(response);
				Log.v("t", "content has been read");
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
		updateUI();
		
	}

	public void handleException(Exception e) {
		if (e instanceof ClientProtocolException) {
			message += "Problems with communicating the server";
		} else if (e instanceof IOException) {
			message += "Problems with communicating the server";
		} else if (e instanceof JSONException) {
			message += "Problems with server response content";
		} else if (e instanceof SQLException) {
			message += "Problem with database";
		}
		cancelWithMessage(message);
		DownloadManager.setDowloadingGroups(false);
	}
	
	public void updateUI() {
		Activity activity = (Activity) context;
		updateUnselectedListViewContent(activity);
		updateSelectedListViewContent(activity);
		hideProgressBar(activity);
	}
	
	private void updateUnselectedListViewContent(Activity activity) {
		ListView allGroupsList = (ListView) activity
				.findViewById(R.id.groupsListView);
		if (allGroupsList != null) {
			allGroupsList
					.setAdapter(new GroupsListAdapter(
							context,
							DatabaseDataProvider
									.getUnselectedGroupsCursor(DatabaseConnectionManager
											.getConnection()
											.getReadableDatabase())));

		}
	}

	private void updateSelectedListViewContent(Activity activity) {
		ListView selected = (ListView) activity
				.findViewById(R.id.selectedGroupsList);
		if (selected != null) {
			selected.setAdapter(new SelectedGroupsAdapter(activity,
					DatabaseDataProvider
							.getSelectedGroupsCursor(DatabaseConnectionManager
									.getConnection().getReadableDatabase())));
		}
	}

	private void hideProgressBar(Activity activity) {
		ProgressBar groupsBar = (ProgressBar) activity
				.findViewById(R.id.loadingGroupsBar);
		if (groupsBar != null) {
			groupsBar.setVisibility(View.GONE);
		}
		TextView downloadingLabel = (TextView) activity
				.findViewById(R.id.loadingGroupText);
		if (downloadingLabel != null) {
			downloadingLabel.setVisibility(View.GONE);
		}
	}
}
