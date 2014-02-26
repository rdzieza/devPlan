package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import classes.DownloadManager;

import prefereces.PreferenceHelper;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import database.DatabaseManager;
import fragments.AddGroupFragment;

/**
 * 
 * @author Robert Dzieża
 * 
 *         AsyncTask creates time table on the server and saves its hash under
 *         timeTableUrl in Preferences.
 * 
 */
public class TimetTableCreator extends AsyncTask<Void, Void, Void> {
	private HttpClient client;
	private int numberOfselected;
	private Cursor selectedCursor;
	private Context context;
	private AddGroupFragment addGroupFragment;
	private int code = 1;
	private String message = "";
	private boolean isConnected;

	public TimetTableCreator(Context context) {
		this.context = context;
	}

	public TimetTableCreator(Context context, AddGroupFragment addGroupFragment) {
		this.context = context;
		this.addGroupFragment = addGroupFragment;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		client = new DefaultHttpClient();

	}

	@Override
	protected Void doInBackground(Void... arg0) {
		isConnected = checkConnection();

		if (!isConnected) {
			message += "Brak połączenia z internetem";
			this.cancel(true);
		} else {
			selectedCursor = DatabaseManager.getSelected(DatabaseManager
					.getConnection().getReadableDatabase());
			numberOfselected = selectedCursor.getCount();

			if (numberOfselected == 0) {
				// Log.v("t", "ZEROOOOOOOO");
				message += "Brak wybranych grup!";
				this.cancel(true);
				return null;
			} else {
				DownloadManager.setDownloadingTimeTable(true);
				HttpPost post = new HttpPost(
						"http://cash.dev.uek.krakow.pl/v0_1/timetables");

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				while (selectedCursor.moveToNext()) {
					params.add(new BasicNameValuePair("group_id[]",
							selectedCursor.getString(selectedCursor
									.getColumnIndex("ID"))));
				}
				for (NameValuePair pair : params) {
					Log.v("t", pair.getName() + " : " + pair.getValue());
				}

				try {
					post.setEntity(new UrlEncodedFormEntity(params));
					// Log.v("t", post.toString());
					HttpResponse response = client.execute(post);
					BufferedReader rd = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
					StringBuilder sb = new StringBuilder();
					String line = "";
					while ((line = rd.readLine()) != null) {
						sb.append(line);
					}
					line = sb.toString();
					Log.v("T", line);
					JSONObject json = new JSONObject(line);
					String hash = json.getString("_id");
					Log.v("t", hash);
					PreferenceHelper.saveString("timeTableUrl", hash);
					code = 0;
				} catch (UnsupportedEncodingException e) {
					message += "Błąd kodowania, problem z serwerem";
					e.printStackTrace();
					this.cancel(true);

					return null;
				} catch (ClientProtocolException e) {
					message += "Błąd protokołu, problem z serwerem";
					e.printStackTrace();
					this.cancel(true);

					return null;
				} catch (IOException e) {
					message += "Błąd operacji wejścia-wyjścia";
					e.printStackTrace();
					this.cancel(true);

					return null;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					message += "Problem z serwerem";
					e.printStackTrace();
					this.cancel(true);

					return null;
				}
			}
		}
		return null;

	}

	@Override
	public void onPostExecute(Void v) {
		Log.v("t", "onPostExecute() begins, " + "message: " + message);

		if (!isCancelled()) {
			Log.v("t", "TimeTable successfully created!");
			addGroupFragment.downloadTimeTable(code);
		} else {
			Toast.makeText(context, "Wystąpił problem: " + message,
					Toast.LENGTH_LONG).show();
			DownloadManager.setDownloadingTimeTable(false);
		}
	}

	public boolean checkConnection() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		Log.v("t", "Connected: " + String.valueOf(isConnected));
		return isConnected;
	}

}
