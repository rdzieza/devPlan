package classes;

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
	
}
