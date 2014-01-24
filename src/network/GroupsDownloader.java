package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import knp.rd.timetable.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import prefereces.PreferenceHelper;
import adapters.GroupsListAdapter;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import database.DatabaseManager;

public class GroupsDownloader extends AsyncTask<Void, Void, Void> {
	private HttpClient client;
	private BufferedReader br;
	private StringBuilder sb;
	private Context context;
	SQLiteDatabase db;
	boolean transactionExists;

	public GroupsDownloader(Context context) {
		this.context = context;
		db = DatabaseManager.getConnection().getWritableDatabase();
		transactionExists = false;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		client = new DefaultHttpClient();
		sb = new StringBuilder();
		// DatabaseManager.removeGroups();
		Toast.makeText(context,
				"Pobieranie: lista grup\n po zakonczeniu dodaj swoje grupy!",
				Toast.LENGTH_LONG).show();
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
			uri = new URI("http", null, "cash.dev.uek.krakow.pl", 3000,
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
					// for (int i = 0; i < groups.length(); i++) {
					// JSONObject group = groups.getJSONObject(i);
					// DatabaseManager.insertGroup(group.getInt("id"),
					// group.getString("name"));
					// }
					// PreferenceHelper.saveBoolean("isFirst", true);

					db.beginTransaction();
					transactionExists = true;
					DatabaseManager.addAllGroups(groups, db);
					PreferenceHelper.saveBoolean("isNotFirst", true);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Long endTime = new Date().getTime();
				Log.v("t",
						"TIME DIFF: : : : "
								+ String.valueOf(endTime - startTime));
			} catch (IOException e) {
				e.printStackTrace();
				this.cancel(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
			this.cancel(true);
		} finally {

		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		if (!isCancelled()) {
			db.setTransactionSuccessful();
			Log.v("t", "finished");
			Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show();
			GroupsListAdapter adapter = new GroupsListAdapter(context,
					DatabaseManager.getGroupsCursor(DatabaseManager
							.getConnection().getReadableDatabase()));
			Activity activity = (Activity) context;
			ListView list = (ListView) activity
					.findViewById(R.id.groupsListView);
			list.setAdapter(adapter);
		} else {
			Toast.makeText(context, "Sth went wrong", Toast.LENGTH_SHORT)
					.show();
		}
		if (transactionExists) {
			db.endTransaction();
		}

	}

}
