package database;

import java.util.List;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseQueryExecutor {
	
	public static boolean runAllInsertGroupsQueries(SQLiteDatabase db, List<String> queries) {
		try {
			db.beginTransaction();
			db.execSQL(DatabaseQueryFactory.getRemoveGroupsQuery());
			for (String query : queries) {
				db.execSQL(query);
			}
			db.setTransactionSuccessful();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			db.endTransaction();
		}
	}
	
	public static boolean runAllInsertActivitiesQueries(SQLiteDatabase db, List<String> queries) {
		try {
			db.beginTransaction();
			db.execSQL(DatabaseQueryFactory.getRemoveActivitiesQuery());
			for (String query : queries) {
				db.execSQL(query);
			}
			db.setTransactionSuccessful();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			db.endTransaction();
		}
	}
}
