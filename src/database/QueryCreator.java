package database;

import org.json.JSONException;

public interface QueryCreator {

	public String createInsertQuery(String json) throws JSONException;
}
