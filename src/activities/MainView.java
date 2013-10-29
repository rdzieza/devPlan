package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.timetable.R;

import fragments.GroupsListFragment;
import fragments.TimeTableFragment;
/**
 * Main Application view, responsible for users navigation.
 * @author robert dzieża
 *
 */
public class MainView extends SherlockFragmentActivity implements
		ActionBar.TabListener {
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("groups")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("time table")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("settings")
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
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
		}

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
