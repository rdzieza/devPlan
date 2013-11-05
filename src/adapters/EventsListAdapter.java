package adapters;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
		SimpleDateFormat df = new SimpleDateFormat("MM-DD hh:mm");
		TextView eventTopic = (TextView) view.findViewById(R.id.topic);
		eventTopic.setText(cursor.getString(cursor.getColumnIndex("NAME")));
		TextView eventType = (TextView) view.findViewById(R.id.type);
		eventType.setText(cursor.getString(cursor
				.getColumnIndex("CATEGORY_NAME")));
		TextView eventDate = (TextView) view.findViewById(R.id.date);
		eventDate.setText(df.format(new Date(cursor.getLong(cursor
				.getColumnIndex("START_AT")))));
		TextView eventHours = (TextView) view.findViewById(R.id.hour);
		eventHours.setText(df.format(new Date(cursor.getLong(cursor
				.getColumnIndex("END_AT")))));
		TextView eventRoom = (TextView) view.findViewById(R.id.room);
		eventRoom.setText(cursor.getString(cursor
				.getColumnIndex("PLACE_LOCATION")));

		SimpleDateFormat dayForm = new SimpleDateFormat("MM-DD");
		String day = dayForm.format(new Date(cursor.getLong(cursor
				.getColumnIndex("START_AT"))));
		if (!day.equals(lastDay)) {
			lastDay = day;
			if (lastUsed == 0) {
				eventHours.setTextColor(Color.parseColor("#0099CC"));
				eventDate.setTextColor(Color.parseColor("#0099CC"));
				lastUsed = 1;
			}else{
				eventHours.setTextColor(Color.GRAY);
				eventDate.setTextColor(Color.GRAY);
				lastUsed = 0;
			}
		}else{
			if (lastUsed == 1) {
				eventHours.setTextColor(Color.parseColor("#0099CC"));
				eventDate.setTextColor(Color.parseColor("#0099CC"));
				lastUsed = 1;
			}else{
				eventHours.setTextColor(Color.GRAY);
				eventDate.setTextColor(Color.GRAY);
				lastUsed = 0;
			}
		}

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.single_event_row_view, parent,
				false);
		return view;
	}

}
