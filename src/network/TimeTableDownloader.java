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
	int i = 0;

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
		HttpGet get = new HttpGet(
				"http://knp.uek.krakow.pl:3000/v0_1/timetables/" + hash);
		try {
			HttpResponse response = client.execute(get);
			br = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			sb = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			// String json = sb.toString();
			// Log.v("t", json);
			JSONObject object = new JSONObject(sb.toString());
			JSONArray activities = object.getJSONArray("activities");
			for (int i = 0; i < activities.length(); i++) {
				JSONObject activity = activities.getJSONObject(i);
				Log.v("t", activity.toString());
				String name = activity.getString("name");
				if (name.equals("Język obcy")) {
					continue;
				}
				int id = activity.getInt("id");
				Log.v("t", "id: " + String.valueOf(id));
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
				String tutorUrl = tutor.getString("moodle_url");
				Log.v("t", "tutor url: " + tutorUrl);
				String placeLocation = null;
				int placeId = 0;
				try {
					JSONObject place = activity.getJSONObject("place");
					placeId = place.getInt("id");
					Log.v("t", "place id: " + placeId);
					placeLocation = place.getString("location");
				} catch (JSONException e) {
					placeLocation = activity.getString("place");
				}
				Log.v("t", "place location: " + placeLocation);
				// JSONObject category = activity.getJSONObject("category");
				// int categoryId = category.getInt("id");
				// Log.v("t", "category id: " + categoryId);
				String categoryName = activity.getString("category");
				Log.v("t", "category name: " + categoryName);
				int state = activity.getInt("state");
				Log.v("t", "state: " + String.valueOf(state));
				String startAt = activity.getString("starts_at");
				Log.v("t", "start: " + startAt);
				String endAt = activity.getString("ends_at");
				Log.v("t", "end: " + String.valueOf(endAt));
				String day = activity.getString("date");
				Log.v("t", "Date: " + day);
				String notes = activity.getString("notes");
				Log.v("t", "Notes: " + notes);
				String dayOfWeek = activity.getString("day_of_week");
				Log.v("t", "Day of week: " + dayOfWeek);
				Log.v("t", "////////////////////////");
				 DatabaseManager.addActivity(DatabaseManager.getConnection().getWritableDatabase(),
				 id, groupId, groupName, tutorId, tutorName, tutorUrl, placeId,
				 placeLocation, categoryName, notes, name, state, day, dayOfWeek,
				 startAt, endAt);
//				 if(i == 8){
//					 break;
//				 }else{
//					 i++;
//				 }
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
