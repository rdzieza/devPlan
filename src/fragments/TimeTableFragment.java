package fragments;

import knp.rd.timetable.R;
import adapters.ActivityAdapter;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import classes.Event;
import classes.Item;

import com.actionbarsherlock.app.SherlockFragment;

import database.DatabaseManager;

public class TimeTableFragment extends SherlockFragment implements
		OnItemClickListener {
	private ListView list;
	private Activity parent;

	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.time_table_list_view, containter,
				false);
		list = (ListView) view.findViewById(R.id.timeTableListView);
		Bundle extras = this.getArguments();
		if (extras != null) {
			String action = extras.getString("action");
			if (action.equals("nameFilter")) {
				String name = extras.getString("name");
				list.setAdapter(new ActivityAdapter(parent, DatabaseManager
						.getEventsListByName(DatabaseManager.getConnection()
								.getReadableDatabase(), name)));
			} else if (action.equals("dayFilter")) {
				String day = extras.getString("day");
				list.setAdapter(new ActivityAdapter(parent, DatabaseManager
						.getEventsListByDay(DatabaseManager.getConnection()
								.getReadableDatabase(), day)));
			} else if (action.equals("daysFilter")) {
				String from = extras.getString("from");
				String to = extras.getString("to");
				list.setAdapter(new ActivityAdapter(parent, DatabaseManager
						.getEventsListFromRange(DatabaseManager.getConnection()
								.getReadableDatabase(), from, to)));
			} else if (action.equals("noFilter")) {
				list.setAdapter(new ActivityAdapter(parent, DatabaseManager
						.getEventsList(DatabaseManager.getConnection()
								.getReadableDatabase())));
			}
		} else {
			list.setAdapter(new ActivityAdapter(parent, DatabaseManager
					.getEventsListSincetToday(DatabaseManager.getConnection()
							.getReadableDatabase())));
		}
		list.setOnItemClickListener(this);
		return view;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = activity;
	}

	public void onDetach() {
		super.onDetach();
		// dbHelper.close();
	}

	public void onDestroy() {
		super.onDestroy();
		// dbHelper.close();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Item item = (Item) parent.getAdapter().getItem(position);
		if (!item.isHeaderType()) {
			Event event = (Event) item;
			Cursor cursor = DatabaseManager.getEventDetails(DatabaseManager
					.getConnection().getReadableDatabase(), event.getId());
			cursor.moveToFirst();
			// Log.v("t", cursor.getString(cursor.getColumnIndex("DAY")));
			// Log.v("t", cursor.getString(cursor.getColumnIndex("NAME")));
			String name = cursor.getString(cursor.getColumnIndex("NAME"));
			String day = cursor.getString(cursor.getColumnIndex("DAY"));
			String weekDay = cursor.getString(cursor
					.getColumnIndex("DAY_OF_WEEK"));
			// Log.v("t",
			// cursor.getString(cursor.getColumnIndex("CATEGORY_NAME")));
			String type = cursor.getString(cursor
					.getColumnIndex("CATEGORY_NAME"));
			// Log.v("t",
			// cursor.getString(cursor.getColumnIndex("PLACE_LOCATION")));
			String place = cursor.getString(cursor
					.getColumnIndex("PLACE_LOCATION"));
			// Log.v("t", cursor.getString(cursor.getColumnIndex("START_AT")));
			String startsAt = cursor.getString(cursor
					.getColumnIndex("START_AT"));
			// Log.v("t", cursor.getString(cursor.getColumnIndex("END_AT")));
			String endsAt = cursor.getString(cursor.getColumnIndex("END_AT"));
			// Log.v("t",
			// cursor.getString(cursor.getColumnIndex("TUTOR_NAME")));
			String tutorName = cursor.getString(cursor
					.getColumnIndex("TUTOR_NAME"));
			// Log.v("t", cursor.getString(cursor.getColumnIndex("TUTOR_URL")));
			String tutorUrl = cursor.getString(cursor
					.getColumnIndex("TUTOR_URL"));
			LayoutInflater infl = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View dialog = infl.inflate(R.layout.events_detail, null);
			TextView nameView = (TextView) dialog.findViewById(R.id.detailName);
			nameView.setText("Subject: " + name);
			TextView endsAtView = (TextView) dialog
					.findViewById(R.id.detailEnd);
			endsAtView.setText("Ends at: " + endsAt);
			TextView dateView = (TextView) dialog.findViewById(R.id.detailDate);
			dateView.setText(day + " " + weekDay);
			TextView roomView = (TextView) dialog.findViewById(R.id.detailRoom);
			roomView.setText("Room: " + place);
			TextView startView = (TextView) dialog
					.findViewById(R.id.detailStart);
			startView.setText("Starts at: " + startsAt);
			TextView tutorNameView = (TextView) dialog
					.findViewById(R.id.detailTutor);
			tutorNameView.setText("Lecturer: " + tutorName);
			TextView tutorUrlView = (TextView) dialog
					.findViewById(R.id.detailTutorUrl);
			tutorUrlView.setText("Lecturer home page: " + tutorUrl);
			TextView typeView = (TextView) dialog.findViewById(R.id.detailType);
			typeView.setText("Type: " + type);
			Builder builder = new Builder(parent.getContext());
			builder.setTitle(name);
			builder.setView(dialog);
			builder.show();
		}

	}

}
