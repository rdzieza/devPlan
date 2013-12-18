package adapters;

import knp.rd.timetable.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class GroupsListAdapter extends CursorAdapter{

	
	
	
	public GroupsListAdapter(Context context, Cursor c) {
		super(context, c,true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView groupName = (TextView)view.findViewById(R.id.groupName);
		groupName.setText(cursor.getString(cursor.getColumnIndex("NAME")));
		if(cursor.getInt(cursor.getColumnIndex("IS_ACTIVE")) == 1){
			groupName.setTextColor(Color.parseColor("#0099CC"));
		}else{
			groupName.setTextColor(Color.GRAY);
		}
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.single_group_row_view, parent, false);
		return view;
	}

}
