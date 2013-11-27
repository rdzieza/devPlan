package adapters;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.timetable.R;

public class EventsListAdapter extends CursorAdapter {
	String lastDay = "00-00";
	int lastUsed = 0;

	public EventsListAdapter(Context context, Cursor c) {
		super(context, c, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		String day = cursor.getString(cursor.getColumnIndex("DAY"));
		String dayOfWeek = cursor.getString(cursor.getColumnIndex("DAY_OF_WEEK"));
		TextView separator = (TextView)view.findViewById(R.id.separator);
		TextView eventTopic = (TextView) view.findViewById(R.id.activityTopic);
		String name = cursor.getString(cursor.getColumnIndex("NAME"));
		eventTopic.setText(name);
		TextView eventType = (TextView) view.findViewById(R.id.activityType);
		eventType.setText(cursor.getString(cursor
				.getColumnIndex("CATEGORY_NAME")));
		TextView eventDate = (TextView) view.findViewById(R.id.startTime);
		eventDate.setText(cursor.getString(cursor.getColumnIndex("START_AT")));
		TextView eventHours = (TextView) view.findViewById(R.id.endTime);
		eventHours.setText(cursor.getString(cursor.getColumnIndex("END_AT")));
		TextView eventRoom = (TextView) view.findViewById(R.id.activityRoom);
		eventRoom.setText(cursor.getString(cursor
				.getColumnIndex("PLACE_LOCATION")));
		if(day.equals(lastDay)){
			separator.setVisibility(View.GONE);
		}else{
			separator.setText(day + " " + dayOfWeek);
			lastDay = day;
			separator.setBackgroundColor(Color.parseColor("#0099CC"));
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.single_event_view_separator, parent,
				false);
		return view;
	}

}
