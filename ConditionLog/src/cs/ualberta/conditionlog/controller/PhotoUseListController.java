

package cs.ualberta.conditionlog.controller;

import java.util.ArrayList;

/**
 * An adapter for item list data of the ListSelectionView class. Uses the Adapter design pattern.
 * date: March 14
 * @author Andrew Neufeld
 */
public class PhotoUseListController {
	
	static public String[] getNamesFromListArray(ArrayList<ArrayList<String>> lists) {
		int len = lists.size();
		String[] list = new String[len];
		
		for (int i = 0; i < len; i++) {
			list[i] = lists.get(i).get(0);
		}
		return list;
	}
}