package activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import dev.rd.devplan.R;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_view);
//		TextView messageView = (TextView)findViewById(R.id.infoMessage);
//		String message = "Projekt UekTimeTable\n"
//				+"versja: 0.1 beta\n"
//				+"Przygotowany przez KNP >dev\n\n\n"
//				+"autorzy:\n"
//				+"Robert Dzieża\n"
//				+"Maciek Komorowski";
//		messageView.setText(message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}
	
	

}


