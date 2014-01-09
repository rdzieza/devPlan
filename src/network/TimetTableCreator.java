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

import prefereces.PreferenceHelper;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import database.DatabaseManager;

public class TimetTableCreator extends AsyncTask<Void, Void, Void> {
	private HttpClient client;
	private int numberOfselected;
	private Cursor selectedCursor;
	private Context context;

	public TimetTableCreator(Context context) {
		this.context = context;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		client = new DefaultHttpClient();
		selectedCursor = DatabaseManager.getSelected(DatabaseManager
				.getConnection().getReadableDatabase());
		numberOfselected = selectedCursor.getCount();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
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
		if (numberOfselected == 0) {
			Log.v("t", "ZEROOOOOOOO");
			return null;
		} else {
			HttpPost post = new HttpPost(
					"http://cash.dev.uek.krakow.pl//v0_1/timetables");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			while (selectedCursor.moveToNext()) {
				params.add(new BasicNameValuePair("group_id[]", selectedCursor
						.getString(selectedCursor.getColumnIndex("ID"))));
			}
			for (NameValuePair pair : params) {
				Log.v("t", pair.getName() + " : " + pair.getValue());
			}
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = client.execute(post);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				line = sb.toString();
				Log.v("T", line);
				JSONObject json = new JSONObject(line);
				// JSONArray array = new JSONArray(line);
				// JSONObject json = array.getJSONObject(0);
				String hash = json.getString("_id");
				Log.v("t", hash);
				// String hash = "52911dceecd7da68fb000002";
				PreferenceHelper.saveString("timeTableUrl", hash);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				this.cancel(true);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				this.cancel(true);
			} catch (IOException e) {
				e.printStackTrace();
				this.cancel(true);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.cancel(true);
			}
			return null;
		}

	}

	@Override
	public void onPostExecute(Void v) {
		if (!isCancelled()) {
			TimeTableDownloader tDown = new TimeTableDownloader(context);
			tDown.execute();
			Toast.makeText(context, "groups download finished",
					Toast.LENGTH_SHORT);
		} else {
			Toast.makeText(context, "Sth went wrong", Toast.LENGTH_SHORT)
					.show();
		}
	}

}
