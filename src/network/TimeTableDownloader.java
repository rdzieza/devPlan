package network;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import prefereces.PreferenceHelper;
import android.os.AsyncTask;
import android.util.Log;

public class TimeTableDownloader extends AsyncTask<Void, Void, Void>{
	private String hash;
	private HttpClient client;
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		hash = PreferenceHelper.getString("timeTableHash");
		client = new DefaultHttpClient();
	}
	@Override
	protected Void doInBackground(Void... params) {
		String url = "http://knp.uek.krakow.pl:3000/v0_1/timetables/" + hash;
		Log.v("t", url);
		HttpGet get = new HttpGet("http://knp.uek.krakow.pl:3000/v0_1/timetables/" + hash);
		
		return null;
	}
	
}
