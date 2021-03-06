package classes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivitiesStack {
	private static List<Activity> activities;
	
	public static void add(Activity activity){
		if(activities == null){
			activities = new ArrayList<Activity>();
		}
		activities.add(activity);
	}
	
	public static void close(){
		for(Activity a : activities){
			if(a != null){
				a.finish();
			}
		}
	}
}
