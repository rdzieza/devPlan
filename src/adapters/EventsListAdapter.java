package adapters;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.timetable.R;

public class EventsListAdapter extends CursorAdapter{

	public EventsListAdapter(Context context, Cursor c) {
		super(context, c, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		SimpleDateFormat df = new SimpleDateFormat("hh:mm");
		TextView eventTopic = (TextView)view.findViewById(R.id.topic);
		eventTopic.setText(cursor.getString(cursor.getColumnIndex("NAME")));
		TextView eventType = (TextView)view.findViewById(R.id.type);
		eventType.setText(cursor.getString(cursor.getColumnIndex("CATEGORY_NAME")));
		TextView eventDate = (TextView)view.findViewById(R.id.date);
		eventDate.setText(df.format(new Date(cursor.getLong(cursor.getColumnIndex("START_AT")))));
		TextView eventHours = (TextView)view.findViewById(R.id.hour);
		eventHours.setText(df.format(new Date(cursor.getLong(cursor.getColumnIndex("END_AT")))));
		TextView eventRoom = (TextView)view.findViewById(R.id.room);
		eventRoom.setText(cursor.getString(cursor.getColumnIndex("PLACE_LOCATION")));
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.single_event_row_view, parent, false);
		return view;
	}

}
