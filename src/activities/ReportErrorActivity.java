package activities;

import network.ErrorReporter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import dev.rd.devplan.R;

public class ReportErrorActivity extends Activity {
	private EditText emailField;
	private EditText descriptionField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error_message_view);
		emailField = (EditText) findViewById(R.id.email_address);
		descriptionField = (EditText) findViewById(R.id.error_description);

	}

	public void sendRaport(View view) {
		String email = emailField.getText().toString();
		String description = descriptionField.getText().toString();
		String osInfo = System.getProperty("os.version") + ", SDK version: " + android.os.Build.VERSION.SDK_INT;
		String deviceInfo = android.os.Build.DEVICE;
		
		if(email == null | description == null || email.equals("") || description.equals("")){
			Toast.makeText(this, "Pola nie zostały poprawnie wypełnione", Toast.LENGTH_SHORT).show();
		}else{
			ErrorReporter errorReporter = new ErrorReporter(this, email, description, osInfo, deviceInfo);
			errorReporter.execute();
		}
	}
}
