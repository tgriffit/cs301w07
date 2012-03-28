/**
 * A list that stores the filepaths of photos of a certain skin condition.  It has a different
 * way of loading filenames than a TagList, but otherwise uses the methods from PhotoList.
 * 
 * @author tgriffit
 * @date March 15, 2012
 */

package cs.ualberta.conditionlog.model;

import android.content.Context;

public class TagList extends PhotoList {
	
	/**
	 * Creates a new TagList with the specified name and loads all photos that have
	 * been tagged with the tag.
	 * @param tag - the tag to load
	 * @param context - the application context
	 */
	TagList(String tag, Context context) {
		super(tag);
		loadPhotos(context);
	}
	
	/**
	 * Loads all photos associated with a condition into the filenames List.
	 * @param context - the current context of the application
	 */
	private void loadPhotos(Context context) {
		//Loads each photo that matches the condition name into the list
		DatabaseAdapter dbA = new DatabaseAdapter(context);
		dbA.open();
		setFilenames(dbA.loadPhotosByTag(getName()));
		dbA.close();
	}

}