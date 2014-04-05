package activities;

import network.GroupsDownloader;
import prefereces.PreferenceHelper;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import classes.ActivitiesStack;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import database.DatabaseConnectionManager;
import database.DatabaseManager;
import dev.rd.devplan.R;
import fragments.GroupsListFragment;
import fragments.MoreOptionsFragment;
import fragments.TimeTableFragment;

/**
 * 
 * @author Robert Dzieża
 * 
 *         Main Application view, responsible for users navigation.
 * 
 */
@SuppressLint("NewApi")
public class MainView extends SherlockFragmentActivity implements
		ActionBar.TabListener {
	private Bundle extras = null;
	private ActionBar actionBar;
	private int fragmentAttached;
	private AlertDialog filterDialog;
	private AlertDialog nameFfilterDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PreferenceHelper.initialize(getApplicationContext());
		DatabaseManager.initialize(getApplicationContext());
		DatabaseConnectionManager.initialize(getApplicationContext());
		
		ActivitiesStack.add(this);
		
		setContentView(R.layout.activity_main);
		
		extras = getIntent().getExtras();
		
		PreferenceHelper.saveString("filterString", "brak");
		
		createActionBar();

		if (!PreferenceHelper.getBoolean("areGroupsDownloaded")) {
			GroupsDownloader down = new GroupsDownloader(this);
			down.execute();
			actionBar.setSelectedNavigationItem(0);
		} else {
			actionBar.setSelectedNavigationItem(1);
		}

	}
	
	public void createActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.groups_tab_label))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.timetable_tab_label))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.options_tab_label))
				.setTabListener(this));
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		int position = tab.getPosition();
		loadFragment(position, extras);
	}
	
	public void loadFragment(int position, Bundle extras) {
		FragmentTransaction trans = getSupportFragmentManager()
				.beginTransaction();
		switch (position) {
		case 0: {
			GroupsListFragment groupsFragment = new GroupsListFragment();
			trans.replace(R.id.fragment, groupsFragment).commit();
		}
			break;
		case 1: {
			TimeTableFragment tableFragment = new TimeTableFragment();
			if (extras != null) {
				tableFragment.setArguments(extras);
			}
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
			createFilterDialog();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPause() {
		if (filterDialog != null) {
			filterDialog.dismiss();
		}
		if (nameFfilterDialog != null) {
			nameFfilterDialog.dismiss();
		}
		super.onPause();
	}

	public void createFilterDialog() {
		final Builder builder = new Builder(this);
		builder.setTitle(getString(R.string.filters_title));
		final LayoutInflater infl = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = infl.inflate(R.layout.filters, null);
		View nameFilter = view.findViewById(R.id.nameFilter);
		
		nameFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nameFfilterDialog = createFilterByNameDialog(builder);
				nameFfilterDialog.show();
			}
		});

		View sinceToday = view.findViewById(R.id.sinceToday);
		sinceToday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(),
						MainView.class);
				startActivity(intent);

			}

		});
		
		View fullTable = view.findViewById(R.id.noFilter);
		fullTable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(),
						MainView.class);
				intent.putExtra("action", "noFilter");
				startActivity(intent);

			}

		});

		builder.setView(view);
		filterDialog = builder.create();
		filterDialog.show();
	}

	public AlertDialog createFilterByNameDialog(Builder builder) {
		builder.setTitle("Filtruj po nazwie");
		ListView activitiesListView = createActivitiesListView();
		builder.setView(activitiesListView);
		return builder.create();
	}
	
	public ListView createActivitiesListView() {
		ListView activitiesNameList = new ListView(
				getApplicationContext());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.single_group_row_view,
				DatabaseManager.getActivitiesNameList());
		activitiesNameList.setAdapter(adapter);
		activitiesNameList.setBackgroundColor(Color.WHITE);
		activitiesNameList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				TextView nameView = (TextView) adapter.getChildAt(position);
				String name = nameView.getText().toString();
				Intent intent = new Intent(getApplicationContext(),
						MainView.class);
				intent.putExtra("action", "nameFilter");
				intent.putExtra("name", name);
				startActivity(intent);
			}
		});
		return activitiesNameList;
	}
	
	
}
