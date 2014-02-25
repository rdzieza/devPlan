package prefereces;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author Robert Dzieża
 * 
 *         Preference Helper is responsible for interaction between app and its
 *         settings.
 * 
 */
public class PreferenceHelper {
	private static final String NAME = "TIME_TABLE_PREFS";
	private static SharedPreferences settings;

	public static void initialize(Context context) {
		settings = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}

	public static void saveString(String key, String value) {
		Editor edit = settings.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static void saveInt(String key, int value) {
		Editor edit = settings.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static void saveLong(String key, long value) {
		Editor edit = settings.edit();
		edit.putLong(key, value);
		edit.commit();
	}

	public static void saveBoolean(String key, boolean value) {
		Editor edit = settings.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static int getInt(String key) {
		return settings.getInt(key, 0);
	}

	public static String getString(String key) {
		return settings.getString(key, "brak");
	}

	public static boolean getBoolean(String key) {
		return settings.getBoolean(key, false);
	}

	public static long getLong(String key) {
		return settings.getLong(key, -1);
	}

}
