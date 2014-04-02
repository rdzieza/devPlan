package database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseDataProvider {
	
	public static Cursor getUnselectedGroupsCursor(SQLiteDatabase db) {
		try { 
			String[] columns = { "NAME", "ID as _id", "IS_ACTIVE" };
			String[] args = { "0" };
			Cursor c = db.query("GROUPS", columns, "IS_ACTIVE = ?",
					args, null, null, null);
			return c;
		} catch (SQLException e) {
			return null;
		} catch (IllegalStateException e){
			return null;
		}
	}
	
	public static Cursor getSelectedGroupsCursor(SQLiteDatabase db) {
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
