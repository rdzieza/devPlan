package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	private final static String DB_NAME = "UEK_TIME_TABLE";
	private final static int VERSION = 1;
	private static DatabaseHelper instance;
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}
	
	public synchronized DatabaseHelper getConnection(Context context){
		if(instance == null){
			instance = new DatabaseHelper(context);
		}
		return instance;
	}
	
	
}
