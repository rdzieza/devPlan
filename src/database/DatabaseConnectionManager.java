package database;

import prefereces.PreferenceHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnectionManager extends SQLiteOpenHelper{
	private static Context context;
	private static final String DB_NAME = "TIME_TABLE.db";
	private static final int VERSION = 3;
	private static DatabaseConnectionManager instance;
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

	public DatabaseConnectionManager() {
		super(context, DB_NAME, null, VERSION);

	}

	public static void initialize(Context context) {
		DatabaseConnectionManager.context = context;
		getConnection().onCreate(getConnection().getWritableDatabase());
	}

	public static synchronized DatabaseConnectionManager getConnection() {
		if (instance == null) {
			instance = new DatabaseConnectionManager();
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_GROUPS);
		db.execSQL(CREATE_ACTIVITIES);
		PreferenceHelper.saveBoolean("isDatabaseCreated", true);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
