package fragments;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import network.TimetTableCreator;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.example.timetable.R;

import database.DatabaseManager;

public class MoreOptionsFragment extends SherlockFragment implements OnItemClickListener{
	private ListView list;
	private Activity parent;
	private String[] options = {"Podbierz rozklad", "Grupy", "Wykładowcy", "Sale", "WebVersion", "Sprawdz aktualnosc", "Info"};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.groups_list_view, containter,
				false);
		list = (ListView) view.findViewById(R.id.groupsListView);
		List<String> optionsList = new LinkedList<String>();
		optionsList.addAll(Arrays.asList(options));
		list.setAdapter(new ArrayAdapter<String>(parent, R.layout.single_group_row_view, optionsList));
		list.setOnItemClickListener(this);
		return view;
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.parent = activity;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		switch(position){
		case 0: {
			Toast.makeText(this.parent, "Downloading started", Toast.LENGTH_SHORT).show();
			DatabaseManager.removeTimeTable(DatabaseManager.getConnection().getWritableDatabase());
			TimetTableCreator tDown = new TimetTableCreator();
			tDown.execute();
		}
		}
		
	}
}
