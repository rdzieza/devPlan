package classes;

import prefereces.PreferenceHelper;
import adapters.ActivityAdapter;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import database.DatabaseConnectionManager;
import database.DatabaseDataProvider;
import dev.rd.devplan.R;

public class TimeTableUiUpdator implements UiUpdator {
	private Context context;

	public TimeTableUiUpdator(Context context) {
		this.context = context;
	}

	@Override
	public void updateUI() {
		Activity activity = (Activity) context;
		hideProgressBar(activity);
		refreshTimeTableListViewContent(activity);
	}

	public void hideProgressBar(Activity activity) {
		ProgressBar loadingBar = (ProgressBar) activity
				.findViewById(R.id.loadingTimeTableBar);
		TextView label = (TextView) activity
				.findViewById(R.id.loadingTimeTableText);

		if (loadingBar != null && label != null) {
			loadingBar.setVisibility(View.GONE);
			label.setVisibility(View.GONE);
		}

	}

	public void refreshTimeTableListViewContent(Activity activity) {
		Log.v("t", "refreshTimeTableViewContent()");
		ListView list = (ListView) activity
				.findViewById(R.id.timeTableListView);
		if (list != null) {
			ActivityAdapter adapter = new ActivityAdapter(activity,
					DatabaseDataProvider
							.getActivitiesList(DatabaseConnectionManager
									.getConnection().getReadableDatabase()));
			list.setAdapter(adapter);
			list.setSelection(PreferenceHelper.getInt("todaysPosition")-1);
		}
	}

}
