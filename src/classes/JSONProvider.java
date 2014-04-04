package classes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONProvider {
	
	public static JSONArray getJSONArrayFromString(String content) throws JSONException{
		return new JSONArray(content);
	}
	
	public static String getHashFromString(String responseContent) throws JSONException {
		JSONObject json = new JSONObject(responseContent);
		return json.getString("_id");
	}
	
	public static JSONArray getActivitiesArray(String content) throws JSONException{
		JSONObject object = new JSONObject(content);
		return object.getJSONArray("activities");
	}
	
	public static String getVersionHash(String content) throws JSONException{
		JSONObject object = new JSONObject(content);
		JSONObject versions = object.getJSONObject("versions");
		Integer[] ids = getVersionParamsList(versions);
		return getJSONIds(ids, versions);
	}
	
	
	private static Integer[] getVersionParamsList(JSONObject versions){
		LinkedList<Integer> list = new LinkedList<Integer>();
		Iterator<?> i = versions.keys();
		while (i.hasNext()) {
			String key = i.next().toString();
			list.add(Integer.valueOf(key.substring(5)));
		}
		
		Integer[] array = list.toArray(new Integer[list.size()]);
		Arrays.sort(array);
		return array;
	}
	
	private static String getJSONIds(Integer[] ids, JSONObject versions) throws JSONException{
		StringBuilder sb = new StringBuilder("{");
		for(Integer s : ids){
			String groupName = "group" + s.toString();
			sb.append("\"" + groupName +  "\":\"" + versions.getString(groupName) + "\",");
		}
		sb.setLength(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}
}
