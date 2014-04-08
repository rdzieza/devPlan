/**
 * 
 */
package classes;

import prefereces.PreferenceHelper;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import database.DatabaseManager;
import dev.rd.devplan.R;

/**
 * @author robert
 * 
 */
public class SelectedCounter extends AsyncTask<Void, Void, String> {

	TextView label;
	private Activity parent;

	public SelectedCounter(Activity parent) {
		this.parent = parent;
	}


	public SelectedCounter(Activity parent, TextView label) {
		this.parent = parent;
		this.label = label;
	}

	@Override
	protected String doInBackground(Void... params) {
		String text = "";
		if (PreferenceHelper.getBoolean("isDatabaseCreated")) {
			if (DownloadManager.isDowloadingGroups()) {
				this.cancel(true);
			}
			int numberOfSelected = DatabaseManager
					.getSelectedCount(DatabaseManager.getConnection()
							.getReadableDatabase());
			if (numberOfSelected == -1) {
				text = "0";
			}
			text = parent.getResources().getString(
					R.string.selected_groups_label)
					+ " (" + String.valueOf(numberOfSelected) + ")";
			return text;
		} else {
			return parent.getResources().getString(
					R.string.selected_groups_label)
					+ " (0)";
		}
	}

	@Override
	protected void onPostExecute(String text) {
		if (!isCancelled()) {
			if (label != null) {
				label.setText(text);
			} else {
				if (parent != null) {
					TextView selectedLabel = (TextView) parent
							.findViewById(R.id.selectedGroupsLabel);
					if (selectedLabel != null) {
						selectedLabel.setText(text);
					}
				}
			}
		}

	}

}
