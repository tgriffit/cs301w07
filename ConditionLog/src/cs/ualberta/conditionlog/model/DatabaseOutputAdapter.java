package cs.ualberta.conditionlog.model;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

/**
 * Contains methods to load entries from the database for use by the program.  Inherits
 * the open() and close() methods from DatabaseAdapter, which must be used before/after
 * using any other methods.
 * @author tgriffit
 * @date April 5, 2012
 *
 */
public class DatabaseOutputAdapter extends DatabaseAdapter {

	public DatabaseOutputAdapter(Context context) {
		super(context);
	}

	/**
	 * Adds all filepaths corresponding to photos of a given condition
     * to an ArrayList, sorted by the date the photo was created
	 * @param cond - the condition to search for
	 * @return An ArrayList containing the filepaths to all photos matching the condition
	 */
    public ArrayList<String> loadPhotosByCondition(String cond) {
    	ArrayList<String> photoList = new ArrayList<String>();
    	
    	//Default query commands do not like selecting from multiple tables, so use rawQuery

    	String q = "SELECT "+DatabaseHelper.PHOTO_FILE+" " +
    			   "FROM "+DatabaseHelper.COND_TABLE+" INNER JOIN "+DatabaseHelper.PHOTO_TABLE+" USING ("+DatabaseHelper.PHOTO_FILE+") " +
    			   "WHERE "+DatabaseHelper.COND_NAME+"='"+cond+"' ORDER BY "+DatabaseHelper.PHOTO_DATE+" ASC";
    	
    	Cursor c = db.rawQuery(q, null);
    	c.moveToFirst();
    	while(!c.isAfterLast()) {
    		photoList.add(c.getString(0));
    		c.moveToNext();
    	}
    	
    	c.close();
    	
    	return photoList;
    }
    
    /**
     * Adds all filepaths corresponding to photos with a given tag to an
     * ArrayList, sorted by the date the photo was created
     * @param tag - the photo tag to search for
     * @return An ArrayList containing the filepaths to all photos marked with the tag
     */
    public ArrayList<String> loadPhotosByTag(String tag) {
    	ArrayList<String> photoList = new ArrayList<String>();
    	
    	String q = "SELECT "+DatabaseHelper.PHOTO_FILE+" " +
		   		   "FROM "+DatabaseHelper.TAGS_TABLE+" INNER JOIN "+DatabaseHelper.PHOTO_TABLE+" USING ("+DatabaseHelper.PHOTO_FILE+") " +
		   		   "WHERE "+DatabaseHelper.TAGS_NAME+"='"+tag+"' ORDER BY "+DatabaseHelper.PHOTO_DATE+" ASC";

    	Cursor c = db.rawQuery(q, null);
    	c.moveToFirst();
    	
    	//Add each entry to the list
    	while(!c.isAfterLast()) {
    		photoList.add(c.getString(0));
    		c.moveToNext();
    	}
    	
    	c.close();
    	
    	return photoList;
    }
    
    /**
     * Returns a list containing all conditions added to the database so far and a photo
     * from each to use as a thumbnail
     * @return An ArrayList of ArrayLists, each of which contains a condition and a photo filepath
     */
    public ArrayList<ArrayList<String>> loadConditions() {
    	ArrayList<ArrayList<String>> conds = new ArrayList<ArrayList<String>>();
    	
    	//Select each unique condition from the conditions table
    	String[] cols = {DatabaseHelper.COND_NAME, DatabaseHelper.PHOTO_FILE};
    	Cursor c = db.query(DatabaseHelper.COND_TABLE, cols, null, null,
    			            DatabaseHelper.COND_NAME, null, null);
    	
    	c.moveToFirst();
    	//Add each entry to the list
    	while(!c.isAfterLast()) {
    		//Creates a list containing the tag and a photo filepath, then adds them to the main list
    		ArrayList<String> list = new ArrayList<String>();
    		list.add(c.getString(0));
    		try {
    			list.add(c.getString(1));
    		} catch (SQLException e) {
    			//An exception is thrown if the photo filename is null, in which case a default picture is used
    			list.add("@drawable/ic_launcher.png");
    		}
    		conds.add(list);
    		
    		c.moveToNext();
    	}
    	
    	c.close();
    	
    	return conds;
    }
    
