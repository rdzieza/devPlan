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
import android.os.AsyncTask;
import android.util.Log;
import database.DatabaseManager;

public class TimeTableDownloader extends AsyncTask<Void, Void, Void> {
	private String hash;
	private HttpClient client;
	private StringBuilder sb;
	private BufferedReader br;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		hash = PreferenceHelper.getString("timeTableHash");
		client = new DefaultHttpClient();
	}

	@Override
	protected Void doInBackground(Void... params) {
		String url = "http://knp.uek.krakow.pl:3000/v0_1/timetables/" + hash;
		Log.v("t", url);
		HttpGet get = new HttpGet("http://knp.uek.krakow.pl:3000/v0_1/timetables/" + hash);
		try{
			HttpResponse response = client.execute(get);
			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			sb = new StringBuilder();
			String line = "";
			while((line = br.readLine()) != null){
				sb.append(line);
			}
//			String json = sb.toString();
//			Log.v("t", json);
			JSONObject object = new JSONObject(sb.toString());
			JSONArray activities = object.getJSONArray("activities");
			for(int i = 0; i < activities.length(); i++){
				JSONObject activity = activities.getJSONObject(i);
				int id = activity.getInt("id");
				Log.v("t", "id: " + String.valueOf(id));
				String name = activity.getString("name");
				Log.v("t", "name : " + name);
				JSONObject group = activity.getJSONObject("group");
				int groupId = group.getInt("id");
				Log.v("t", "group id: " + groupId);
				String groupName = group.getString("name");
				Log.v("t", "group name: " + groupName);
				JSONObject tutor = activity.getJSONObject("tutor");
				int tutorId = tutor.getInt("id");
				Log.v("t", "tutor id: " + tutorId);
				String tutorName = tutor.getString("name");
				Log.v("t", "tutor name: " + tutorName);
				JSONObject place = activity.getJSONObject("place");
				int placeId = place.getInt("id");
				Log.v("t", "place id: " + placeId);
				String placeLocation = place.getString("location");
				Log.v("t", "place location: " + placeLocation);
				JSONObject category = activity.getJSONObject("category");
				int categoryId = category.getInt("id");
				Log.v("t", "category id: " + categoryId);
				String categoryName = category.getString("name");
				Log.v("t", "category name: " + categoryName);
				int state = activity.getInt("state");
				Log.v("t", "state: " + String.valueOf(state));
				long startAt = activity.getLong("start_at");
				Log.v("t", "start: " + String.valueOf(startAt));
				long endAt = activity.getLong("end_at");
				Log.v("t", "end: " + String.valueOf(endAt));
				Log.v("t", "////////////////////////");
				DatabaseManager.addActivity(DatabaseManager.getConnection().getWritableDatabase(), id, groupId, groupName, tutorId, tutorName, placeId, placeLocation, categoryId, categoryName, name, state, startAt, endAt);
			}
		}catch(IOException e){
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
