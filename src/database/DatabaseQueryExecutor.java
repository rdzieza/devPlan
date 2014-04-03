package database;

import java.util.List;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseQueryExecutor {
	
	public static boolean addAllQueries(SQLiteDatabase db, List<String> queries) {
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
}
