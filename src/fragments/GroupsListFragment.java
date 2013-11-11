package fragments;

import adapters.GroupsListAdapter;
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

import com.actionbarsherlock.app.SherlockFragment;
import com.example.timetable.R;

import database.DatabaseManager;

public class GroupsListFragment extends SherlockFragment implements
		OnItemClickListener {
	private ListView list;
	private Activity parent;
	private EditText filterField;

	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.groups_list_view, containter,
				false);
		filterField = new EditText(parent);
		final GroupsListAdapter adapter = new GroupsListAdapter(parent,
				DatabaseManager.getGroupsCursor(DatabaseManager.getConnection()
						.getReadableDatabase()));
		list = (ListView) view.findViewById(R.id.groupsListView);
		list.setEmptyView(null);
		list.addHeaderView(filterField);
		list.setAdapter(adapter);
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
//				list = (ListView)parent.findViewById(R.id.groupsListView);
//				GroupsListAdapter adapter = (GroupsListAdapter)list.getAdapter();
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
		list.setOnItemClickListener(this);

		return view;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = activity;
		// list.invalidate();
	}

	public void onResume() {
		super.onResume();
		list.invalidate();
	}

	public void update() {
		list.setAdapter(new GroupsListAdapter(parent, DatabaseManager
				.getGroupsCursor(DatabaseManager.getConnection()
						.getReadableDatabase())));
		list.invalidate();
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
		builder.setMessage("Set as active?");
		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.v("t", "agreed: " + String.valueOf(selected));
				DatabaseManager.changeStatus(selected, DatabaseManager
						.getConnection().getReadableDatabase());
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
