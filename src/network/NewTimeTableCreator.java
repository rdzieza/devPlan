package network;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import prefereces.PreferenceHelper;

import classes.DownloadManager;

import android.content.Context;
import android.database.SQLException;
import android.net.ParseException;
import database.DatabaseConnectionManager;
import database.DatabaseDataProvider;

public class NewTimeTableCreator extends BaseNetworkConnector {
	private Context context;

	public NewTimeTableCreator(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (checkPreConditions()) {
			List<NameValuePair> postParams = DatabaseDataProvider
					.getSelectedAsNameValuePairsList(DatabaseConnectionManager
							.getConnection().getReadableDatabase());
			DownloadManager.setDownloadingTimeTable(true);
			HttpPost post = new HttpPost(
					"http://cash.dev.uek.krakow.pl/v0_1/timetables");
			try {
				post.setEntity(new UrlEncodedFormEntity(postParams));
				HttpResponse response = client.execute(post);
				String responseContent = readResponse(response);
				String hash = getHash(responseContent);
				PreferenceHelper.saveString("timeTableUrl", hash);
			} catch (Exception e) {
				handleException(e);
			}
		} else {
			cancelWithMessage("There is no internet connection or no groups has been selected");
		}

		return null;
	}

	public boolean checkPreConditions() {
		return checkConnection() && checkNumberOfSelectedGroups();
	}

	private boolean checkNumberOfSelectedGroups() {
		int number = DatabaseDataProvider
				.getNumberOfSelectedGroups(DatabaseConnectionManager
						.getConnection().getReadableDatabase());
		if (number != 0) {
			return true;
		} else {
			return false;
		}
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
		}
		cancelWithMessage(message);
		DownloadManager.setDownloadingTimeTable(false);
	}
	
	private String getHash(String responseContent) throws JSONException {
		JSONObject json = new JSONObject(responseContent);
		return json.getString("_id");
	}
}
