package adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import classes.Option;
import dev.rd.devplan.R;

public class OptionsListAdapter extends ArrayAdapter<Option>{
	private Context context;
	private ArrayList<Option> items;
	private LayoutInflater infl;
	
	public OptionsListAdapter(Context context, ArrayList<Option> list) {
		super(context, 0, list);
		this.context = context;
		this.items = list;
		infl = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = infl.inflate(R.layout.single_option_view, null);
		}
		ImageView icon = (ImageView)view.findViewById(R.id.optionIcon);
		TextView title = (TextView)view.findViewById(R.id.optionTitle);
		Option option = items.get(position);
		icon.setImageResource(option.icon);
		title.setText(option.title);
		

		return view;
	}
	


}
