package database;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import database.table.GroupsTable;

public class DatabaseDataProvider {

	public static Cursor getUnselectedGroupsCursor(SQLiteDatabase db) {
		try {
			String[] columns = { GroupsTable.NAME_FIELD,
					GroupsTable.ID_FIELD_NAME + " as _id",
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
					GroupsTable.ID_FIELD_NAME + " as _id" };
			String[] args = { "1" };
			Cursor c = db.query(GroupsTable.TABLE_NAME, colums, GroupsTable.IS_ACTIVE_FIELD + " = ?", args, null,
					null, GroupsTable.NAME_FIELD);
			c.moveToFirst();
			return c;
		} catch (SQLException e) {
			return null;
		}
	}

	public static int getNumberOfSelectedGroups(SQLiteDatabase db) {
		String[] columns = { GroupsTable.IS_ACTIVE_FIELD };
		String[] args = { "1" };
		Cursor c = db.query(GroupsTable.TABLE_NAME, columns, GroupsTable.IS_ACTIVE_FIELD + " = ?", args, null, null, null); 
		return c.getCount();
	}

	public static List<NameValuePair> getSelectedAsNameValuePairsList(SQLiteDatabase db){
		String[] columns = { GroupsTable.IS_ACTIVE_FIELD, GroupsTable.ID_FIELD_NAME };
		String[] args = { "1" };
		Cursor c = db.query(GroupsTable.TABLE_NAME, columns, GroupsTable.IS_ACTIVE_FIELD + " = ?", args, null, null, null);
		List<NameValuePair> params = new ArrayList<NameValuePair>(c.getCount());
		while(c.moveToNext()) {
			params.add(new BasicNameValuePair("group_id[]", String.valueOf(c.getLong(GroupsTable.ID_FIELD_INDEX))));
		}
		return null;
	}
}
