/**
 * 
 * This class acts as an interface for manipulating the database.
 * 
 * After creating a DatabaseAdapter object, call open() on it before using it or you will
 * get Null Pointer Exceptions.
 * Once you are done with the DatabaseAdapter, call close() on it.
 * 
 * To add a condition to the database use addCondition(String condition)
 * To add a photo to the database use addPhoto(String filepath, String condition)
 * To get a list of all conditions use loadConditions()
 * To get a list of all tags use loadTags()
 * To get a list of all photos of a condition use loadPhotosByCondition(String condition)
 * To get a list of all photos with a tag use loadPhotosByTag(String tag)
 * To add a tag to a photo use addTag(String filepath, String tag)
 * To delete a condition, use deleteCondition(String condition)
 * 
 * @author tgriffit
 * @date March 15, 2012
 *
 */

package cs.ualberta.conditionlog;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author   tgriffit
 */
public class DatabaseAdapter {
	
	/** 
	 * @uml.property name="dbHelper"
	 * @uml.associationEnd aggregation="shared"
	 */
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	/**
	 * Constructor for the adapter.  It sets up a DatabaseHelper to maintain the
	 * database connection.
	 * @param context - the current application context
	 */
	public DatabaseAdapter(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * Opens a connection to the database using the DatabaseHelper.  Must be called
	 * before any other DatabaseAdapter methods can be called.
	 */
	public void open() {
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Closes the connection to the database.  Should always be called when you are
	 * doing calling DatabaseAdapter methods.
	 */
	public void close() {
		dbHelper.close();
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
    	
    	//Default query commands do not like selecting from multiple tables, so use rawQuery
    	String q = "SELECT ? FROM ?, ? WHERE ?=? ODER BY ? ASC";
    	String[] args = {DatabaseHelper.PHOTO_TABLE + "." + DatabaseHelper.PHOTO_FILE,
    	                 DatabaseHelper.PHOTO_TABLE, DatabaseHelper.TAGS_TABLE,
    	                 DatabaseHelper.TAGS_NAME, tag, DatabaseHelper.PHOTO_DATE};
    	
    	Cursor c = db.rawQuery(q, args);
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
     * Adds a photo to the photos table and to the conditions table with its
     * associated condition
     * @param photo - a filepath to a .bmp file
     * @param cond
     */
    public void addPhoto(String photo, String cond) {
    	addPhotoToDB(photo);
    	addPhotoToCondition(photo, cond);
    }
    
    /*
     * Adds a photo to the photos table in the database.
     */
    private void addPhotoToDB(String filepath) {
    	
    	//Inserts the new photo into the photos table
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHelper.PHOTO_FILE, filepath);
    	//Uses the SQLite function date() to set the photo's date to the current time
    	values.put(DatabaseHelper.PHOTO_DATE, "date('now')");
    	
    	long insertTest = db.insert(DatabaseHelper.PHOTO_TABLE, null, values);
    	
    	//If the values can't be inserted into the table, throw an exception
    	if (insertTest < 0)
    		throw new SQLException();
    }
    
    /*
     * Adds a photo to the conditions table, making that photo part of the condition's list.
     * If the condition does not exist, this creates it.
     */
    private void addPhotoToCondition(String photo, String cond) {
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHelper.PHOTO_FILE, photo);
    	values.put(DatabaseHelper.COND_NAME, cond);
    	long insertTest;
    	
    	//Tests if there is a tuple containing the condition but no photo
    	if (findNullCond(cond)) {
    		//Place photo in null slot rather than creating a new tuple
    		String[] arg = {cond};
    		insertTest = db.update(DatabaseHelper.COND_TABLE, values, 
    				               DatabaseHelper.COND_NAME + "=? AND " + 
    				               DatabaseHelper.PHOTO_FILE + "=NULL", arg);
    	} else {        	
    		insertTest = db.insert(DatabaseHelper.COND_TABLE, null, values);
    	}
    	
    	//If the values can't be inserted into the table, throw an exception
    	if (insertTest < 0)
    		throw new SQLException();
    }

    /**
     * Adds a new condition to the conditions table with no associated photo.  The next
     * photo added to the condition will be added to this line in the table.
     * @param cond - the condition to create
     */
    public void addCondition(String cond)
    {
    	//Inserts a tuple representing the relationship in the conditions table
    	ContentValues values = new ContentValues();
    	//The name of the condition to store the photo under
    	values.put(DatabaseHelper.COND_NAME, cond);
    	
    	long insertTest = db.insert(DatabaseHelper.COND_TABLE, null, values);
    	
    	//If the values can't be inserted into the table, throw an exception
    	if (insertTest < 0)
    		throw new SQLException();
    }
    
    /*
     * If the conditions table contains a tuple containing the condition and a null
     * photo, returns true, otherwise returns false
     */
    private boolean findNullCond(String cond)
    {
    	String[] cols = {"COUNT (*)"};
    	String[] args = {DatabaseHelper.COND_NAME, cond, DatabaseHelper.PHOTO_FILE}; 
    	Cursor c = db.query(DatabaseHelper.COND_TABLE, cols, "?=? AND ?=NULL", args, null, null, null);
    	
    	//Finds the number of rows with the specified condition and no photo
    	c.moveToFirst();
    	int test = c.getInt(0);
    	
    	c.close();
    	
    	if (test > 0)
    		return true;
    	else
    		return false;
    }
    
    /**
     * Adds a line to the tags table which marks a photo with a certain tag
     * @param photo - a filepath to a .bmp file
     * @param tag - the tag to add
     */
    public void addTag(String photo, String tag) {
    	ContentValues values = new ContentValues();
    	//The name of the condition to store the photo under
    	values.put(DatabaseHelper.TAGS_NAME, tag);
    	values.put(DatabaseHelper.PHOTO_FILE, photo);
    	
    	long insertTest = db.insert(DatabaseHelper.TAGS_TABLE, null, values);
    	
    	//If the values can't be inserted into the table, throw an exception
    	if (insertTest < 0)
    		throw new SQLException();
    }
    
    /**
     * Removes all lines associated condition from the conditions table.  The photos
     * still exist, and can still be associated with tags, but will not be found by
     * searching for condition.
     * @param cond - the condition to remove.
     */
    public void deleteCondition(String cond) {
    	String[] args = {cond};
    	
    	db.delete(DatabaseHelper.COND_TABLE, DatabaseHelper.COND_NAME + "=?", args);
    }
}
