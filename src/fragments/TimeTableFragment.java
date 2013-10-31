package fragments;

import adapters.EventsListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.timetable.R;

import database.DatabaseHelper;

public class TimeTableFragment extends SherlockFragment{
	private ListView list;
	private Activity parent;
	private DatabaseHelper dbHelper;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState){
		View view =  inflater.inflate(R.layout.groups_list_view, containter, false);
		list = (ListView)view.findViewById(R.id.groupsListView);
		list.setAdapter(new EventsListAdapter(parent, dbHelper.getEventsCursor()));
		return view;
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		parent = activity;
		dbHelper = new DatabaseHelper(parent);
	}
	
	public void onDetach(){
		super.onDetach();
		dbHelper.close();
	}
	
	public void onDestroy(){
		super.onDestroy();
		dbHelper.close();
	}
	
	
}
