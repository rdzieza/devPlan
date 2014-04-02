package fragments;

import java.util.ArrayList;

import network.GroupsDownloader;
import network.VersionChecker;
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
import classes.Option;
import classes.SelectedCounter;

import com.actionbarsherlock.app.SherlockFragment;

import dev.rd.devplan.R;

/**
 * 
 * @author Robert Dzieża
 * 
 *         Fragment responsible for option list representation.
 * 
 */
public class MoreOptionsFragment extends SherlockFragment implements
		OnItemClickListener {
	private ListView list;
	private MainView parent;
//	private MoreOptionsFragment self;

	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.options_list_view, containter,
				false);
		list = (ListView) view.findViewById(R.id.optionsList);
//		self = this;
		ArrayList<Option> items = new ArrayList<Option>();

		items.add(new Option(R.drawable.ic_web, parent
				.getString(R.string.option_homepage)));
		items.add(new Option(R.drawable.ic_check, parent
				.getString(R.string.option_check_update)));
		items.add(new Option(R.drawable.ic_groups, parent
				.getString(R.string.option_update_groups)));
		items.add(new Option(R.drawable.ic_error, parent
				.getString(R.string.option_report_error)));
		items.add(new Option(R.drawable.ic_info, parent
				.getString(R.string.option_info)));
		list.setAdapter(new OptionsListAdapter(parent, items));
		list.setOnItemClickListener(this);
		return view;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.parent = (MainView) activity;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long arg3) {
		switch (position) {
		case 0: {
			String url = getString(R.string.home_page);
			openBrowser(url);
		}
			break;
		case 1: {
			// Toast.makeText(this.parent, "Checkin for update",
			// Toast.LENGTH_LONG)
			// .show();
			VersionChecker versionChecker = new VersionChecker(this.parent);
			versionChecker.execute();
		}
			break;
		case 2: {
			GroupsDownloader groupDown = new GroupsDownloader(this.getActivity());
			groupDown.execute();
		}
			break;
		case 3: {
			Intent intent = new Intent(parent.getContext(),
					ReportErrorActivity.class);
			startActivity(intent);
		}
			break;
		case 4: {
			Intent intent = new Intent(parent.getContext(), InfoActivity.class);
			startActivity(intent);
		}
			break;
		}

	}

	public void openBrowser(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}
	
	public void updateGroupsInformation(){
		SelectedCounter selectedCounter = new SelectedCounter(parent);
		selectedCounter.execute();
	}
}
