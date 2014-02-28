/**
 * 
 */
package classes;

import android.util.Log;
import fragments.AddGroupFragment;

/**
 * @author robert
 * 
 */
public class DownloadManager {
	private static boolean isDowloadingGroups = false;
	private static boolean isDownloadingTimeTable = false;
	private static AddGroupFragment addGroupFragment = null;

	public static boolean isDowloadingGroups() {
		return isDowloadingGroups;
	}

	public static void setDowloadingGroups(boolean isDowloadingGroups) {
		DownloadManager.isDowloadingGroups = isDowloadingGroups;
	}

	public static boolean isDownloadingTimeTable() {
		return isDownloadingTimeTable;
	}

	public static void setDownloadingTimeTable(boolean isDownloadingTimeTable) {
		DownloadManager.isDownloadingTimeTable = isDownloadingTimeTable;
	}
	
	public static void setAddGroupFragment(AddGroupFragment addGroupFragment){
		DownloadManager.addGroupFragment = addGroupFragment;
		Log.v("t", "setting reference");
	}
	
	public static AddGroupFragment getAddGroupFragment(){
		Log.v("t", "getting reference");
		return DownloadManager.addGroupFragment;
	}

}
