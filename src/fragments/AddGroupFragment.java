package fragments;

import network.TimeTableDownloader;
import network.TimetTableCreator;
import adapters.GroupsListAdapter;
import adapters.SelectedGroupsAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import classes.DownloadManager;

import com.actionbarsherlock.app.SherlockFragment;

import database.DatabaseManager;
import dev.rd.devplan.R;

/**
 * 
 * @author Robert Dzieża
 * 
 *         Fragment responsible for marking groups as selected, allows user to
 *         filter group list by the name and refresh time table content.
 */
public class AddGroupFragment extends SherlockFragment {
	private Activity parent;
	private ListView allGroupsList;
	private GroupsListAdapter adapter;
	private Button updateTimetable;
	private EditText filterField;
	private AddGroupFragment selfPointer;

	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_group_fragment_view,
				containter, false);
		selfPointer = this;
		allGroupsList = (ListView) view.findViewById(R.id.groupsListView);
		updateTimetable = (Button) view
				.findViewById(R.id.updateTimeTableButton);
		filterField = (EditText) view.findViewById(R.id.filterField);
		
		if(DownloadManager.isDowloadingGroups()){
			ProgressBar groupsBar = (ProgressBar)view.findViewById(R.id.loadingGroupsBar);
			groupsBar.setVisibility(View.VISIBLE);
			TextView downloadingLabel = (TextView)view.findViewById(R.id.loadingGroupText);
			downloadingLabel.setVisibility(View.VISIBLE);
		}
		
		setAdapter();
		fixFilter();

		allGroupsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				DatabaseManager.setAsActive(id, DatabaseManager.getConnection()
						.getWritableDatabase());
				setAdapter();

				ListView selected = (ListView) parent
						.findViewById(R.id.selectedGroupsList);
				SelectedGroupsAdapter selectedAdapter = new SelectedGroupsAdapter(
						parent, DatabaseManager
								.getSelectedWithNames(DatabaseManager
										.getConnection().getReadableDatabase()));
				selected.setAdapter(selectedAdapter);
				selected.setVisibility(View.GONE);
			}
		});

		updateTimetable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(parent, parent.getString(R.string.download_started),
						Toast.LENGTH_SHORT).show();
				TimetTableCreator tDown = new TimetTableCreator(parent,
						selfPointer);
				tDown.execute();

			}
		});
		DownloadManager.setAddGroupFragment(selfPointer);
		return view;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = activity;
	}

	public void fixFilter() {
		Log.v("t", "fixin filter");
		filterField.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				adapter.getFilter().filter(s.toString());

			}
		});

		adapter.setFilterQueryProvider(new FilterQueryProvider() {

			@Override
			public Cursor runQuery(CharSequence constraint) {
				Cursor c = DatabaseManager.getWhereName(DatabaseManager
						.getConnection().getReadableDatabase(), constraint
						.toString());
				return c;
			}
		});
	}
	
	
	

	public void setAdapter() {
		String text = filterField.getText().toString();
		if (text == null || text.equals("")) {
			adapter = new GroupsListAdapter(parent,
					DatabaseManager.getGroupsCursor(DatabaseManager
							.getConnection().getReadableDatabase()));
		} else {
			adapter = new GroupsListAdapter(parent,
					DatabaseManager.getWhereName(DatabaseManager
							.getConnection().getReadableDatabase(), text));
		}
		allGroupsList.setAdapter(adapter);
	}

	public void downloadTimeTable(int code) {
		if (code == 0) {
			TimeTableDownloader tDown = new TimeTableDownloader(parent, selfPointer);
			tDown.execute();
		} else {
//			Log.v("t", "could run downloader - creator returned 0");
		}
	}
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		DownloadManager.setAddGroupFragment(null);
	}
	
	public void onDetach(){
		super.onDetach();
		DownloadManager.setAddGroupFragment(null);
	}
}
