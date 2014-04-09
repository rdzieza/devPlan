package fragments;

import prefereces.PreferenceHelper;
import adapters.SelectedGroupsAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
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
 *         Shows user lists of groups, those selected and those to be added.
 * 
 */
public class GroupsListFragment extends SherlockFragment {

	private Activity parent;
	private SelectedGroupsAdapter selectedAdapter;
	private boolean areSelectedShown;
	private boolean areAllShown;
	private ListView selectedGroupsListView;
	private TextView selectedLabel;
	private TextView addGroupsLabel;
	private TextView noSelectedLabel;
	private AddGroupFragment addGroupFragment;

	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.groups_list_view, containter,
				false);

		initializeFields(view);
		updateViewsData();
		addAddGroupsLabelListener();

		return view;
	}

	public void initializeFields(View view) {
		selectedGroupsListView = (ListView) view
				.findViewById(R.id.selectedGroupsList);
		selectedLabel = (TextView) view.findViewById(R.id.selectedGroupsLabel);
		areSelectedShown = false;
		noSelectedLabel = (TextView) view.findViewById(R.id.noSelectedLabel);
		addGroupsLabel = (TextView) view.findViewById(R.id.addGroupsLabel);
		areAllShown = false;
	}

	public void updateViewsData() {
		updateSelectedCount();
		updateSelectedListViewDataAndHide();
		addSelectedLabelListener();
		addSelectedListViewListener();
		noSelectedLabel.setText(parent
				.getString(R.string.no_selected_groups_text));

	}

	public void updateSelectedCount() {
		if (parent != null) {
			SelectedCounter selectedCounter = new SelectedCounter(parent,
					selectedLabel);
			selectedCounter.execute();
		}
	}

	public void updateSelectedListViewDataAndHide() {
		updateSelectedListViewData();
		selectedGroupsListView.setVisibility(View.GONE);

	}

	public void addSelectedLabelListener() {
		selectedLabel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (areSelectedShown) {
					hideSelected();
				} else {
					showSelected();
				}

			}
		});
	}

	public void addSelectedListViewListener() {
		selectedGroupsListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long id) {
						boolean result = DatabaseQueryExecutor
								.setGroupAsInactive(DatabaseConnectionManager
										.getConnection().getWritableDatabase(),
										id);
						if (result) {
							updateSelectedListViewData();
							if (addGroupFragment != null) {
								addGroupFragment.updateAllGroupsList();
							}
							if (PreferenceHelper
									.getBoolean("isDatabaseCreated")) {
								updateSelectedCount();
							}
						}
					}
				});
	}

	public void updateSelectedListViewData() {
		selectedAdapter = new SelectedGroupsAdapter(parent,
				DatabaseDataProvider
						.getSelectedGroupsCursor(DatabaseConnectionManager
								.getConnection().getReadableDatabase()));

		selectedGroupsListView.setEmptyView(noSelectedLabel);
		selectedGroupsListView.setAdapter(selectedAdapter);

	}

	public void addAddGroupsLabelListener() {
		addGroupsLabel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentTransaction trans = getSherlockActivity()
						.getSupportFragmentManager().beginTransaction();
				if (areAllShown) {
					trans.remove(addGroupFragment);
					trans.commit();
					areAllShown = false;
				} else {
					showAllGroups(trans);
				}
			}
		});
	}

	public void showAllGroups(FragmentTransaction trans) {
		if (trans != null) {
			addGroupFragment = new AddGroupFragment();
			trans.replace(R.id.addGroupsFragmentLayout, addGroupFragment,
					"add_group_fragment");
			trans.commit();
			areAllShown = true;
			noSelectedLabel.setVisibility(View.GONE);
			selectedGroupsListView.setVisibility(View.GONE);
			areSelectedShown = false;
		}
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = activity;
	}

	public void onDetach() {
		super.onDetach();
		parent = null;
	}

	public void hideSelected() {
		if (areSelectedShown) {
			selectedGroupsListView.setVisibility(View.GONE);
			areSelectedShown = false;
		}
		if (noSelectedLabel.getVisibility() == View.VISIBLE) {
			noSelectedLabel.setVisibility(View.GONE);
		}
	}

	public void showSelected() {
		if (!areSelectedShown) {
			selectedGroupsListView.setVisibility(View.VISIBLE);
		}
		if (selectedGroupsListView.getAdapter().getCount() == 0) {
			noSelectedLabel.setVisibility(View.VISIBLE);
		}
		areSelectedShown = true;
	}

}
