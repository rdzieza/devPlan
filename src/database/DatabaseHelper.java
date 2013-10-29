package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private final static String DB_NAME = "UEK_TIME_TABLE";
	private final static int VERSION = 1;

	private final static String CREATE_EVENTS = "CREATE TABLE IF NOT EXISTS EVENTS("
			+ "_ID LONG PRIMARY KEY,"
			+ "TOPIC VARCHAR(60),"
			+ "TYPE VARCHAR(40),"
			+ "FIRST_NAME VARCHAR(40),"
			+ "LAST_NAME VARCHAR(40),"
			+ "HOURS VARCHAR(10),"
			+ "DATE VARCHAR(10),"
			+ "ROOM VARCHAR(15),"
			+ "GROUP_NAME VARCHAR(15))";

	private static final String CREATE_GROUPS = "CREATE TABLE IF NOT EXISTS GROUPS("
			+ "_ID LONG PRIMARY KEY,"
			+ "NAME VARCHAR(15),"
			+ "IS_ACTIVE INT NOT NULL)";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("DROP TABLE GROUPS");
		// Log.v("t", "groups dropped");
//		db.execSQL("DROP TABLE EVENTS");
		// Log.v("t", "events dropped");
		db.execSQL(CREATE_EVENTS);
		// Log.v("t", "events created");
		db.execSQL(CREATE_GROUPS);
		// Log.v("t", "groups created");
//		db.execSQL("insert into groups('_ID','NAME','IS_ACTIVE') values (1,'krdzis1011',1)");
//		db.execSQL("insert into groups('_ID','NAME','IS_ACTIVE') values (2,'krdzis2011',0)");
//		db.execSQL("insert into groups('_ID','NAME','IS_ACTIVE') values (3,'krdzis3011',0)");
//		db.execSQL("insert into groups('_ID','NAME','IS_ACTIVE') values (337,'krdzis4011',1)");
//		db.execSQL("insert into groups('_ID','NAME','IS_ACTIVE') values (37,'krdzis5011',1)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Cursor getGroupsCursor() {
		try {
			String[] columns = { "NAME", "_ID as _id", "IS_ACTIVE" };
			Cursor c = getReadableDatabase().query("GROUPS", columns, null,
					null, null, null, "NAME");
			return c;
		} catch (SQLException e) {

		}

		return null;
	}

	public void changeStatus(long id) {
		Log.v("t", "status change, id: " + String.valueOf(id));
		String[] columns = { "IS_ACTIVE", "_ID" };
		String[] args = { String.valueOf(id) };
		Cursor c = getReadableDatabase().query("GROUPS", columns,
				" _ID = ?", args, null, null, null);
		c.moveToNext();
		int isActive = c.getInt(c.getColumnIndex("IS_ACTIVE"));
		if (isActive == 1) {
			Log.v("t", "setting as inactive, id: " + String.valueOf(id));
			setAsInactive(id);
		} else {
			Log.v("t", "setting as active, id: " + String.valueOf(id));
			setAsActive(id);
		}
	}

	public void setAsInactive(long id) {
		ContentValues values = new ContentValues();
		values.put("IS_ACTIVE", 0);
		String[] args = { String.valueOf(id) };
		getWritableDatabase().update("GROUPS", values, " _ID = ?", args);
	}

	public void setAsActive(long id) {
		ContentValues values = new ContentValues();
		values.put("IS_ACTIVE", 1);
		String[] args = { String.valueOf(id) };
		getWritableDatabase().update("GROUPS", values, " _ID = ?", args);
	}
}
