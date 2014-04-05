package network;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONArray;

import prefereces.PreferenceHelper;
import activities.MainView;
import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;
import classes.ActivitiesStack;
import classes.DownloadManager;
import classes.EmptyListException;
import classes.JSONProvider;
import classes.TimeTableUiUpdator;
import database.DatabaseConnectionManager;
import database.DatabaseQueryExecutor;
import database.DatabaseQueryFactory;
import dev.rd.devplan.R;

public class TimeTableDownloader extends BaseNetworkConnector {
	private HttpGet get;

	public TimeTableDownloader(Context context) {
		super(context);
	}

	@Override
	protected Void doInBackground(Void... params) {
		Log.v("t", "TimeTableDownloader - doInBackground()");
		String url = context.getResources().getString(
				R.string.timetable_download_url)
				+ PreferenceHelper.getString("timeTableUrl");
		get = new HttpGet(url);
		if (checkConnection()) {
			try {
				HttpResponse response = client.execute(get);
				String responseContent = readResponse(response);
				JSONArray groups = JSONProvider
						.getActivitiesArray(responseContent);
				List<String> queries = DatabaseQueryFactory
						.getInsertActivitiesQueriesList(groups);
				if (DatabaseQueryExecutor.runAllInsertActivitiesQueries(
						DatabaseConnectionManager.getConnection()
								.getWritableDatabase(), queries)) {
					String versionHash = JSONProvider.getVersionHash(responseContent);
					PreferenceHelper.saveString("versions", versionHash);
					return null;
				} else {
					cancelWithMessage("Database problem");
				}
			} catch (Exception e) {
				handleException(e);
			}
		} else {
			cancelWithMessage("No internet connection");
		}
		return null;
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
		} else {
			message += "Something is wrong";
			e.printStackTrace();
		}
		cancelWithMessage(message);
		DownloadManager.setDowloadingGroups(false);
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.v("t", "TimeTableDownloader - onPostExecute()");
		Toast.makeText(context,
				context.getString(R.string.download_finished_message),
				Toast.LENGTH_LONG).show();
		DownloadManager.setDownloadingTimeTable(false);

		TimeTableUiUpdator updator = new TimeTableUiUpdator(context);
		updator.updateUI();
		switchToTimeTableTab();
		
	}
	
	private void switchToTimeTableTab() {
		Log.v("t", "switchToTimeTableTab()");
		try {
			Activity activity = ActivitiesStack.getFromTop();
			if(activity instanceof MainView) {
				MainView mainView = (MainView)activity;
				mainView.getActionBar().setSelectedNavigationItem(1);
			}
		}catch(EmptyListException e) {
			Log.v("t", "EmptyListException");
			e.printStackTrace();
		}
	}

}
