package activities;

import prefereces.PreferenceHelper;
import network.GroupsDownloader;
import network.TimeTableDownloader;
import network.TimetTableCreator;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.timetable.R;

import database.DatabaseManager;

import fragments.GroupsListFragment;
import fragments.MoreOptionsFragment;
import fragments.TimeTableFragment;

/**
 * Main Application view, responsible for users navigation.
 * 
 * @author robert dzieża
 * 
 */
public class MainView extends SherlockFragmentActivity implements
		ActionBar.TabListener {
	private ActionBar actionBar;
	private int fragmentAttached;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceHelper.initialize(getApplicationContext());
//		PreferenceHelper.saveBoolean("isFirst", true);
		if (!PreferenceHelper.getBoolean("isFirst")) {
			GroupsDownloader down = new GroupsDownloader(this);
			down.execute();
		}
		DatabaseManager.initialize(getApplicationContext());
		

		setContentView(R.layout.activity_main);
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("groups")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("time table")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("more")
				.setTabListener(this));
		actionBar.setSelectedNavigationItem(1);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentTransaction trans = getSupportFragmentManager()
				.beginTransaction();
		int position = tab.getPosition();
		switch (position) {
		case 0: {
			GroupsListFragment groupsFragment = new GroupsListFragment();
			trans.replace(R.id.fragment, groupsFragment).commit();
		}
			break;
		case 1: {
			TimeTableFragment tableFragment = new TimeTableFragment();
			trans.replace(R.id.fragment, tableFragment).commit();
		}
			break;
		case 2: {
			MoreOptionsFragment optionsFragment = new MoreOptionsFragment();
			trans.replace(R.id.fragment, optionsFragment).commit();
		}
		}
		fragmentAttached = position;

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU && fragmentAttached == 1) {
			Toast.makeText(getApplicationContext(),
					"you will have some options in here", Toast.LENGTH_SHORT)
					.show();
			Log.v("t", "menu clicked");
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
