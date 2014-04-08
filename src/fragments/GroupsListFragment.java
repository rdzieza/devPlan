package fragments;

import prefereces.PreferenceHelper;
import adapters.GroupsListAdapter;
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

import database.DatabaseManager;
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
	GroupsListAdapter adapter;

	// Boolean values
	private boolean areSelectedShown;
	private boolean areAllShown;

	// Views
	private ListView selected;
	// Labels
	private TextView selectedLabel;
	private TextView addGroupsLabel;
	// Empty list view
	private TextView noSelectedLabel;
	AddGroupFragment addGroupFragment;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.groups_list_view, containter,
				false);
		// Selected groups section
		selected = (ListView) view.findViewById(R.id.selectedGroupsList);

		selectedLabel = (TextView) view.findViewById(R.id.selectedGroupsLabel);
		// //////////////////////////////////////////////////////////////////////////////////////////////////
		if (parent != null) {
			SelectedCounter selectedCounter = new SelectedCounter(parent,
					selectedLabel);
			selectedCounter.execute();
		}
		// //////////////////////////////////////////////////////////////////////////////////////////////////
		areSelectedShown = false;
		selectedAdapter = new SelectedGroupsAdapter(parent,
				DatabaseManager.getSelectedWithNames(DatabaseManager
						.getConnection().getReadableDatabase()));
		noSelectedLabel = (TextView) view.findViewById(R.id.noSelectedLabel);
		noSelectedLabel.setText(parent
				.getString(R.string.no_selected_groups_text));
		selected.setEmptyView(noSelectedLabel);
		selected.setAdapter(selectedAdapter);
		selected.setVisibility(View.GONE);
		selectedLabel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (areSelectedShown) {
					// Log.v("t", "Ukryj wybrane");
					hideSelected();
				} else {
					// Log.v("t", "Pokaz wybrane");
					showSelected();
				}

			}
		});
		selected.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				DatabaseManager.setAsInactive(id, DatabaseManager
						.getConnection().getWritableDatabase());
				selectedAdapter = new SelectedGroupsAdapter(parent,
						DatabaseManager.getSelectedWithNames(DatabaseManager
								.getConnection().getReadableDatabase()));
				selected.setAdapter(selectedAdapter);
				if (addGroupFragment != null) {
					addGroupFragment.update();
				}
				if (PreferenceHelper.getBoolean("isDatabaseCreated")) {
					SelectedCounter selectedCounter = new SelectedCounter(
							parent, selectedLabel);
					selectedCounter.execute();
				}

			}
		});

		// Add group section

		addGroupsLabel = (TextView) view.findViewById(R.id.addGroupsLabel);
		areAllShown = false;
		addGroupsLabel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (areAllShown) {
					FragmentTransaction trans = getSherlockActivity()
							.getSupportFragmentManager().beginTransaction();
					// Log.v("t", "Ukryj wszystkie");
					trans.remove(addGroupFragment);
					trans.commit();
					areAllShown = false;
				} else {
					FragmentTransaction trans = getSherlockActivity()
							.getSupportFragmentManager().beginTransaction();
					// Log.v("t", "Pokaż wszystkie");
					
					addGroupFragment = new AddGroupFragment();
					trans.replace(R.id.addGroupsFragmentLayout,
							addGroupFragment, "add_group_fragment");
					trans.commit();
					areAllShown = true;
					noSelectedLabel.setVisibility(View.GONE);
					selected.setVisibility(View.GONE);
					areSelectedShown = false;
				}
			}
		});

		return view;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = activity;
		// isAdded = true;
	}

	public void onDetach() {
		super.onDetach();
		parent = null;
		// isAdded = false;
	}

	public void hideSelected() {
		if (areSelectedShown) {
			selected.setVisibility(View.GONE);
			areSelectedShown = false;
		}
		if (noSelectedLabel.getVisibility() == View.VISIBLE) {
			noSelectedLabel.setVisibility(View.GONE);
		}
	}

	public void showSelected() {
		if (!areSelectedShown) {
			selected.setVisibility(View.VISIBLE);
			// areSelectedShown = true;
		}
		if (selected.getAdapter().getCount() == 0) {
			noSelectedLabel.setVisibility(View.VISIBLE);
		}
		areSelectedShown = true;
	}

}
