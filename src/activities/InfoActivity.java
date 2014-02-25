package activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import dev.rd.devplan.R;

/**
 * 
 * @author Robert Dzieża
 * 
 *         Activity responsible for representing information about program.
 * 
 */
public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_info_view);

		// LinearLayout header =
		// (LinearLayout)findViewById(R.layout.header_view);
		// if(header != null){
		// if(header.getVisibility() == LinearLayout.VISIBLE){
		// Log.v("t", "visible");
		// }else{
		// Log.v("t", "not visible");
		// }
		// }else{
		// Log.v("t", "doesnt exist");
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

}
