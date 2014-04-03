package database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.table.GroupsTable;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseInserQueryExecutor {

	public static void addAllGroups(SQLiteDatabase db, String json)
			throws JSONException {
		JSONArray groups = new JSONArray(json);
		Log.v("t", "add all groups");
		synchronized (db) {
			db.beginTransaction();
			db.execSQL("DELETE FROM " + GroupsTable.TABLE_NAME);
			try {
				for (int i = 0; i < groups.length(); i++) {
					JSONObject group = groups.getJSONObject(i);
					String query = "INSERT INTO " + GroupsTable.TABLE_NAME
							+ " (" + GroupsTable.ID_FIELD_NAME + ", "
							+ GroupsTable.NAME_FIELD + ","
							+ GroupsTable.IS_ACTIVE_FIELD + ") VALUES ('"
							+ String.valueOf(group.getInt("id")) + "','"
							+ group.getString("name") 
							+ "', 0)";
					db.execSQL(query);
					Log.v("t", query);
				}
				Log.v("t", "adding finished");
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			db.endTransaction();
		}
	}

}
