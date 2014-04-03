package database;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.table.ActivitiesTable;
import database.table.GroupsTable;

public class DatabaseQueryFactory {
	private static final String INSERT_ACTIVITY_QUERY_PREFIX = "INSERT INTO " + ActivitiesTable.TABLE_NAME +" (" 
			+ ActivitiesTable.ID_FIELD + ", "
			+ ActivitiesTable.GROUP_NAME_FIELD + ", "
			+ ActivitiesTable.GROUP_ID_FIELD + ", "
			+ ActivitiesTable.TUTOR_ID_FIELD + ", "
			+ ActivitiesTable.TUTOR_NAME_FIELD + ", "
			+ ActivitiesTable.TUTOR_URL_FIELD + ", "
			+ ActivitiesTable.PLACE_ID_FIELD + ", "
			+ ActivitiesTable.PLACE_LOCATION_FIELD + ", "
			+ ActivitiesTable.CATEGORY_NAME_FIELD + ", "
			+ ActivitiesTable.NAME_FIELD + ", "
			+ ActivitiesTable.NOTES_FIELD + ", "
			+ ActivitiesTable.STATE_FIELD + ", "
			+ ActivitiesTable.START_AT_FIELD + ", "
			+ ActivitiesTable.END_AT_FIELD + ", "
			+ ActivitiesTable.DAY_FIELD + ", "
			+ ActivitiesTable.DAY_OF_WEEK_FIELD + ", "
			+ ActivitiesTable.TIME_FIELD + ") VALUES (";
	
	
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
	
	public static List<String> getInsertActivitiesQueriesList(JSONArray activities)
			throws JSONException {
		StringBuilder sb = new StringBuilder();
		List<String> queries = new ArrayList<String>(activities.length());
		
		for (int i = 0; i < activities.length(); i++) {
			sb.append(INSERT_ACTIVITY_QUERY_PREFIX);
			JSONObject activity = activities.getJSONObject(i);

			sb.append("'" + activity.getInt("id") + "',");
			
			JSONObject group = activity.getJSONObject("group");
			sb.append("'" + group.getString("name") + "', ");
			sb.append("'" + group.getInt("id") + "', ");
			
			JSONObject tutor = activity.getJSONObject("tutor");
			sb.append("'" + tutor.getInt("id") + "', ");
			sb.append("'" + tutor.getString("name") + "', ");
			sb.append("'" + tutor.getString("moodle_url") + "', ");
			
			JSONObject place = activity.getJSONObject("place");
			sb.append("'" + place.getInt("id") + "', ");
			sb.append("'" + place.getString("location") + "', ");
			
			sb.append("'" + activity.getString("category") + "', ");
			
			sb.append("'" + activity.getString("name") + "', ");
			
			sb.append("'" + activity.getString("notes") + "', ");
			
			sb.append("'" + activity.getInt("state") + "', ");
			
			sb.append("'" + activity.getString("starts_at") + "', ");
			
			sb.append("'" + activity.getString("ends_at") + "', ");
			
			sb.append("'" + activity.getString("date") + "', ");
			sb.append("'" + activity.getString("day_of_week") + "', ");
			sb.append("'" + activity.getLong("starts_at_timestamp") + "')");
			queries.add(sb.toString());
			sb.delete(0, sb.length());
					
		}
		
		return queries;
	}
	
	public static String getRemoveGroupsQuery() {
		return "DELETE FROM " + GroupsTable.TABLE_NAME;
	}
	
	public static String getRemoveActivitiesQuery() {
		return "DELETE FROM " + ActivitiesTable.TABLE_NAME;
	}

}
