package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View.OnCreateContextMenuListener;

public class DatabaseManager extends SQLiteOpenHelper {
	private static Context context;
	private static final String DB_NAME = "TIME_TABLE";
	private static final int VERSION = 3;
	private static DatabaseManager instance;
	private final static String CREATE_EVENTS = "CREATE TABLE IF NOT EXISTS EVENTS("
			+ "ID LONG PRIMARY KEY,"
			+ "TOPIC VARCHAR(60),"
			+ "TYPE VARCHAR(40),"
			+ "FIRST_NAME VARCHAR(40),"
			+ "LAST_NAME VARCHAR(40),"
			+ "HOURS VARCHAR(10),"
			+ "DATE VARCHAR(10),"
			+ "ROOM VARCHAR(15),"
			+ "GROUP_NAME VARCHAR(15))";
	private static final String CREATE_GROUPS = "CREATE TABLE IF NOT EXISTS GROUPS("
			+ "ID LONG PRIMARY KEY,"
			+ "NAME VARCHAR(30) NOT NULL,"
			+ "IS_ACTIVE INT NOT NULL DEFAULT 0)";
	private static final String CREATE_SELECTED = "CREATE TABLE IF NOT EXISTS SELECTED ("
			+ "ID LONG PRIMARY KEY)";

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
		db.execSQL("DROP TABLE IF EXISTS EVENTS");
//		db.execSQL("DROP TABLE IF EXISTS GROUPS");
		db.execSQL(CREATE_GROUPS);
		db.execSQL(CREATE_EVENTS);
		db.execSQL(CREATE_SELECTED);
/**
		try {
			db.execSQL("INSERT INTO EVENTS VALUES(0,'orm','lecture','robert','dzieza','4:50-6:20','15-10-2013','F303','krdzis2011')");
			db.execSQL("INSERT INTO EVENTS VALUES(1,'ask','lecure','robert','dzieza','6:50-8:20','15-10-2013','F3','krdzis2011')");
			db.execSQL("INSERT INTO EVENTS VALUES(2,'pp5','exercises','robert','dzieza','8:50-10:20','15-10-2013','F35','krdzis2011')");
		} catch (SQLException e) {

		} finally {
			// db.close();
		}
**/
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

	public static Cursor getEventsCursor(SQLiteDatabase db) {
		try {
			String[] columns = { "ID as _id", "TOPIC", "TYPE", "ROOM", "DATE",
					"HOURS" };
			Cursor c = db.query("EVENTS", columns, null, null, null, null,
					"DATE ASC, HOURS ASC");
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// db.close();
		}
		return null;
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
	
	public static boolean insertGroup(int id, String name){
		SQLiteDatabase db = instance.getWritableDatabase();
		try{
			String query = "INSERT INTO GROUPS (ID, NAME, IS_ACTIVE) VALUES ("+ String.valueOf(id)+", '"+ name +"', 0)";
			db.execSQL(query);
			return true;
		}catch(SQLException e){
			return false;
		}finally{
//			db.close();
		}
	}
	
	public static void addToSelected(long id, SQLiteDatabase db){
		try{
			db.execSQL("insert into selected values ("+ String.valueOf(id) + ")");
		}catch(SQLException e){
			
		}
	}
	
	public static void removeFromSelected(long id, SQLiteDatabase db){
		String[] args = {String.valueOf(id)};
		try{
			db.delete("selected", "id = ?", args);
		}catch(SQLException e){
			
		}
	}
	
	public static Cursor getSelected(SQLiteDatabase db){
		try{
			String[] columns = {"ID"};
			Cursor c = db.query("SELECTED", columns, null, null, null, null, null);
			return c;
		}catch(SQLException e){
			return null;
		}
	}
	
}
