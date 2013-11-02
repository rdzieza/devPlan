package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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

	}

	@Override
	protected Void doInBackground(Void... params) {

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
				try {
					org.json.JSONArray groups = new org.json.JSONArray(
							sb.toString());
					for (int i = 0; i < groups.length(); i++) {
						JSONObject group = groups.getJSONObject(i);
						DatabaseManager.insertGroup(group.getInt("id"),
								group.getString("name"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
		Log.v("t", "finished");
		Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show();

	}

}
