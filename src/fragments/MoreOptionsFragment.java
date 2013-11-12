package fragments;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import network.TimetTableCreator;
import activities.InfoActivity;
import activities.MainView;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.example.timetable.R;

import database.DatabaseManager;

public class MoreOptionsFragment extends SherlockFragment implements OnItemClickListener{
	private ListView list;
	private MainView parent;
	private String[] options = {"Podbierz rozklad", "Wykładowcy", "Sale", "WebVersion", "Sprawdz aktualnosc", "Info"};
	
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
		this.parent = (MainView)activity;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		switch(position){
		case 0: {
			Toast.makeText(this.parent, "Downloading started", Toast.LENGTH_SHORT).show();
			DatabaseManager.removeTimeTable(DatabaseManager.getConnection().getWritableDatabase());
			TimetTableCreator tDown = new TimetTableCreator();
			tDown.execute();
		}break;
		case 1:{
			String url = "http://knp.uek.krakow.pl/~planzajec/index.php/tutors";
			openBrowser(url);			
		}break;
		case 2:{
			String url = "http://knp.uek.krakow.pl/~planzajec/index.php/places";
			openBrowser(url);		
		}break;
		case 3:{
			String url = "http://knp.uek.krakow.pl/~planzajec/index.php/";
			openBrowser(url);
		}break;
		case 4:{
			Toast.makeText(this.parent, "Aktualne", Toast.LENGTH_SHORT).show();
		}break;
		case 5:{
			Intent intent = new Intent(parent.getContext(), InfoActivity.class);
			startActivity(intent);
		}
		}
		
	}
	
	public void openBrowser(String url){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}
}
