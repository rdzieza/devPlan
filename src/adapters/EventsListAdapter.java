package adapters;

import com.example.timetable.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class EventsListAdapter extends CursorAdapter{

	public EventsListAdapter(Context context, Cursor c) {
		super(context, c, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView eventTopic = (TextView)view.findViewById(R.id.topic);
		eventTopic.setText(cursor.getString(cursor.getColumnIndex("TOPIC")));
		TextView eventType = (TextView)view.findViewById(R.id.type);
		eventType.setText(cursor.getString(cursor.getColumnIndex("TYPE")));
		TextView eventDate = (TextView)view.findViewById(R.id.date);
		eventDate.setText(cursor.getString(cursor.getColumnIndex("DATE")));
		TextView eventHours = (TextView)view.findViewById(R.id.hour);
		eventHours.setText(cursor.getString(cursor.getColumnIndex("HOURS")));
		TextView eventRoom = (TextView)view.findViewById(R.id.room);
		eventRoom.setText(cursor.getString(cursor.getColumnIndex("ROOM")));
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.single_event_row_view, parent, false);
		return view;
	}

}
