package database;

import database.table.GroupsTable;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseDataProvider {

	public static Cursor getUnselectedGroupsCursor(SQLiteDatabase db) {
		try {
			String[] columns = { GroupsTable.NAME_FIELD,
					GroupsTable.ID_FIELD + " as _id",
					GroupsTable.IS_ACTIVE_FIELD };
			String[] args = { "0" };
			Cursor c = db.query(GroupsTable.TABLE_NAME, columns,
					GroupsTable.IS_ACTIVE_FIELD + " = ?", args, null, null,
					null);
			return c;
		} catch (SQLException e) {
			return null;
		} catch (IllegalStateException e) {
			return null;
		}
	}

	public static Cursor getSelectedGroupsCursor(SQLiteDatabase db) {
		try {
			String[] colums = { GroupsTable.NAME_FIELD,
					GroupsTable.ID_FIELD + " as _id" };
			String[] args = { "1" };
			Cursor c = db.query(GroupsTable.TABLE_NAME, colums, GroupsTable.IS_ACTIVE_FIELD + " = ?", args, null,
					null, GroupsTable.NAME_FIELD);
			c.moveToFirst();
			return c;
		} catch (SQLException e) {
			return null;
		}
	}

}
