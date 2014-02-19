package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import prefereces.PreferenceHelper;
import adapters.ActivityAdapter;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import database.DatabaseManager;
import dev.rd.devplan.R;

public class TimeTableDownloader extends AsyncTask<Void, Void, Void> {
	private String url;
	private HttpClient client;
	private StringBuilder sb;
	private BufferedReader br;
	private Context context;
	private String message;
	boolean isConnected;

	public TimeTableDownloader(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		url = "http://cash.dev.uek.krakow.pl:3000/v0_1/timetables/"
				+ PreferenceHelper.getString("timeTableUrl");
		client = new DefaultHttpClient();
	}

	@Override
	protected Void doInBackground(Void... params) {
		Log.v("t", "Downloader - do in background - start");

		isConnected = checkConnection();
		if (!isConnected) {
			this.cancel(true);
			Toast.makeText(context,
					"There is no internett connection,  turn on the internet!",
					Toast.LENGTH_SHORT).show();
			message = "brak połączenia z internetem";
		}

		Log.v("t", url);
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			br = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			sb = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			JSONObject object = new JSONObject(sb.toString());
			JSONArray activities = object.getJSONArray("activities");

			ContentValues results = DatabaseManager
					.addAllAvtivities(activities);

			int code = results.getAsInteger("code");
			if (code == 1) {
				message = results.getAsString("message");
				this.cancel(true);
			}

		} catch (IOException e) {
			e.printStackTrace();
			this.cancel(true);
			message = "Problem z siecią lub serwerem";
		} catch (JSONException e) {
			e.printStackTrace();
			this.cancel(true);
			message = "Problem z  serwerem";
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		if (!isCancelled()) {
			Log.v("t", context.getString(R.string.download_finished_message));
			Toast.makeText(context,
					context.getString(R.string.download_finished_message),
					Toast.LENGTH_SHORT).show();

			Activity activity = (Activity) context;
			ListView timeTableList = (ListView) activity
					.findViewById(R.id.timeTableListView);
			if (timeTableList != null) {
				timeTableList.setAdapter(new ActivityAdapter(context,
						DatabaseManager.getEventsList(DatabaseManager
								.getConnection().getReadableDatabase())));
			}

		} else {
			Toast.makeText(context, "Sth went wrong: " + message,
					Toast.LENGTH_SHORT).show();
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
