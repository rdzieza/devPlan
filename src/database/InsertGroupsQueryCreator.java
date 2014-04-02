package database;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class InsertGroupsQueryCreator {
	private final String TABLE_NAME = "GROUPS";
	private final String INSERT_BEGGINING = "INSERT INTO " + TABLE_NAME + " (ID, NAME) VALUES ";
	private StringBuilder sb = new StringBuilder();
	
	
	public List<String> createInsertQueriesList(String json) throws JSONException{
		org.json.JSONArray groups = new org.json.JSONArray(
				json);
		int length = groups.length();
		List<String> inserts = new ArrayList<String>(length);
		sb.append(INSERT_BEGGINING);
		
		for (int i = 0; i < length; i++) {
			JSONObject group = groups.getJSONObject(i);
			sb.append("('");
			sb.append(group.getInt("id") + "','");
			sb.append(group.getString("name"));
			sb.append("')");
			inserts.add(sb.toString());
		}
		sb.delete(0, sb.length());
		sb = null;
		
		return inserts;
	}

}
