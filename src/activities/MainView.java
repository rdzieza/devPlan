package activities;

import network.GroupsDownloader;
import prefereces.PreferenceHelper;
import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import database.DatabaseManager;
import dev.rd.devplan.R;
import fragments.GroupsListFragment;
import fragments.MoreOptionsFragment;
import fragments.TimeTableFragment;

/**
 * Main Application view, responsible for users navigation.
 * 
 * @author robert dzieża
 * 
 */
@SuppressLint("NewApi")
public class MainView extends SherlockFragmentActivity implements
		ActionBar.TabListener {
	private Bundle extras = null;
	private ActionBar actionBar;
	private int fragmentAttached;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseManager.initialize(getApplicationContext());
		PreferenceHelper.initialize(getApplicationContext());

		setContentView(R.layout.activity_main);
		extras = getIntent().getExtras();
		// Utworzenie action bar'a
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

		if (!PreferenceHelper.getBoolean("areGroupsDownloaded")) {
			Log.v("t", "no groups downloaded");
			GroupsDownloader down = new GroupsDownloader(this);
			down.execute();
			actionBar.setSelectedNavigationItem(0);
		} else {
			actionBar.setSelectedNavigationItem(1);
			Log.v("t", "no need to download anything");
		}

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
			if (extras != null) {
				Log.v("t", extras.getString("action"));
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
			final Builder builder = new Builder(this);
			builder.setTitle(getString(R.string.filters_title));
			final LayoutInflater infl = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = infl.inflate(R.layout.filters, null);
			View nameFilter = view.findViewById(R.id.nameFilter);
			nameFilter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					builder.setTitle("Filtruj po nazwie");
					final ListView acticitiesNameList = new ListView(
							getApplicationContext());
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getApplicationContext(),
							R.layout.single_group_row_view, DatabaseManager
									.getActivitiesNameList());
					acticitiesNameList.setAdapter(adapter);
					acticitiesNameList.setBackgroundColor(Color.WHITE);
					acticitiesNameList
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> adapter,
										View view, int position, long id) {
									TextView nameView = (TextView) adapter
											.getChildAt(position);
									String name = nameView.getText().toString();
									Log.v("t", name);
									 Intent intent = new Intent(
											getApplicationContext(),
											MainView.class);
									intent.putExtra("action", "nameFilter");
									intent.putExtra("name", name);
									startActivity(intent);
								}

							});
					builder.setView(acticitiesNameList);
					builder.show();
				}
			});
//			View dayFilter = view.findViewById(R.id.dayFilter);
//			dayFilter.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					builder.setTitle("day filter");
//					final DatePicker date = new DatePicker(
//							getApplicationContext());
//					int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//					if (currentapiVersion >= 11) {
//						date.setCalendarViewShown(false);
//					}
//					builder.setPositiveButton("OK",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									String day = date.getYear() + "-"
//											+ (date.getMonth() + 1) + "-"
//											+ date.getDayOfMonth();
//									Log.v("t", "date: " + day);
//									Intent intent = new Intent(
//											getApplicationContext(),
//											MainView.class);
//									intent.putExtra("action", "dayFilter");
//									intent.putExtra("day", day);
//									startActivity(intent);
//								}
//							});
//					builder.setView(date);
//					builder.setNegativeButton("Anuluj", null);
//					builder.show();
//
//				}
//			});
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
//			View daysFilter = view.findViewById(R.id.daysFilter);
//			daysFilter.setOnClickListener(new OnClickListener() {
//
//				@SuppressLint("NewApi")
//				@Override
//				public void onClick(View v) {
//					builder.setTitle("day filter");
//					View view = infl.inflate(R.layout.days_filter, null);
//					final DatePicker fromDate = (DatePicker) view
//							.findViewById(R.id.dateFrom);
//					final DatePicker toDate = (DatePicker) view
//							.findViewById(R.id.dateTo);
//					int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//					if (currentapiVersion >= 11) {
//						fromDate.setCalendarViewShown(false);
//						toDate.setCalendarViewShown(false);
//					}
//					builder.setPositiveButton("OK",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									String from = fromDate.getYear() + "-"
//											+ (fromDate.getMonth() + 1) + "-"
//											+ fromDate.getDayOfMonth();
//									String to = toDate.getYear() + "-"
//											+ (toDate.getMonth() + 1) + "-"
//											+ toDate.getDayOfMonth();
//									Log.v("t", "range: " + from + " - " + to);
//									Intent intent = new Intent(
//											getApplicationContext(),
//											MainView.class);
//									intent.putExtra("action", "daysFilter");
//									intent.putExtra("from", from);
//									intent.putExtra("to", to);
//									startActivity(intent);
//
//								}
//							});
//					builder.setView(view);
//					builder.setNegativeButton("Anuluj", null);
//					builder.show();
//
//				}
//			});
			builder.setView(view);
			builder.show();
		}

		return super.onKeyDown(keyCode, event);
	}

}
