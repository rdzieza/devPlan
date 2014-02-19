package adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import classes.Event;
import classes.Item;
import classes.Separator;
import dev.rd.devplan.R;

public class ActivityAdapter extends ArrayAdapter<Item> {
	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater infl;

	public ActivityAdapter(Context context, ArrayList<Item> list) {
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
			view = new View(context);
		}
		final Item item = items.get(position);
		if (item != null) {
			if (item.isHeaderType()) {
				Separator separator = (Separator) item;
				view = infl.inflate(R.layout.separator, null);
				TextView date = (TextView) view.findViewById(R.id.separator);
				date.setText(separator.getDate());
			} else {
				Event event = (Event) item;
				view = infl.inflate(R.layout.single_event_view_separator, null);
				TextView eventTopic = (TextView) view
						.findViewById(R.id.activityTopic);
				eventTopic.setText(event.getSubject());
				TextView eventType = (TextView) view
						.findViewById(R.id.activityType);
				eventType.setText(event.getType());
				TextView eventDate = (TextView) view
						.findViewById(R.id.startTime);
				eventDate.setText(event.getStartHours());
				TextView eventHours = (TextView) view
						.findViewById(R.id.endTime);
				eventHours.setText(event.getEndHour());
				TextView eventRoom = (TextView) view
						.findViewById(R.id.activityRoom);
				eventRoom.setText(event.getRoom());
			}
		}

		return view;
	}

	
}