    /**
     * Loads every tag that has been added to the database so far and a thumbnail
     * for each one.  Assumes that a tag must be associated with a photo, and thus
     * the photo entry cannot be null
     * @return An ArrayList of ArrayLists, each of which contains a tag and a photo filepath
     */
    public ArrayList<ArrayList<String>> loadTags() {
    	ArrayList<ArrayList<String>> tags = new ArrayList<ArrayList<String>>();
    	
    	//Select each unique tag from the tags table
    	String[] cols = {DatabaseHelper.TAGS_NAME, DatabaseHelper.PHOTO_FILE};
    	Cursor c = db.query(DatabaseHelper.TAGS_TABLE, cols, null, null,
    			            DatabaseHelper.TAGS_NAME, null, null);
    	
    	c.moveToFirst();
    	
    	//Add each entry to the list
    	while(!c.isAfterLast()) {
    		//Creates a list containing the tag and a photo filepath, then adds them to the main list
    		ArrayList<String> list = new ArrayList<String>();
    		list.add(c.getString(0));
    		list.add(c.getString(1));
    		tags.add(list);
    		
    		c.moveToNext();
    	}
    	
    	c.close();
    	
    	return tags;
    }
    
    /**
     * Loads a list containing all the tags for a given photo.
     * @param filename
     * @return an ArrayList of strings
     */
    public ArrayList<String> loadTagsForPhoto(String filename) {
    	ArrayList<String> list = new ArrayList<String>();

    	String[] cols = {DatabaseHelper.TAGS_NAME};
    	String[] args = {filename};

    	Cursor c = db.query(DatabaseHelper.TAGS_TABLE, cols, DatabaseHelper.PHOTO_FILE + "=?", args,
    			null, null, null);

    	c.moveToFirst();

    	while(!c.isAfterLast()) {
    		list.add(c.getString(0));
    		c.moveToNext();
    	}

    	c.close();

    	return list;
    }
    
    /**
     * Finds the timestamp for a given photo
     * @param filename
     * @return the timestamp as a string
     */
    public String getPhotoTimestamp(String filename) {
    	String[] cols = {DatabaseHelper.PHOTO_DATE_FORMATTED};
    	String[] args = {filename};
    	
    	Cursor c = db.query(DatabaseHelper.PHOTO_TABLE, cols, DatabaseHelper.PHOTO_FILE + "=?", args,
    			            null, null, null);
    	
    	String date = "";
    	
    	if (c.moveToFirst())
    		date = c.getString(0);
    	
    	c.close();
    	
    	return date;
    }
    
    /**
     * Finds the filenames of every photo in the database sorted by time
     * @return an ArrayList of filename strings
     */
    public ArrayList<String> loadPhotosByTime() {
    	ArrayList<String> photoList = new ArrayList<String>();
    	
    	String[] args = {DatabaseHelper.PHOTO_FILE};
    	
    	Cursor c = db.query(DatabaseHelper.PHOTO_TABLE, args, null, null, null, null,
    			            DatabaseHelper.PHOTO_DATE + " ASC");
    	
    	c.moveToFirst();
    	while(!c.isAfterLast()) {
    		photoList.add(c.getString(0));
    		c.moveToNext();
    	}
    	
    	c.close();
    	
    	return photoList;
    }
    
    /**
     * Retrieves the stored password hash.
     * @return the password hash or an empty string if the table is empty
     */
    public String getPasswordHash() {
    	String[] cols = {DatabaseHelper.PASS};
    	String hash = "";
    	
    	Cursor c = db.query(DatabaseHelper.PASS_TABLE, cols, null, null, null, null, null);
    	
    	if (c.moveToFirst()) {
    		hash = c.getString(0);
    	}
    	
    	c.close();
    	
    	return hash;
    }
}
