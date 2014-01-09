package adapters;

import knp.rd.timetable.R;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SelectedGroupsAdapter extends CursorAdapter{

	public SelectedGroupsAdapter(Context context, Cursor c) {
		super(context, c,true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView groupName = (TextView)view.findViewById(R.id.groupName);
		groupName.setText(cursor.getString(cursor.getColumnIndex("NAME")));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.single_group_row_view, parent, false);
		return view;
	}
}
