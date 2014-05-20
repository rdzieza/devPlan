package database;

import database.table.ActivitiesTable;
import database.table.GroupsTable;
import prefereces.PreferenceHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnectionManager extends SQLiteOpenHelper{
	private static Context context;
	private static final String DB_NAME = "TIME_TABLE.db";
	private static final int VERSION = 3;
	private static DatabaseConnectionManager instance;
	
	

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
		db.execSQL(GroupsTable.CREATE_GROUPS_QUERY);
		db.execSQL(ActivitiesTable.CREATE_ACTIVITIES_QUERY);
		PreferenceHelper.saveBoolean("isDatabaseCreated", true);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
