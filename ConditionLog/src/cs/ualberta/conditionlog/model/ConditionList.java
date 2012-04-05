package cs.ualberta.conditionlog.model;

import android.content.Context;

/**
 * A list that stores the filepaths of photos of a certain skin condition.  It has a different
 * way of loading filenames than a TagList, but otherwise uses the methods from PhotoList.
 * @author tgriffit
 * @date March 15, 2012
 */
public class ConditionList extends PhotoList {
	
	/**
	 * Creates a new ConditionList for the specified condition and loads the filenames
	 * of all photos that have been taken of that condition.
	 * @param cond - the condition to load
	 * @param context - the application context
	 */
	public ConditionList(String cond, Context context) {
		super(cond);
		loadPhotos(context);
	}
	
	/**
	 * Loads all photos associated with a condition into the filenames List.
	 * @param context - the current context of the application
	 */
	private void loadPhotos(Context context) {
		//Loads each photo that matches the condition name into the list
		DatabaseOutputAdapter dbA = new DatabaseOutputAdapter(context);
		dbA.open();
		setFilenames(dbA.loadPhotosByCondition(getName()));
		dbA.close();
	}
	

}
