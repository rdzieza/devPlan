package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import prefereces.PreferenceHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import database.DatabaseManager;

public class GroupsDownloader extends AsyncTask<Void, Void, Void> {
	private HttpClient client;
	private BufferedReader br;
	private StringBuilder sb;
	private Context context;

	public GroupsDownloader(Context context) {
		this.context = context;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		client = new DefaultHttpClient();
		sb = new StringBuilder();
		DatabaseManager.removeGroups();

	}

	@Override
	protected Void doInBackground(Void... params) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		Log.v("t", "Connected: " + String.valueOf(isConnected));
		if (!isConnected) {
			this.cancel(true);
			Toast.makeText(context,
					"There is no internett connection,  turn on the internet!",
					Toast.LENGTH_SHORT).show();
			return null;
		}
		
		URI uri = null;
		/**
		 * Create URI
		 */
		try {
			uri = new URI("http", null, "knp.uek.krakow.pl", 3000,
					"/v0_1/groups", null, null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		HttpGet get = new HttpGet(uri);
		try {
			/**
			 * Execute GET request
			 */
			HttpResponse responce = client.execute(get);
			br = new BufferedReader(new InputStreamReader(responce.getEntity()
					.getContent()));
			String line = "";
			/**
			 * Read data
			 */
			try {
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				/**
				 * Parse JSON
				 */
				Long startTime = new Date().getTime();
				try {
					org.json.JSONArray groups = new org.json.JSONArray(
							sb.toString());
//					for (int i = 0; i < groups.length(); i++) {
//						JSONObject group = groups.getJSONObject(i);
//						DatabaseManager.insertGroup(group.getInt("id"),
//								group.getString("name"));
//					}
//					PreferenceHelper.saveBoolean("isFirst", true);
					DatabaseManager.addAllGroups(groups);
					PreferenceHelper.saveBoolean("isNotFirst", true);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Long endTime = new Date().getTime();
				Log.v("t", "TIME DIFF: : : : " + String.valueOf(endTime - startTime));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		if (!isCancelled()) {
			Log.v("t", "finished");
			Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "Sth went wrong", Toast.LENGTH_SHORT)
					.show();
		}

	}

}
