package database;

import java.util.List;

import database.table.GroupsTable;

import android.content.ContentValues;
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

	public static boolean setGroupAsActive(SQLiteDatabase db, long groupId) {
		ContentValues values = new ContentValues();
		values.put(GroupsTable.IS_ACTIVE_FIELD, 1);
		String[] args = { String.valueOf(groupId) };
		try {
			db.update(GroupsTable.TABLE_NAME, values, " ID = ?", args);
			return true;
		}catch(SQLException e) {
			return false;
		}
	}
	
	public static boolean setGroupAsInactive(SQLiteDatabase db, long groupId) {
		ContentValues values = new ContentValues();
		values.put(GroupsTable.IS_ACTIVE_FIELD, 0);
		String[] args = { String.valueOf(groupId) };
		try {
			db.update(GroupsTable.TABLE_NAME, values, " ID = ?", args);
			return true;
		}catch(SQLException e) {
			return false;
		}
	}
}
