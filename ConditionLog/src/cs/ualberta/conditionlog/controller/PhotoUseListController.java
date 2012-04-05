

package cs.ualberta.conditionlog.controller;

import java.util.ArrayList;

import android.content.Context;
import cs.ualberta.conditionlog.model.DatabaseInputAdapter;

/**
 * An adapter for item list data of the ListSelectionView class. Uses the Adapter design pattern.
 * date: March 14
 * @author Andrew Neufeld
 */
public class PhotoUseListController {
	
	/**
	 * Extract the log names from the list of lists
	 * @param lists A lists containing lists that contain name, imgFilepath
	 * @return 
	 */
	static public String[] getNamesFromListArray(ArrayList<ArrayList<String>> lists) {
		int len = lists.size();
		String[] list = new String[len];
		
		for (int i = 0; i < len; i++) {
			list[i] = lists.get(i).get(0);
		}
		return list;
	}
	
	/**
	 * Given a list of tags save each to the database for the photo given in filename
	 * @param context Application context for the database adapter
	 * @param filename Filename of the photo to add tags to
	 * @param tags String array with the relevant tags
	 */
	static public void savePhotoTags(Context context, String filename, String[] tags) {
		DatabaseInputAdapter dbadapter = new DatabaseInputAdapter(context);
        dbadapter.open();

		for (int i = 0; i < tags.length; i++) {
			dbadapter.addTag(filename, tags[i]);
		}
        dbadapter.close();
	}
}