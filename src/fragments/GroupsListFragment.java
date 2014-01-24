package fragments;

import knp.rd.timetable.R;
import adapters.GroupsListAdapter;
import adapters.SelectedGroupsAdapter;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import database.DatabaseManager;

public class GroupsListFragment extends SherlockFragment implements
		OnItemClickListener {
	private ListView list;
	private Activity parent;
	private EditText filterField;
	GroupsListAdapter adapter;
	private boolean areSelectedShown;
	private boolean areAllShown;
	ListView selected;

	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.groups_list_view, containter,
				false);
		filterField = new EditText(parent);
		filterField.setHint("Wprowadz nazwe grupy");
		filterField.setSingleLine(true);
		filterField.setSingleLine();
		// ///////////////////////////
		String text = filterField.getText().toString();
		if (text == null || text.equals("")) {
			adapter = new GroupsListAdapter(parent,
					DatabaseManager.getGroupsCursor(DatabaseManager
							.getConnection().getReadableDatabase()));
		} else {
			adapter = new GroupsListAdapter(parent, DatabaseManager.getWhereName(DatabaseManager
					.getConnection().getReadableDatabase(), text));
		}
		list = (ListView) view.findViewById(R.id.groupsListView);
		list.setEmptyView(null);
		list.addHeaderView(filterField);
		list.setAdapter(adapter);
		fixFilter();
		list.setOnItemClickListener(this);
		// ///////////////////////////////////////////////////////////////////////////////////////
		selected = (ListView) view.findViewById(R.id.selectedGroupsList);
		TextView selectedLabel = (TextView) view
				.findViewById(R.id.selectedGroupsLabel);
		TextView groupsHeader = (TextView) view
				.findViewById(R.id.addGroupsLabel);
		SelectedGroupsAdapter selectedAdapter = new SelectedGroupsAdapter(
				parent, DatabaseManager.getSelectedWithNames(DatabaseManager
						.getConnection().getReadableDatabase()));
		TextView noSelectedLabel = (TextView) view
				.findViewById(R.id.noSelectedLabel);
		// noSelectedLabel.setText("Brak wybranych grup");
		selected.setEmptyView(noSelectedLabel);
		selected.setAdapter(selectedAdapter);
		selected.setVisibility(View.GONE);
		list.setVisibility(View.GONE);
		areAllShown = false;
		areSelectedShown = false;
		selectedLabel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (areSelectedShown) {
					selected.setVisibility(View.GONE);
					areSelectedShown = false;
				} else {
					selected.setVisibility(View.VISIBLE);
					areSelectedShown = true;
				}

			}
		});
		groupsHeader.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (areAllShown) {
					list.setVisibility(View.GONE);
					areAllShown = false;
				} else {
					selected.setVisibility(View.GONE);
					areSelectedShown = false;
					list.setVisibility(View.VISIBLE);
					areAllShown = true;
				}

			}
		});
		selected.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View view, int position,
					long id) {
				DatabaseManager.changeStatus(id, DatabaseManager
						.getConnection().getReadableDatabase());
				SelectedGroupsAdapter selectedAdapter = new SelectedGroupsAdapter(
						parent, DatabaseManager
								.getSelectedWithNames(DatabaseManager
										.getConnection().getReadableDatabase()));
				selected.setAdapter(selectedAdapter);
			}
		});
		return view;
	}

	public void fixFilter() {
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
				// list = (ListView)parent.findViewById(R.id.groupsListView);
				// GroupsListAdapter adapter =
				// (GroupsListAdapter)list.getAdapter();
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

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = activity;
		// list.invalidate();
	}

	public void onResume() {
		super.onResume();
		// list.invalidate();
		// String text = filterField.getText().toString();
		// if (text == null || text.equals("")) {
		// adapter = new GroupsListAdapter(parent,
		// DatabaseManager.getGroupsCursor(DatabaseManager
		// .getConnection().getReadableDatabase()));
		// list.setAdapter(adapter);
		// } else {
		// adapter.getFilter().filter(text);
		// }
		// fixFilter();

	}

	public void update() {
		// adapter.notifyDataSetChanged();
//		adapter = new GroupsListAdapter(parent,
//				DatabaseManager.getGroupsCursor(DatabaseManager.getConnection()
//						.getReadableDatabase()));
		String text = filterField.getText().toString();
		if (text == null || text.equals("")) {
			adapter = new GroupsListAdapter(parent,
					DatabaseManager.getGroupsCursor(DatabaseManager
							.getConnection().getReadableDatabase()));
		} else {
			adapter = new GroupsListAdapter(parent, DatabaseManager.getWhereName(DatabaseManager
					.getConnection().getReadableDatabase(), text));
		}
		list.setAdapter(adapter);
		fixFilter();
		// list.setAdapter(new GroupsListAdapter(parent, DatabaseManager
		// .getGroupsCursor(DatabaseManager.getConnection()
		// .getReadableDatabase())));
		SelectedGroupsAdapter selectedAdapter = new SelectedGroupsAdapter(
				parent, DatabaseManager.getSelectedWithNames(DatabaseManager
						.getConnection().getReadableDatabase()));
		selected.setAdapter(selectedAdapter);
		selected.setVisibility(View.GONE);
		areSelectedShown = false;

	}

	public void onDetach() {
		super.onDetach();

	}

	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final long selected = id;
		Builder builder = new Builder(this.parent);
		builder.setMessage("Change status?");
		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.v("t", "agreed: " + String.valueOf(selected));
				DatabaseManager.changeStatus(selected, DatabaseManager
						.getConnection().getReadableDatabase());
				// adapter.notifyDataSetChanged();

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
