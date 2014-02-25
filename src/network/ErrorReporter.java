package network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;
import dev.rd.devplan.R;

/**
 * 
 * @author Robert Dzieża
 * 
 *         AsyncTask responsible for crating POST request in order to report an
 *         error.
 * 
 */
public class ErrorReporter extends AsyncTask<Void, Void, Void> {
	private String url = "http://devplan.uek.krakow.pl/devPlanAdmin/index.php/issue/create";
	private String email;
	private String description;
	private String osInfo;
	private String deviceInfo;
	private HttpPost httpPost;
	private HttpClient client;
	private Boolean isConnected;
	private String message = "";
	private Context context;

	public ErrorReporter(Context context, String email, String description,
			String osInfo, String deviceInfo) {
		this.email = email;
		this.description = description;
		this.osInfo = osInfo;
		this.deviceInfo = deviceInfo;
		this.context = context;

	}

	@Override
	protected Void doInBackground(Void... params) {
		isConnected = checkConnection();

		if (!isConnected) {
			this.cancel(true);
			message += "Brak połączenia z internetem";
		} else {

			client = new DefaultHttpClient();
			httpPost = new HttpPost(url);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("email", email));
			parameters.add(new BasicNameValuePair("content", description));
			parameters.add(new BasicNameValuePair("device", deviceInfo));
			parameters
					.add(new BasicNameValuePair("device_information", osInfo));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(parameters));
				client.execute(httpPost);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				this.cancel(true);
				message += "Błąd kodowania, problem z serwerem";
				return null;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				this.cancel(true);
				message += "Błąd protokołu, problem z serwerem";
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				this.cancel(true);
				message += "Błąd operacji wejścia-wyjścia";
				return null;
			}
		}
		return null;
	}

	@Override
	public void onPostExecute(Void v) {
		if (!isCancelled()) {
//			Log.v("t", "Error reported");
			Toast.makeText(context, context.getString(R.string.error_reported),
					Toast.LENGTH_SHORT).show();
			Activity activity = (Activity) context;
			EditText descriptionField = (EditText) activity
					.findViewById(R.id.error_description);
			if (descriptionField != null) {
				descriptionField.setText("");
			}
		} else {
			Toast.makeText(context, "Wystąpił problem: " + message,
					Toast.LENGTH_SHORT).show();
		}
	}

	public boolean checkConnection() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
//		Log.v("t", "Connected: " + String.valueOf(isConnected));
		return isConnected;
	}

}
