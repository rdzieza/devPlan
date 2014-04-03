package database.table;


public class GroupsTable {
	public static final String TABLE_NAME="GROUPS";
	public static final String ID_FIELD_NAME = "ID";
	public static final int ID_FIELD_INDEX = 0;
	public static final String NAME_FIELD = "NAME";
	public static final String IS_ACTIVE_FIELD = "IS_ACTIVE";
	
	
	
	public static final String CREATE_GROUPS_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME 
			+ "(" + ID_FIELD_NAME + " LONG PRIMARY KEY,"
			+ NAME_FIELD + " VARCHAR(30) NOT NULL,"
			+ IS_ACTIVE_FIELD + " INT NOT NULL DEFAULT 0)";
	
	
}


