package database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import prefereces.PreferenceHelper;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;
import classes.Event;
import classes.Item;
import classes.Separator;
import database.table.ActivitiesTable;
import database.table.GroupsTable;

public class DatabaseDataProvider {

	public static Cursor getUnselectedGroupsCursor(SQLiteDatabase db) {
		try {
			String[] columns = { GroupsTable.NAME_FIELD,
					GroupsTable.ID_FIELD_NAME + " as _id",
					GroupsTable.IS_ACTIVE_FIELD };
			String[] args = { "0" };
			Cursor c = db.query(GroupsTable.TABLE_NAME, columns,
					GroupsTable.IS_ACTIVE_FIELD + " = ?", args, null, null,
					null);
			return c;
		} catch (SQLException e) {
			return null;
		} catch (IllegalStateException e) {
			return null;
		}
	}

	public static Cursor getSelectedGroupsCursor(SQLiteDatabase db) {
		try {
			String[] colums = { GroupsTable.NAME_FIELD,
					GroupsTable.ID_FIELD_NAME + " as _id" };
			String[] args = { "1" };
			Cursor c = db.query(GroupsTable.TABLE_NAME, colums,
					GroupsTable.IS_ACTIVE_FIELD + " = ?", args, null, null,
					GroupsTable.NAME_FIELD);
			c.moveToFirst();
			return c;
		} catch (SQLException e) {
			return null;
		}
	}

	public static int getNumberOfSelectedGroups(SQLiteDatabase db) {
		String[] columns = { GroupsTable.IS_ACTIVE_FIELD };
		String[] args = { "1" };
		Cursor c = db.query(GroupsTable.TABLE_NAME, columns,
				GroupsTable.IS_ACTIVE_FIELD + " = ?", args, null, null, null);
		return c.getCount();
	}

	public static List<NameValuePair> getSelectedAsNameValuePairsList(
			SQLiteDatabase db) {

		String[] columns = { GroupsTable.IS_ACTIVE_FIELD,
				GroupsTable.ID_FIELD_NAME };
		String[] args = { "1" };
		Cursor c = db.query(GroupsTable.TABLE_NAME, columns,
				GroupsTable.IS_ACTIVE_FIELD + " = ?", args, null, null, null);
		List<NameValuePair> params = new ArrayList<NameValuePair>(c.getCount());
		int index = c.getColumnIndex(GroupsTable.ID_FIELD_NAME);
		while (c.moveToNext()) {
			params.add(new BasicNameValuePair("group_id[]", String.valueOf(c
					.getLong(index))));
		}
		return params;
	}

	public static ArrayList<Item> getActivitiesList(SQLiteDatabase db) {
		try {
			String[] columns = { ActivitiesTable.ID_FIELD + " as _id",
					ActivitiesTable.NAME_FIELD,
					ActivitiesTable.PLACE_LOCATION_FIELD,
					ActivitiesTable.START_AT_FIELD,
					ActivitiesTable.END_AT_FIELD,
					ActivitiesTable.CATEGORY_NAME_FIELD,
					ActivitiesTable.DAY_FIELD,
					ActivitiesTable.DAY_OF_WEEK_FIELD,
					ActivitiesTable.TIME_FIELD };
			Cursor cursor = db.query(ActivitiesTable.TABLE_NAME, columns, null,
					null, null, null, ActivitiesTable.TIME_FIELD);
			return transformtToItemList(cursor);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getTodaysDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	public static ArrayList<Item> getActivitiesListByName(SQLiteDatabase db,
			String name) {
		try {
			String[] columns = { ActivitiesTable.ID_FIELD + " as _id",
					ActivitiesTable.NAME_FIELD,
					ActivitiesTable.PLACE_LOCATION_FIELD,
					ActivitiesTable.START_AT_FIELD,
					ActivitiesTable.END_AT_FIELD,
					ActivitiesTable.CATEGORY_NAME_FIELD,
					ActivitiesTable.DAY_FIELD,
					ActivitiesTable.DAY_OF_WEEK_FIELD,
					ActivitiesTable.TIME_FIELD };
			String[] args = { "%" + name + "%" };
			Cursor cursor = db.query("ACTIVITIES", columns, "NAME like ?",
					args, null, null, ActivitiesTable.TIME_FIELD);
			return transformtToItemList(cursor);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	private static ArrayList<Item> transformtToItemList(Cursor cursor) {
		String lastDay = "00-00";
		ArrayList<Item> items = new ArrayList<Item>();
		boolean isDaySet = false;

		final int dayFieldIndex = cursor
				.getColumnIndex(ActivitiesTable.DAY_FIELD);
		final int dayOfWeekFieldIndex = cursor
				.getColumnIndex(ActivitiesTable.DAY_OF_WEEK_FIELD);
		final int nameFieldIndex = cursor
				.getColumnIndex(ActivitiesTable.NAME_FIELD);
		final int startAtIndex = cursor
				.getColumnIndex(ActivitiesTable.START_AT_FIELD);
		final int endAtIndex = cursor
				.getColumnIndex(ActivitiesTable.END_AT_FIELD);
		final int timeFieldIndex = cursor
				.getColumnIndex(ActivitiesTable.TIME_FIELD);
		final int placeLocationFieldIndex = cursor
				.getColumnIndex(ActivitiesTable.PLACE_LOCATION_FIELD);
		final int categoryNameFieldIndex = cursor
				.getColumnIndex(ActivitiesTable.CATEGORY_NAME_FIELD);

		String today = getTodaysDate();

		while (cursor.moveToNext()) {
			String day = cursor.getString(dayFieldIndex);

			if (!lastDay.equals(day)) {
				lastDay = day;
				String weekDay = cursor.getString(dayOfWeekFieldIndex);
				Separator separator = new Separator(day + " " + weekDay);
				items.add(separator);
				if (day.equals(today)) {
					PreferenceHelper.saveInt("todaysPosition", items.size());
					Log.v("t", "position: " + items.size());
					Log.v("t", "today is: " + today);
					isDaySet = true;
				}

			}
			Event event = new Event(
					cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getString(nameFieldIndex),
					cursor.getString(startAtIndex),
					cursor.getString(endAtIndex),
					cursor.getLong(timeFieldIndex),
					cursor.getString(placeLocationFieldIndex),
					cursor.getString(categoryNameFieldIndex));
			items.add(event);
		}

		if (!isDaySet) {
			getNextDayIndex(today, items);
		}
		return items;
	}

	private static String findNextDay(String today) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(today));
			c.add(Calendar.DATE, 1);
			String tomorow = sdf.format(c.getTime());
			return tomorow;
		} catch (ParseException e) {
			return null;
		}

	}
	
	private static boolean findDatesIndex(ArrayList<Item> items, String date) {
			for(int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
				if(item instanceof Separator) {
					Separator separator = (Separator)item;
					String day = separator.getDate().substring(0, 10);
//					Log.v("t", day);
					if (date.equals(day)) {
						PreferenceHelper.saveInt("todaysPosition", i+1);
						Log.v("t", "position: " + i);
						return true;
					}
				}
			}
			return false;
	}
	
	private static void getNextDayIndex(String today, ArrayList<Item> items) {
		boolean isFound = false;
		String day = today;
		while(!isFound) {
			String tomorow = findNextDay(day);
			day = tomorow;
			isFound = findDatesIndex(items, tomorow);
		}
	}
}
