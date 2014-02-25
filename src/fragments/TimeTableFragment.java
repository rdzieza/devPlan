package fragments;

import adapters.ActivityAdapter;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import classes.DownloadManager;
import classes.Event;
import classes.Item;

import com.actionbarsherlock.app.SherlockFragment;

import database.DatabaseManager;
import dev.rd.devplan.R;

/**
 * 
 * @author Robert Dzieża 
 * 		   Fragment responsible for presenting users time table
 *         and handle users filter actions.
 */
public class TimeTableFragment extends SherlockFragment implements
		OnItemClickListener {
	private ListView list;
	private Activity parent;

	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.time_table_list_view, containter,
				false);
		list = (ListView) view.findViewById(R.id.timeTableListView);
		
		if(DownloadManager.isDownloadingTimeTable()){
			ProgressBar loadingBar = (ProgressBar)view.findViewById(R.id.loadingTimeTableBar);
			TextView label = (TextView)view.findViewById(R.id.loadingTimeTableText);
			loadingBar.setVisibility(View.VISIBLE);
			label.setVisibility(View.VISIBLE);
		}
		
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
			String name = cursor.getString(cursor.getColumnIndex("NAME"));
			String day = cursor.getString(cursor.getColumnIndex("DAY"));
			String weekDay = cursor.getString(cursor
					.getColumnIndex("DAY_OF_WEEK"));
			String type = cursor.getString(cursor
					.getColumnIndex("CATEGORY_NAME"));
			String place = cursor.getString(cursor
					.getColumnIndex("PLACE_LOCATION"));
			String startsAt = cursor.getString(cursor
					.getColumnIndex("START_AT"));
			String endsAt = cursor.getString(cursor.getColumnIndex("END_AT"));
			String tutorName = cursor.getString(cursor
					.getColumnIndex("TUTOR_NAME"));
			String tutorUrl = cursor.getString(cursor
					.getColumnIndex("TUTOR_URL"));

			LayoutInflater infl = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View dialog = infl.inflate(R.layout.events_detail, null);

			TextView endsAtView = (TextView) dialog
					.findViewById(R.id.detailEnd);
			endsAtView.setText("Koniec: " + endsAt);

			TextView dateView = (TextView) dialog.findViewById(R.id.detailDate);
			dateView.setText(day + " " + getFullName(weekDay));
			//
			TextView roomView = (TextView) dialog.findViewById(R.id.detailRoom);
			roomView.setText("Sala: " + place);

			TextView startView = (TextView) dialog
					.findViewById(R.id.detailStart);
			startView.setText("Początek: " + startsAt);

			TextView tutorNameView = (TextView) dialog
					.findViewById(R.id.detailTutor);
			tutorNameView.setText("Prowadzący: " + tutorName);

			TextView tutorUrlLabel = (TextView) dialog
					.findViewById(R.id.detailTutorUrlLabel);
			tutorUrlLabel.setText("Strona prowadzącego: ");

			TextView tutorUrlView = (TextView) dialog
					.findViewById(R.id.detailTutorUrl);
			tutorUrlView.setText(tutorUrl);
			tutorUrlView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					TextView urlView = (TextView) v;
					String url = urlView.getText().toString();
					intent.setData(Uri.parse(url));
					startActivity(intent);

				}
			});

			TextView typeView = (TextView) dialog.findViewById(R.id.detailType);
			typeView.setText(type);

			Builder builder = new Builder(parent.getContext());
			TextView title = new TextView(this.parent);
			title.setBackgroundColor(Color.WHITE);
			title.setGravity(Gravity.CENTER);
			title.setPadding(10, 10, 10, 10);
			title.setTextColor(Color.BLACK);
			title.setText(name);
			title.setTextSize(20);

			builder.setCustomTitle(title);
			builder.setView(dialog);
			builder.show();
		}

	}

	public String getFullName(String name) {
		if (name.equals("Pn")) {
			return "Poniedziałek";
		} else if (name.equals("Wt")) {
			return "Wtorek";
		} else if (name.equals("Śr")) {
			return "Środa";
		} else if (name.equals("Cz")) {
			return "Czwartek";
		} else if (name.equals("Pt")) {
			return "Piątek";
		} else if (name.equals("Sb")) {
			return "Sobota";
		} else {
			return "Niedziela";
		}
	}

}
