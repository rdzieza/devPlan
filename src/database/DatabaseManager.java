package database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import classes.Event;
import classes.Item;
import classes.Separator;

public class DatabaseManager extends SQLiteOpenHelper {
	private static Context context;
	private static final String DB_NAME = "TIME_TABLE.db";
	private static final int VERSION = 3;
	private static DatabaseManager instance;
	private final static String CREATE_ACTIVITIES = "CREATE TABLE IF NOT EXISTS ACTIVITIES("
			+ "ID LONG PRIMARY KEY,"
			+ "GROUP_NAME VARCHAR(60),"
			+ "GROUP_ID LONG,"
			+ "TUTOR_ID LONG,"
			+ "TUTOR_NAME VARCHAR(60),"
			+ "TUTOR_URL VARCHAR(60),"
			+ "PLACE_ID LONG,"
			+ "PLACE_LOCATION VARCHAR(30),"
			+ "CATEGORY_NAME VARCHAR(30),"
			+ "NAME VARCHAR(60),"
			+ "NOTES VARCHAR(60),"
			+ "STATE INT,"
			+ "START_AT VARCHAR(20),"
			+ "END_AT  VARCHAR(20),"
			+ "DAY  VARCHAR(20)," + "DAY_OF_WEEK VARCHAR(20)," + "TIME LONG)";

	private static final String CREATE_GROUPS = "CREATE TABLE IF NOT EXISTS GROUPS("
			+ "ID LONG PRIMARY KEY,"
			+ "NAME VARCHAR(30) NOT NULL,"
			+ "IS_ACTIVE INT NOT NULL DEFAULT 0)";
	private static final String CREATE_SELECTED = "CREATE TABLE IF NOT EXISTS SELECTED ("
			+ "ID LONG PRIMARY KEY)";

	// private static final Strning CREATE_INDEX = ""

	private DatabaseManager() {
		super(context, DB_NAME, null, VERSION);
	}

	public static void initialize(Context context) {
		DatabaseManager.context = context;
		getConnection().onCreate(getConnection().getWritableDatabase());
	}

