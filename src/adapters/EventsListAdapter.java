package adapters;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import classes.Item;
import dev.rd.devplan.R;

public class EventsListAdapter extends CursorAdapter {
	String lastDay = "00-00";
	ArrayList<Item> items = new ArrayList<Item>();
	int lastUsed = 0;

	public EventsListAdapter(Context context, Cursor cursor) {
		super(context, cursor, false);
//		while(cursor.moveToNext()){
//			String day = cursor.getString(cursor.getColumnIndex("DAY"));
//			if(!lastDay.equals(day)){
//				lastDay = day;
//				Separator separator = new Separator(day);
//				items.add(separator);
//			}
//			Event event = new Event(cursor.getInt(cursor.getColumnIndex("_id")),
//					cursor.getString(cursor.getColumnIndex("NAME")),
//					cursor.getString(cursor.getColumnIndex("START_AT")),
//					cursor.getString(cursor.getColumnIndex("END_AT")),
//					cursor.getLong(cursor.getColumnIndex("TIME")),
//					cursor.getString(cursor.getColumnIndex("PLACE_LOCATION")),
//					cursor.getString(cursor.getColumnIndex("CATEGORY_NAME")));
//			items.add(event);
//		}
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
//		String day = cursor.getString(cursor.getColumnIndex("DAY"));
//		String dayOfWeek = cursor.getString(cursor.getColumnIndex("DAY_OF_WEEK"));
//		TextView separator = (TextView)view.findViewById(R.id.separator);
//		TextView eventTopic = (TextView) view.findViewById(R.id.activityTopic);
//		String name = cursor.getString(cursor.getColumnIndex("NAME"));
//		eventTopic.setText(name);
//		TextView eventType = (TextView) view.findViewById(R.id.activityType);
//		eventType.setText(cursor.getString(cursor
//				.getColumnIndex("CATEGORY_NAME")));
//		TextView eventDate = (TextView) view.findViewById(R.id.startTime);
//		eventDate.setText(cursor.getString(cursor.getColumnIndex("START_AT")));
//		TextView eventHours = (TextView) view.findViewById(R.id.endTime);
//		eventHours.setText(cursor.getString(cursor.getColumnIndex("END_AT")));
//		TextView eventRoom = (TextView) view.findViewById(R.id.activityRoom);
//		eventRoom.setText(cursor.getString(cursor
//				.getColumnIndex("PLACE_LOCATION")));
//		if(day.equals(lastDay)){
//			separator.setVisibility(View.GONE);
//		}else{
//			separator.setText(day + " " + dayOfWeek);
//			lastDay = day;
//			separator.setBackgroundColor(Color.parseColor("#0099CC"));
//		}
		
		
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
