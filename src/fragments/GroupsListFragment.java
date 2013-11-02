package fragments;

import adapters.GroupsListAdapter;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.timetable.R;

import database.DatabaseManager;

public class GroupsListFragment extends SherlockFragment implements
		OnItemClickListener {
	private ListView list;
	private Activity parent;
//	private DatabaseHelper dbHelper;

	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.groups_list_view, containter,
				false);
		list = (ListView) view.findViewById(R.id.groupsListView);
		list.setAdapter(new GroupsListAdapter(parent, DatabaseManager.getGroupsCursor(DatabaseManager.getConnection().getReadableDatabase())));
		list.setOnItemClickListener(this);
		return view;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = activity;
//		if (dbHelper == null) {
//			dbHelper = new DatabaseHelper(parent);
//		}
//		update();
	}
	
	public void onResume(){
		super.onResume();
//		update();
	}
	
	public void update(){
		list.setAdapter(new GroupsListAdapter(parent, DatabaseManager.getGroupsCursor(DatabaseManager.getConnection().getReadableDatabase())));
	}
	
	public void onDetach(){
		super.onDetach();
		
	}
	
	public void onDestroy(){
		super.onDestroy();
		
	}
	
	
	
	@Override
	public void onItemClick( AdapterView<?> parent, View view, int position, long id) {
		final long selected = id;
		Builder builder = new Builder(this.parent);
		builder.setMessage("Set as active?");
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.v("t", "agreed: " + String.valueOf(selected));
				DatabaseManager.changeStatus(selected, DatabaseManager.getConnection().getReadableDatabase());
				update();
				
			}
			
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.v("t", "denied: " + String.valueOf(selected));
				
			}
		});
		builder.create().show();
		
	}

	

	

	

}
