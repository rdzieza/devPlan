/**
 * 
 */
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import prefereces.PreferenceHelper;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author robert
 *
 */
public class VersionChecker extends AsyncTask<Void, Void, Void>{
	private Context context;
	private HttpClient client;
	private StringBuilder sb;
	private String message = "";
	private BufferedReader br;
	int result;
	
	public VersionChecker(Context context){
		this.context = context;
	}
	
	@Override
	protected void onPreExecute(){
		client = new DefaultHttpClient();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		boolean isConnected = checkConnection();
		if(!isConnected){
			message += "Brak połączenia z internetem";
			this.cancel(true);
		}else{
			String url = "http://cash.dev.uek.krakow.pl:3000/v0_1/timetables/"
					+ PreferenceHelper.getString("timeTableUrl") + "/versions";
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
				
				Log.v("check for ubpdate versions: ", sb.toString());
				String downloadedVersions = sb.toString();
				String savedVersions = PreferenceHelper.getString("versions");
				Log.v("saved versions: ", savedVersions);
				
				
				if(downloadedVersions.contains(savedVersions)){
//					Log.v("t", "contains");
//					Builder builder = new Builder(context);
//					TextView text = new TextView(context);
//					text.setText("Plan zajęć aktualny!");
//					builder.setView(text);
//					builder.show();
					result = 0;
				}else{
					result = 1;
//					Builder builder = new Builder(context);
//					TextView text = new TextView(context);
//					text.setText("Plan zajęć nie aktualny! Pobrać nowszy plan?");
//					builder.setView(text);
//					builder.show();
				}
				
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}
	
	@Override
	protected void onPostExecute(Void v) {
		if(isCancelled()){
//			Log.v("t", "canceled");
		}else{
			Builder builder = new Builder(context);
			TextView text = new TextView(context);
			text.setBackgroundColor(Color.WHITE);
			text.setGravity(Gravity.CENTER);
			text.setTextSize(18);
			String info = "";
			builder.setTitle("Wersja planu");
			if(result == 0){
				info = "Plan zajęć aktualny";
				builder.setPositiveButton("OK", null);
				
			}else{
				info = "Plan zajęć nie aktualny!";
				builder.setNegativeButton("Anuluj", null);
				builder.setPositiveButton("OK", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						TimeTableDownloader tdown = new TimeTableDownloader(context);
						tdown.execute();
						
					}
				});
			}
			text.setText(info);
			builder.setView(text);
			builder.show();
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
