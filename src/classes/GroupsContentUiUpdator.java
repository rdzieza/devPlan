package classes;

import activities.MainView;
import adapters.GroupsListAdapter;
import adapters.SelectedGroupsAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import database.DatabaseConnectionManager;
import database.DatabaseDataProvider;
import dev.rd.devplan.R;
import fragments.AddGroupFragment;
import fragments.GroupsListFragment;

public class GroupsContentUiUpdator implements UiUpdator {
	private Context context;

	public GroupsContentUiUpdator(Context context) {
		this.context = context;
	}

	@Override
	public void updateUI() {
		Log.v("t", "GroupsContentUiUpdator - updateUI()");
		final Activity activity = (Activity) context;

		new Handler(Looper.getMainLooper()).post(new Runnable() {
			public void run() {
				updateSelectedListViewContent(activity);
				hideProgressBar(activity);
				updateUnselectedGroupsList();
			}
		});

	}


	private void updateSelectedListViewContent(Activity activity) {
		ListView selected = (ListView) activity
				.findViewById(R.id.selectedGroupsList);
		if (selected != null) {
			selected.setAdapter(new SelectedGroupsAdapter(activity,
					DatabaseDataProvider
							.getSelectedGroupsCursor(DatabaseConnectionManager
									.getConnection().getReadableDatabase())));
		}
	}

	private void hideProgressBar(Activity activity) {
		ProgressBar groupsBar = (ProgressBar) activity
				.findViewById(R.id.loadingGroupsBar);
		if (groupsBar != null) {
			groupsBar.setVisibility(View.GONE);
		}
		TextView downloadingLabel = (TextView) activity
				.findViewById(R.id.loadingGroupText);
		if (downloadingLabel != null) {
			downloadingLabel.setVisibility(View.GONE);
		}
	}

	private void updateUnselectedGroupsList() {
		try {
			Activity activity = ActivitiesStack.getFromTop();
			if (activity instanceof MainView) {
				MainView main = (MainView) activity;
				AddGroupFragment addGroupFragment = (AddGroupFragment) main
						.getSupportFragmentManager().findFragmentByTag(
								"add_group_fragment");
				if (addGroupFragment != null) {
					addGroupFragment.updateAllGroupsList();
				}
			}
		} catch (EmptyListException e) {

		}
	}

}
