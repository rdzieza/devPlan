package fragments;

import java.util.ArrayList;

import network.GroupsDownloader;
import activities.InfoActivity;
import activities.MainView;
import activities.ReportErrorActivity;
import adapters.OptionsListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import classes.Option;

import com.actionbarsherlock.app.SherlockFragment;

import dev.rd.devplan.R;

public class MoreOptionsFragment extends SherlockFragment implements OnItemClickListener{
	private ListView list;
	private MainView parent;
//	private String[] options = {"Podbierz rozklad", "Wykładowcy", "Sale", "WebVersion", "Sprawdz aktualnosc", "Info", "Aktualizuj grupy"};
//	private String[] options = {"Strona projektu", };
	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.options_list, containter,
				false);
		list = (ListView) view.findViewById(R.id.groupsListView);
//		List<String> optionsList = new LinkedList<String>();
//		optionsList.addAll(Arrays.asList(options));
//		list.setAdapter(new ArrayAdapter<String>(parent, R.layout.single_group_row_view, optionsList));
//		list.setOnItemClickListener(this);
		ArrayList<Option> items = new ArrayList<Option>();
//		items.add(new Option(R.drawable.download, "Pobierz rozkład"));
//		items.add(new Option(R.drawable.person, "Wykładowcy"));
//		items.add(new Option(R.drawable.room, "Sale"));
		items.add(new Option(R.drawable.ic_web, "Strona projektu"));
		items.add(new Option(R.drawable.ic_check, "Sprawdź aktualność"));
		items.add(new Option(R.drawable.ic_groups, "Aktualizuj listę grup"));
		items.add(new Option(R.drawable.ic_error, "Zgłoś błąd"));
		items.add(new Option(R.drawable.ic_info, "O programie"));
		list.setAdapter(new OptionsListAdapter(parent, items));
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
//		case 0: {
//			Toast.makeText(this.parent, "Downloading started", Toast.LENGTH_SHORT).show();
//			DatabaseManager.removeTimeTable(DatabaseManager.getConnection().getWritableDatabase());
//			TimetTableCreator tDown = new TimetTableCreator(this.parent);
//			tDown.execute();
//		}break;
//		case 1:{
//			String url = "http://knp.uek.krakow.pl/~planzajec/index.php/tutors";
//			openBrowser(url);			
//		}break;
//		case 2:{
//			String url = "http://knp.uek.krakow.pl/~planzajec/index.php/places";
//			openBrowser(url);		
//		}break;
//		case 3:{
//			String url = "http://knp.uek.krakow.pl/~planzajec/index.php/";
//			openBrowser(url);
//		}break;
//		case 4:{
//			Toast.makeText(this.parent, "Aktualne", Toast.LENGTH_SHORT).show();
//		}break;
//		case 5:{
//			Intent intent = new Intent(parent.getContext(), InfoActivity.class);
//			startActivity(intent);
//		}case 6: {
//			GroupsDownloader groupDown = new GroupsDownloader(getActivity());
//			groupDown.execute();
//		}break;
		case 0: {
			String url = getString(R.string.home_page);
			openBrowser(url);
		}break;
		case 1:{
			Toast.makeText(this.parent, "Checkin for update", Toast.LENGTH_LONG).show();			
		}break;
		case 2:{
			GroupsDownloader groupDown = new GroupsDownloader(getActivity());
			groupDown.execute();		
		}break;
		case 3:{
			Intent intent = new Intent(parent.getContext(), ReportErrorActivity.class);
			startActivity(intent);
		}break;
		case 4:{
			Intent intent = new Intent(parent.getContext(), InfoActivity.class);
			startActivity(intent);
		}break;
		}
		
	}
	
	public void openBrowser(String url){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}
}
