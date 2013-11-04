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

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import database.DatabaseManager;

public class TimetTableCreator extends AsyncTask<Void, Void, Void>{
	private HttpClient client;
	private int numberOfselected;
	private Cursor selectedCursor;
	
	@Override
	public void onPreExecute(){
		super.onPreExecute();
		client = new DefaultHttpClient();
		selectedCursor = DatabaseManager.getSelected(DatabaseManager.getConnection().getReadableDatabase());
		numberOfselected = selectedCursor.getCount();
	}
	
	
	@Override
	protected Void doInBackground(Void... arg0) {
		if(numberOfselected == 0){
			Log.v("t", "ZEROOOOOOOO");
			return null;	
		}else{
			HttpPost post = new HttpPost("http://knp.uek.krakow.pl:3000/v0_1/timetables");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			while(selectedCursor.moveToNext()){
				params.add(new BasicNameValuePair("group_id[]", selectedCursor.getString(selectedCursor.getColumnIndex("ID"))));
			}
			for(NameValuePair pair : params){
				Log.v("t", pair.getName() + " : " + pair.getValue());
			}
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = client.execute(post);
				BufferedReader rd = new BufferedReader(
				        new InputStreamReader(response.getEntity().getContent()));
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
				PreferenceHelper.saveString("timeTableHash", hash);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		
	}
	
	@Override
	public void onPostExecute(Void v){
		super.onPostExecute(null);
		TimeTableDownloader tDown = new TimeTableDownloader();
		tDown.execute();
	}

}
