package database;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.table.GroupsTable;

public class DatabaseQueryFactory {

	public static List<String> getInsertGroupsQueriesList(JSONArray groups)
			throws JSONException {
		List<String> queries = new ArrayList<String>(groups.length());
		for (int i = 0; i < groups.length(); i++) {
			JSONObject group = groups.getJSONObject(i);
			String query = "INSERT INTO " + GroupsTable.TABLE_NAME
					+ " (" + GroupsTable.ID_FIELD_NAME + ", "
					+ GroupsTable.NAME_FIELD + ","
					+ GroupsTable.IS_ACTIVE_FIELD + ") VALUES ('"
					+ String.valueOf(group.getInt("id")) + "','"
					+ group.getString("name") 
					+ "', 0)";
			queries.add(query);
		}
		return queries;
	}
	
	public static String getRemoveGroupsQuery() {
		return "DELETE FROM " + GroupsTable.TABLE_NAME;
	}

}
