package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import prefereces.PreferenceHelper;
import adapters.GroupsListAdapter;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import classes.DownloadManager;
import database.DatabaseManager;
import dev.rd.devplan.R;
import fragments.AddGroupFragment;

/**
 * 
 * @author Robert Dzieża
 * 
 *         AsyncTask responsible for downloading list of all available groups.
 * 
 */
public class GroupsDownloader extends AsyncTask<Void, Void, Void> {
	private HttpClient client;
	private BufferedReader br;
	private StringBuilder sb;
	private Context context;
	SQLiteDatabase db;
	boolean transactionExists;
	private String message = "";
	private boolean isConnected;

	public GroupsDownloader(Context context) {
		this.context = context;
		db = DatabaseManager.getConnection().getWritableDatabase();
		transactionExists = false;
		classes.DownloadManager.setDowloadingGroups(true);
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		client = new DefaultHttpClient();
		sb = new StringBuilder();
		Toast.makeText(context,
				"Pobieranie: lista grup\npo zakończeniu dodaj swoje grupy!",
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		isConnected = checkConnection();

		if (!isConnected) {
			this.cancel(true);
			message += "Brak połączenia z internetem";
		}

		/**
		 * Creates server connection
		 */
		HttpGet get = new HttpGet("http://cash.dev.uek.krakow.pl/v0_1/groups");
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
				// Log.v("t", sb.toString());
				/**
				 * Parse JSON
				 */
				Long startTime = new Date().getTime();
				try {
					org.json.JSONArray groups = new org.json.JSONArray(
							sb.toString());

					ContentValues results = DatabaseManager
							.addAllGroups(groups);
					int code = results.getAsInteger("code");
					if (code == 1) {
						message = results.getAsString("message");
						this.cancel(true);
					} else {
						// Log.v("t", "groups downlaoded - saved in settings");
						PreferenceHelper.saveBoolean("areGroupsDownloaded",
								true);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					message = message + " " + "server problem";
				}
				Long endTime = new Date().getTime();
				Log.v("t",
						"TIME DIFF: : : : "
								+ String.valueOf(endTime - startTime));
			} catch (IOException e) {
				e.printStackTrace();
				this.cancel(true);
				message = message + " " + "connection problem";
			}
		} catch (IOException e) {
			e.printStackTrace();
			this.cancel(true);
			message = message + " " + "connection problem";
		} finally {
			db.close();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		Activity activity = (Activity) context;
		if (!isCancelled()) {
			// Log.v("t", "Grupy zostały pobrane");
			Toast.makeText(context, "Grupy zostały pobrane", Toast.LENGTH_SHORT)
					.show();

			ListView allGroupsList = (ListView) activity
					.findViewById(R.id.groupsListView);
			if (allGroupsList != null) {
				allGroupsList.setAdapter(new GroupsListAdapter(context,
						DatabaseManager.getGroupsCursor(DatabaseManager
								.getConnection().getReadableDatabase())));

			}

		} else {
			Toast.makeText(
					context,
					"Wystąpił problem: "
							+ message
							+ "\nW zakładce opcje możesz \nręcznie pobrać listę grup",
					Toast.LENGTH_SHORT).show();
		}

		classes.DownloadManager.setDowloadingGroups(false);
		ProgressBar groupsBar = (ProgressBar) activity
				.findViewById(R.id.loadingGroupsBar);
		TextView downloadingLabel = (TextView) activity
				.findViewById(R.id.loadingGroupText);
		if (groupsBar != null && downloadingLabel != null) {
			groupsBar.setVisibility(View.GONE);
			downloadingLabel.setVisibility(View.GONE);
		}
		
		AddGroupFragment adf = DownloadManager.getAddGroupFragment();
		if(adf != null){
			Log.v("t", "adf not null");
			adf.setAdapter();
			adf.fixFilter();
		}else{
			Log.v("t", "adf null");
		}

	}

	public boolean checkConnection() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		// Log.v("t", "Connected: " + String.valueOf(isConnected));
		return isConnected;
	}

}
