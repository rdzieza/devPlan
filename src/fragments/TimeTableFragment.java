package fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.timetable.R;

public class TimeTableFragment extends SherlockFragment{
	private ListView list;
	private String[] groups = {"zajecia rano", "zajecia potem", "zajecia wieczorem"};
	private Activity parent;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState){
		View view =  inflater.inflate(R.layout.groups_list_view, containter, false);
		list = (ListView)view.findViewById(R.id.groupsListView);
		list.setAdapter(new ArrayAdapter<String>(parent, R.layout.single_group_row_view, groups));
		return view;
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		parent = activity;
	}
}