	public static synchronized DatabaseManager getConnection() {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL("DROP TABLE IF EXISTS ACTIVITIES");
		// db.execSQL("DROP TABLE IF EXISTS GROUPS");
		db.execSQL(CREATE_GROUPS);
		db.execSQL(CREATE_ACTIVITIES);
		db.execSQL(CREATE_SELECTED);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public static Cursor getGroupsCursor(SQLiteDatabase db) {
		try {
			String[] columns = { "NAME", "ID as _id", "IS_ACTIVE" };
			Cursor c = db.query("GROUPS", columns, null, null, null, null,
					"NAME");
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}

	public static ArrayList<Item> getEventsListFromRange(SQLiteDatabase db, String from, String to) {
		try {
			String[] columns = { "ID as _id", "NAME", "PLACE_LOCATION",
					"START_AT", "END_AT", "CATEGORY_NAME", "DAY",
					"DAY_OF_WEEK", "TIME" };
			String[] args = { from, to};
			Cursor cursor = db.query("ACTIVITIES", columns, "DAY BETWEEN ? AND ?", args,
					null, null, "TIME");

			String lastDay = "00-00";
			ArrayList<Item> items = new ArrayList<Item>();
			Log.v("t", String.valueOf(cursor.getCount()));
			while (cursor.moveToNext()) {
				String day = cursor.getString(cursor.getColumnIndex("DAY"));
				if (!lastDay.equals(day)) {
					lastDay = day;
					String weekDay = cursor.getString(cursor
							.getColumnIndex("DAY_OF_WEEK"));
					Separator separator = new Separator(day + " " + weekDay);
					items.add(separator);
				}
				Event event = new Event(
						cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getString(cursor.getColumnIndex("NAME")),
						cursor.getString(cursor.getColumnIndex("START_AT")),
						cursor.getString(cursor.getColumnIndex("END_AT")),
						cursor.getLong(cursor.getColumnIndex("TIME")),
						cursor.getString(cursor
								.getColumnIndex("PLACE_LOCATION")),
						cursor.getString(cursor.getColumnIndex("CATEGORY_NAME")));
				items.add(event);
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}
	
	public static ArrayList<Item> getEventsListSincetToday(SQLiteDatabase db) {
		try {
			String[] columns = { "ID as _id", "NAME", "PLACE_LOCATION",
					"START_AT", "END_AT", "CATEGORY_NAME", "DAY",
					"DAY_OF_WEEK", "TIME" };
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date today = new Date();
			String[] args = { sdf.format(today) };
			Cursor cursor = db.query("ACTIVITIES", columns, "DAY >= ?", args,
					null, null, "TIME");

			String lastDay = "00-00";
			ArrayList<Item> items = new ArrayList<Item>();
			Log.v("t", String.valueOf(cursor.getCount()));
			while (cursor.moveToNext()) {
				String day = cursor.getString(cursor.getColumnIndex("DAY"));
				if (!lastDay.equals(day)) {
					lastDay = day;
					String weekDay = cursor.getString(cursor
							.getColumnIndex("DAY_OF_WEEK"));
					Separator separator = new Separator(day + " " + weekDay);
					items.add(separator);
				}
				Event event = new Event(
						cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getString(cursor.getColumnIndex("NAME")),
						cursor.getString(cursor.getColumnIndex("START_AT")),
						cursor.getString(cursor.getColumnIndex("END_AT")),
						cursor.getLong(cursor.getColumnIndex("TIME")),
						cursor.getString(cursor
								.getColumnIndex("PLACE_LOCATION")),
						cursor.getString(cursor.getColumnIndex("CATEGORY_NAME")));
				items.add(event);
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}
	
	public static ArrayList<Item> getEventsList(SQLiteDatabase db) {
		try {
			String[] columns = { "ID as _id", "NAME", "PLACE_LOCATION",
					"START_AT", "END_AT", "CATEGORY_NAME", "DAY",
					"DAY_OF_WEEK", "TIME" };
			Cursor cursor = db.query("ACTIVITIES", columns, null, null,
					null, null, "TIME");

			String lastDay = "00-00";
			ArrayList<Item> items = new ArrayList<Item>();
			Log.v("t", String.valueOf(cursor.getCount()));
			while (cursor.moveToNext()) {
				String day = cursor.getString(cursor.getColumnIndex("DAY"));
				if (!lastDay.equals(day)) {
					lastDay = day;
					String weekDay = cursor.getString(cursor
							.getColumnIndex("DAY_OF_WEEK"));
					Separator separator = new Separator(day + " " + weekDay);
					items.add(separator);
				}
				Event event = new Event(
						cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getString(cursor.getColumnIndex("NAME")),
						cursor.getString(cursor.getColumnIndex("START_AT")),
						cursor.getString(cursor.getColumnIndex("END_AT")),
						cursor.getLong(cursor.getColumnIndex("TIME")),
						cursor.getString(cursor
								.getColumnIndex("PLACE_LOCATION")),
						cursor.getString(cursor.getColumnIndex("CATEGORY_NAME")));
				items.add(event);
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}

	public static ArrayList<Item> getEventsListByDay(SQLiteDatabase db,
			String dayFilter) {
		try {
			String[] columns = { "ID as _id", "NAME", "PLACE_LOCATION",
					"START_AT", "END_AT", "CATEGORY_NAME", "DAY",
					"DAY_OF_WEEK", "TIME" };
			String[] args = { dayFilter };
			Cursor cursor = db.query("ACTIVITIES", columns, "DAY = ?", args,
					null, null, "TIME");

			ArrayList<Item> items = new ArrayList<Item>();
			Log.v("t", String.valueOf(cursor.getCount()));
			int counter = 0;
			while (cursor.moveToNext()) {
				if (counter == 0) {
					String day = cursor.getString(cursor.getColumnIndex("DAY"));
					String weekDay = cursor.getString(cursor
							.getColumnIndex("DAY_OF_WEEK"));
					Separator separator = new Separator(day + " " + weekDay);
					items.add(separator);
				}
				Event event = new Event(
						cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getString(cursor.getColumnIndex("NAME")),
						cursor.getString(cursor.getColumnIndex("START_AT")),
						cursor.getString(cursor.getColumnIndex("END_AT")),
						cursor.getLong(cursor.getColumnIndex("TIME")),
						cursor.getString(cursor
								.getColumnIndex("PLACE_LOCATION")),
						cursor.getString(cursor.getColumnIndex("CATEGORY_NAME")));
				items.add(event);
				counter++;
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}

	public static ArrayList<Item> getEventsListByName(SQLiteDatabase db,
			String name) {
		try {
			String[] columns = { "ID as _id", "NAME", "PLACE_LOCATION",
					"START_AT", "END_AT", "CATEGORY_NAME", "DAY",
					"DAY_OF_WEEK", "TIME" };
			String[] args = { "%" + name + "%" };
			Cursor cursor = db.query("ACTIVITIES", columns, "NAME like ?",
					args, null, null, "TIME");

			String lastDay = "00-00";
			ArrayList<Item> items = new ArrayList<Item>();
			Log.v("t", String.valueOf(cursor.getCount()));
			while (cursor.moveToNext()) {
				String day = cursor.getString(cursor.getColumnIndex("DAY"));
				if (!lastDay.equals(day)) {
					lastDay = day;
					String weekDay = cursor.getString(cursor
							.getColumnIndex("DAY_OF_WEEK"));
					Separator separator = new Separator(day + " " + weekDay);
					items.add(separator);
				}
				Event event = new Event(
						cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getString(cursor.getColumnIndex("NAME")),
						cursor.getString(cursor.getColumnIndex("START_AT")),
						cursor.getString(cursor.getColumnIndex("END_AT")),
						cursor.getLong(cursor.getColumnIndex("TIME")),
						cursor.getString(cursor
								.getColumnIndex("PLACE_LOCATION")),
						cursor.getString(cursor.getColumnIndex("CATEGORY_NAME")));
				items.add(event);
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}

	public static Cursor getEventDetails(SQLiteDatabase db, long id) {
		try {
			String[] columns = { "ID as _id", "NAME", "PLACE_LOCATION",
					"START_AT", "END_AT", "CATEGORY_NAME", "DAY",
					"DAY_OF_WEEK", "TUTOR_NAME", "TUTOR_URL" };
			String[] args = { String.valueOf(id) };
			Cursor cursor = db.query("ACTIVITIES", columns, "ID = ?", args,
					null, null, null);
			return cursor;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Cursor getEventsCursor(SQLiteDatabase db) {
		try {
			Long time = new Date().getTime();
			String[] args = { String.valueOf(time) };
			String[] columns = { "ID as _id", "NAME", "PLACE_LOCATION",
					"START_AT", "END_AT", "CATEGORY_NAME", "DAY",
					"DAY_OF_WEEK", "TIME" };
			Cursor c = db.query("ACTIVITIES", columns, null, null, null, null,
					"TIME");
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
	}

	public static void removeTimeTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS ACTIVITIES");
		db.execSQL(CREATE_ACTIVITIES);
	}

	public static boolean addActivity(SQLiteDatabase db, long id, long groupId,
			String groupName, long tutorId, String tutorName, String tutorUrl,
			long placeId, String placeLocation, String categoryName,
			String notes, String name, int state, String day, String dayOfWeek,
			String startAt, String endAt, long time) {
		try {
			ContentValues values = new ContentValues();
			values.put("ID", id);
			values.put("GROUP_ID", groupId);
			values.put("GROUP_NAME", groupName);
			values.put("TUTOR_ID", tutorId);
			values.put("TUTOR_NAME", tutorName);
			values.put("TUTOR_URL", tutorUrl);
			values.put("PLACE_ID", placeId);
			values.put("PLACE_LOCATION", placeLocation);
			values.put("CATEGORY_NAME", categoryName);
			values.put("NAME", name);
			values.put("START_AT", startAt);
			values.put("END_AT", endAt);
			values.put("DAY", day);
			values.put("NOTES", notes);
			values.put("DAY_OF_WEEK", dayOfWeek);
			values.put("TIME", time);
			db.insert("ACTIVITIES", null, values);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void changeStatus(long id, SQLiteDatabase db) {
		// Log.v("t", "status change, id: " + String.valueOf(id));
		String[] columns = { "IS_ACTIVE", "ID" };
		String[] args = { String.valueOf(id) };
		Cursor c = db.query("GROUPS", columns, " ID = ?", args, null, null,
				null);
		c.moveToNext();
		int isActive = c.getInt(c.getColumnIndex("IS_ACTIVE"));
		if (isActive == 1) {
			Log.v("t", "setting as inactive, id: " + String.valueOf(id));
			DatabaseManager.setAsInactive(id, instance.getWritableDatabase());
		} else {
			Log.v("t", "setting as active, id: " + String.valueOf(id));
			DatabaseManager.setAsActive(id, instance.getWritableDatabase());
		}
	}

	public static void setAsInactive(long id, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("IS_ACTIVE", 0);
		String[] args = { String.valueOf(id) };
		db.update("GROUPS", values, " ID = ?", args);
		removeFromSelected(id, db);
	}

	public static void setAsActive(long id, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("IS_ACTIVE", 1);
		String[] args = { String.valueOf(id) };
		db.update("GROUPS", values, " ID = ?", args);
		addToSelected(id, db);
	}

	public static boolean insertGroup(int id, String name) {
		SQLiteDatabase db = instance.getWritableDatabase();
		try {
			String query = "INSERT INTO GROUPS (ID, NAME, IS_ACTIVE) VALUES ("
					+ String.valueOf(id) + ", '" + name + "', 0)";
			db.execSQL(query);
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			// db.close();
		}
	}
	
	public static void addAllGroups(org.json.JSONArray groups, SQLiteDatabase db) throws JSONException {
		//SQLiteDatabase db = instance.getWritableDatabase();
		//db.beginTransaction();
		for (int i = 0; i < groups.length(); i++) {
			JSONObject group = groups.getJSONObject(i);
			String query = "INSERT INTO GROUPS (ID, NAME, IS_ACTIVE) VALUES ('"
					+ String.valueOf(group.getInt("id")) + "','"
					+ group.getString("name") + "', 0)";
			db.execSQL(query);
		}
		//db.setTransactionSuccessful();
		//db.endTransaction();
		
	}
	
	public static void removeGroups(){
		SQLiteDatabase db = instance.getWritableDatabase();
		db.beginTransaction();
		db.execSQL("DELETE FROM GROUPS");
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	public static void addToSelected(long id, SQLiteDatabase db) {
		try {
			db.execSQL("insert into selected values (" + String.valueOf(id)
					+ ")");
		} catch (SQLException e) {

		}
	}

	public static void removeFromSelected(long id, SQLiteDatabase db) {
		String[] args = { String.valueOf(id) };
		try {
			db.delete("selected", "id = ?", args);
		} catch (SQLException e) {

		}
	}

	public static Cursor getSelected(SQLiteDatabase db) {
		try {
			String[] columns = { "ID" };
			Cursor c = db.query("SELECTED", columns, null, null, null, null,
					null);
			return c;
		} catch (SQLException e) {
			return null;
		}
	}

	public static Cursor getWhereName(SQLiteDatabase db, String name) {
		try {
			String[] colums = { "NAME", "ID as _id", "IS_ACTIVE" };
			String[] args = { "%" + name + "%" };
			Cursor c = db.query("GROUPS", colums, "NAME like ?", args, null,
					null, "NAME");
			c.moveToFirst();
			return c;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static Cursor getSelectedWithNames(SQLiteDatabase db){
		try {
			String[] colums = { "NAME", "ID as _id" };
			String[] args = { "1" };
			Cursor c = db.query("GROUPS", colums, "IS_ACTIVE = ?", args, null,
					null, "NAME");
			c.moveToFirst();
			return c;
		} catch (SQLException e) {
			return null;
		}
	}
	

}
