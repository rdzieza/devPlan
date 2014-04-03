package network;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;

import prefereces.PreferenceHelper;
import android.content.Context;
import android.database.SQLException;
import android.net.ParseException;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import classes.DownloadManager;
import classes.JSONProvider;
import database.DatabaseConnectionManager;
import database.DatabaseDataProvider;

public class TimeTableCreator extends BaseNetworkConnector {
	private HttpPost post;
	public TimeTableCreator(Context context) {
		super(context);

	}

	@Override
	protected Void doInBackground(Void... params) {
		if (checkPreConditions()) {
			Log.v("t", "PreConditions have been met");
			List<NameValuePair> postParams = DatabaseDataProvider
					.getSelectedAsNameValuePairsList(DatabaseConnectionManager
							.getConnection().getReadableDatabase());
			DownloadManager.setDownloadingTimeTable(true);
			post = new HttpPost(
					"http://cash.dev.uek.krakow.pl/v0_1/timetables");
			try {
				post.setEntity(new UrlEncodedFormEntity(postParams));
				HttpResponse response = client.execute(post);
				String responseContent = readResponse(response);
				String hash = JSONProvider.getHashFromString(responseContent);
				PreferenceHelper.saveString("timeTableUrl", hash);
				Log.v("t", hash);
			} catch (Exception e) {
				handleException(e);
			}
		} else {
			cancelWithMessage("There is no internet connection or no groups has been selected");
		}

		return null;
	}
	
	
	public boolean checkPreConditions() {
		Log.v("t", "checkPreConditions()");
		return checkConnection() && checkNumberOfSelectedGroups();
	}

	private boolean checkNumberOfSelectedGroups() {
		Log.v("t", "checkNumberOfSelected()");
		int number = DatabaseDataProvider
				.getNumberOfSelectedGroups(DatabaseConnectionManager
						.getConnection().getReadableDatabase());
		Log.v("t", String.valueOf(number));
		if (number != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onPostExecute(Void result) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				NewTimeTableDownloader tdown = new NewTimeTableDownloader(context);
				tdown.execute();
				
			}
			
		});
	}
	
	@Override
	public void handleException(Exception e) {
		if (e instanceof ClientProtocolException) {
			message += "Problems with communicating the server";
		}else if(e instanceof ParseException) {
			message += "Problem with parsing request parameters";
		}else if (e instanceof IOException) {
			message += "Problems with communicating the server";
		} else if (e instanceof JSONException) {
			message += "Problems with server response content";
		} else if (e instanceof SQLException) {
			message += "Problem with database";
		}else {
			message += "Something is wrong";
			Log.v("t", "Exception class: " + e.getClass().toString());
		}
		cancelWithMessage(message);
		DownloadManager.setDownloadingTimeTable(false);
	}
	
	
}
