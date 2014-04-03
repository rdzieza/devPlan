package database.table;

public class ActivitiesTable {
	public static final String TABLE_NAME = "ACTIVITIES";
	public static final String ID_FIELD = "ID";
	public static final String GROUP_NAME_FIELD = "GROUP_NAME";
	public static final String GROUP_ID_FIELD = "GROUP_ID";
	public static final String TUTOR_ID_FIELD = "TUTOR_ID";
	public static final String TUTOR_NAME_FIELD = "TUTOR_NAME";
	public static final String TUTOR_URL_FIELD = "TUTOR_URL";
	public static final String PLACE_ID_FIELD = "PLACE_ID";
	public static final String PLACE_LOCATION_FIELD = "PLACE_LOCATION";
	public static final String CATEGORY_NAME_FIELD = "CATEGORY_NAME";
	public static final String NAME_FIELD = "NAME";
	public static final String NOTES_FIELD = "NOTES";
	public static final String STATE_FIELD = "STATE";
	public static final String START_AT_FIELD = "START_AT";
	public static final String END_AT_FIELD = "END_AT";
	public static final String DAY_FIELD = "DAY";
	public static final String DAY_OF_WEEK_FIELD = "DAY_OF_WEEK";
	public static final String TIME_FIELD = "TIME";

	public final static String CREATE_ACTIVITIES_QUERY = "CREATE TABLE IF NOT EXISTS "
			+ ActivitiesTable.TABLE_NAME
			+ " ("
			+ ActivitiesTable.ID_FIELD
			+ " LONG PRIMARY KEY,"
			+ ActivitiesTable.GROUP_NAME_FIELD
			+ " VARCHAR(60),"
			+ ActivitiesTable.GROUP_ID_FIELD
			+ " LONG,"
			+ ActivitiesTable.TUTOR_ID_FIELD
			+ " LONG,"
			+ ActivitiesTable.TUTOR_NAME_FIELD
			+ " VARCHAR(60),"
			+ ActivitiesTable.TUTOR_URL_FIELD
			+ " VARCHAR(60),"
			+ ActivitiesTable.PLACE_ID_FIELD
			+ " LONG,"
			+ ActivitiesTable.PLACE_LOCATION_FIELD
			+ " VARCHAR(30),"
			+ ActivitiesTable.CATEGORY_NAME_FIELD
			+ " VARCHAR(30),"
			+ ActivitiesTable.NAME_FIELD
			+ " VARCHAR(60),"
			+ ActivitiesTable.NOTES_FIELD
			+ " VARCHAR(60),"
			+ ActivitiesTable.STATE_FIELD
			+ " INT,"
			+ ActivitiesTable.START_AT_FIELD
			+ " VARCHAR(20),"
			+ ActivitiesTable.END_AT_FIELD
			+ " VARCHAR(20),"
			+ ActivitiesTable.DAY_FIELD
			+ " VARCHAR(20),"
			+ ActivitiesTable.DAY_OF_WEEK_FIELD
			+ " VARCHAR(20),"
			+ ActivitiesTable.TIME_FIELD + " LONG)";

}