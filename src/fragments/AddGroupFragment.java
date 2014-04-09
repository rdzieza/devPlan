package fragments;

import network.TimeTableCreator;
import network.TimeTableDownloader;
import prefereces.PreferenceHelper;
import adapters.GroupsListAdapter;
import adapters.SelectedGroupsAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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
import classes.SelectedCounter;

import com.actionbarsherlock.app.SherlockFragment;

import database.DatabaseConnectionManager;
import database.DatabaseDataProvider;
import database.DatabaseQueryExecutor;
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
	private Button downloadButton;
	private EditText filterField;
	private AddGroupFragment selfPointer;
	private Handler handler;

	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_group_fragment_view,
				containter, false);

		initializeFields(view);
		addFilterFieldListener();
		setProgressBarVisibility(view);
		updateAllGroupsList();
		addAllGroupsListListener();
		addDownloadButtonListener();
		DownloadManager.setAddGroupFragment(selfPointer);

		return view;
	}

	public void initializeFields(View view) {
		allGroupsList = (ListView) view.findViewById(R.id.groupsListView);
		downloadButton = (Button) view.findViewById(R.id.updateTimeTableButton);
		filterField = (EditText) view.findViewById(R.id.filterField);
		handler = new Handler();
	}

	public void addDownloadButtonListener() {
		downloadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(parent,
						parent.getString(R.string.download_started),
						Toast.LENGTH_SHORT).show();
				TimeTableCreator tDown = new TimeTableCreator(getActivity());
				tDown.execute();

			}
		});
	}

	public void addFilterFieldListener() {
		filterField.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					hideSelected();
				}
			}
		});
	}

	public void hideSelected() {
		hideSelectedListView();
		hideNoSelectedLabel();
	}

	public void hideSelectedListView() {
		ListView selected = (ListView) parent
				.findViewById(R.id.selectedGroupsList);
		if (selected != null) {
			selected.setVisibility(View.GONE);
		}
	}

	public void hideNoSelectedLabel() {
		TextView noSelectedLabel = (TextView) parent
				.findViewById(R.id.noSelectedLabel);
		if (noSelectedLabel != null) {
			noSelectedLabel.setVisibility(View.GONE);
		}
	}

	public void addAllGroupsListListener() {
		allGroupsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				boolean result = DatabaseQueryExecutor.setGroupAsActive(
						DatabaseConnectionManager.getConnection()
								.getWritableDatabase(), id);
				if (result) {
					updateBothListsData();
				}
			}
		});
	}

	public void updateBothListsData() {
		setUnselectedGroupsListAdapter();
		setAndHideSelectedListViewAdapter();
		SelectedCounter selectedCounter = new SelectedCounter(parent);
		selectedCounter.execute();
	}

	public void setAndHideSelectedListViewAdapter() {
		ListView selected = (ListView) parent
				.findViewById(R.id.selectedGroupsList);

		SelectedGroupsAdapter selectedAdapter = new SelectedGroupsAdapter(
				parent,
				DatabaseDataProvider
						.getSelectedGroupsCursor(DatabaseConnectionManager
								.getConnection().getReadableDatabase()));
		selected.setAdapter(selectedAdapter);
		selected.setVisibility(View.GONE);
	}

	public void setProgressBarVisibility(View view) {
		if (DownloadManager.isDowloadingGroups()) {
			ProgressBar groupsBar = (ProgressBar) view
					.findViewById(R.id.loadingGroupsBar);
			groupsBar.setVisibility(View.VISIBLE);
			TextView downloadingLabel = (TextView) view
					.findViewById(R.id.loadingGroupText);
			downloadingLabel.setVisibility(View.VISIBLE);
		}
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = activity;
	}

	public void fixFilter() {
		Log.v("t", "fixi filter from adf");
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
				PreferenceHelper.saveString("filterString", s.toString());

			}
		});

		adapter.setFilterQueryProvider(new FilterQueryProvider() {

			@Override
			public Cursor runQuery(CharSequence constraint) {
				Cursor c = DatabaseDataProvider.getGroupsWithNameContaining(
						DatabaseConnectionManager.getConnection()
								.getReadableDatabase(), constraint.toString());
				return c;
			}
		});
	}

	public void setUnselectedGroupsListAdapter() {
		String text = filterField.getText().toString();

		if (text == null || text.equals("")) {
			text = PreferenceHelper.getString("filterString");
			if (text.equals("brak")) {
				adapter = new GroupsListAdapter(
						parent,
						DatabaseDataProvider
								.getUnselectedGroupsCursor(DatabaseConnectionManager
										.getConnection().getReadableDatabase()));
			} else {
				filterField.setText(text);
				adapter = new GroupsListAdapter(parent,
						DatabaseDataProvider.getGroupsWithNameContaining(
								DatabaseConnectionManager.getConnection()
										.getReadableDatabase(), text));
			}
		} else {
			adapter = new GroupsListAdapter(parent,
					DatabaseDataProvider.getGroupsWithNameContaining(
							DatabaseConnectionManager.getConnection()
									.getReadableDatabase(), text));
		}
		allGroupsList.setAdapter(adapter);
	}

	public void downloadTimeTable(int code) {
		if (code == 0) {
			TimeTableDownloader tDown = new TimeTableDownloader(parent);
			tDown.execute();
		} else {
			// Log.v("t", "could run downloader - creator returned 0");
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		DownloadManager.setAddGroupFragment(null);
	}

	public void onDetach() {
		super.onDetach();
		DownloadManager.setAddGroupFragment(null);
	}

	public void updateAllGroupsList() {
		handler.post(new AdapterUpdator());
	}

	private class AdapterUpdator implements Runnable {

		@Override
		public void run() {
			setUnselectedGroupsListAdapter();
			fixFilter();

		}

	}
}
