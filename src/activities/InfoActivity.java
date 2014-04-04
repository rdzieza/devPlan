package activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import classes.ActivitiesStack;
import dev.rd.devplan.R;

/**
 * 
 * @author Robert Dzieża
 * 
 *         Activity responsible for representing information about program.
 * 
 */
public class InfoActivity extends Activity implements OnClickListener{
	private TextView homePageLabel;
	private TextView googlePlayLabel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_info_view);
		ActivitiesStack.add(this);
		homePageLabel = (TextView)findViewById(R.id.home_page_label);
		googlePlayLabel = (TextView)findViewById(R.id.google_play_label);
		homePageLabel.setOnClickListener(this);
		googlePlayLabel.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		String url = v.getTag().toString();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
		
	}

}
