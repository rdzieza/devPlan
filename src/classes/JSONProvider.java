package classes;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONProvider {
	
	public static JSONArray getJSONArrayFromString(String content) throws JSONException{
		return new JSONArray(content);
	}
	
}
