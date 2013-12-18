package activities;

import network.GroupsDownloader;
import prefereces.PreferenceHelper;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

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
	private Bundle extras = null;
	private ActionBar actionBar;
	private int fragmentAttached;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceHelper.initialize(getApplicationContext());
		// PreferenceHelper.saveBoolean("isFirst", true);
		if (!PreferenceHelper.getBoolean("isFirst")) {
			GroupsDownloader down = new GroupsDownloader(this);
			down.execute();
		}
		DatabaseManager.initialize(getApplicationContext());

		setContentView(R.layout.activity_main);
		extras = getIntent().getExtras();
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
			builder.setTitle("Filter");
			final LayoutInflater infl = (LayoutInflater) this
					.getSystemService(this.LAYOUT_INFLATER_SERVICE);
			View view = infl.inflate(R.layout.filters, null);
			View nameFilter = view.findViewById(R.id.nameFilter);
			nameFilter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					builder.setTitle("name filter");
					final EditText name = new EditText(getApplicationContext());
					name.setHint("Wpisz nazwe przedmiotu...");
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String nameText = name.getText().toString();
									Log.v("t", "name: " + nameText);
									Intent intent = new Intent(
											getApplicationContext(),
											MainView.class);
									intent.putExtra("action", "nameFilter");
									intent.putExtra("name", nameText);
									startActivity(intent);
								}
							});
					builder.setView(name);
					builder.setNegativeButton("Anuluj", null);
					builder.show();
				}
			});
			View dayFilter = view.findViewById(R.id.dayFilter);
			dayFilter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					builder.setTitle("day filter");
					final DatePicker date = new DatePicker(
							getApplicationContext());
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String day = date.getYear() + "-"
											+ (date.getMonth()+1) + "-"
											+ date.getDayOfMonth();
									Log.v("t", "date: " + day);
									Intent intent = new Intent(
											getApplicationContext(),
											MainView.class);
									intent.putExtra("action", "dayFilter");
									intent.putExtra("day", day);
									startActivity(intent);
								}
							});
					builder.setView(date);
					builder.setNegativeButton("Anuluj", null);
					builder.show();

				}
			});
			View sinceToday = view.findViewById(R.id.sinceToday);
			sinceToday.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(
							getApplicationContext(),
							MainView.class);
					startActivity(intent);
					
				}
				
			});
			View fullTable = view.findViewById(R.id.noFilter);
			fullTable.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(
							getApplicationContext(),
							MainView.class);
					intent.putExtra("action", "noFilter");
					startActivity(intent);
					
				}
				
			});
			View daysFilter = view.findViewById(R.id.daysFilter);
			daysFilter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					builder.setTitle("day filter");
					View view = infl.inflate(R.layout.days_filter, null);
					final DatePicker fromDate = (DatePicker) view
							.findViewById(R.id.dateFrom);
					final DatePicker toDate = (DatePicker) view
							.findViewById(R.id.dateTo);
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String from = fromDate.getYear() + "-"
											+ (fromDate.getMonth()+1) + "-"
											+ fromDate.getDayOfMonth();
									String to = toDate.getYear() + "-"
											+ (toDate.getMonth()+1)  + "-"
											+ toDate.getDayOfMonth();
									Log.v("t", "range: " + from + " - " + to);
									Intent intent = new Intent(
											getApplicationContext(),
											MainView.class);
									intent.putExtra("action", "daysFilter");
									intent.putExtra("from", from);
									intent.putExtra("to", to);
									startActivity(intent);

								}
							});
					builder.setView(view);
					builder.setNegativeButton("Anuluj", null);
					builder.show();

				}
			});
			builder.setView(view);
			builder.show();
		}
		
		return super.onKeyDown(keyCode, event);
	}

}
