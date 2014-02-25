/**
 * 
 */
package classes;

/**
 * @author robert
 * 
 */
public class DownloadManager {
	private static boolean isDowloadingGroups = false;
	private static boolean isDownloadingTimeTable = false;

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

}
