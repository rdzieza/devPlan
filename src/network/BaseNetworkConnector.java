package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class BaseNetworkConnector extends AsyncTask<Void, Void, Void> {
	protected String message="";
	protected Context context;
	protected HttpClient client;
	
	@Override
	protected void onPreExecute() {
		Log.v("t", "Base - onPreExecute()");
		this.client = new DefaultHttpClient();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return null;
	}

	protected boolean checkConnection() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		Log.v("t", "isConnected() : " + String.valueOf(isConnected));
		return isConnected;
	}

	protected String readResponse(HttpResponse response) {
		StringBuilder sb = new StringBuilder();
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			cancelWithMessage("Problem with server response content");
		}
		return sb.toString();
	}
	
	@Override
	protected void onCancelled(Void result) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	protected void cancelWithMessage(String msg) {
		this.message += msg;
		this.cancel(true);
	}
	
	public void handleException(Exception e) {
		cancelWithMessage("Something is wrong");
	}
	}

}
