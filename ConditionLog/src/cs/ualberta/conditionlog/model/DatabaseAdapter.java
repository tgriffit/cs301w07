package cs.ualberta.conditionlog.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * This class acts as an interface for manipulating the database.
 * 
 * After creating a DatabaseAdapter object, call open() on it before using it or you will
 * get Null Pointer Exceptions.
 * Once you are done with the DatabaseAdapter, call close() on it.

 * @author tgriffit
 * @date March 15, 2012
 *
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
    	SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
    	Date date = new Date();
    	
    	//Inserts the new photo into the photos table
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHelper.PHOTO_FILE, filepath);
    	//Uses the SQLite function date() to set the photo's date to the current time
    	values.put(DatabaseHelper.PHOTO_DATE, "date('now')");
    	//Enters the date as a pre-formatted string because it's much easier than dealing with sql date formatting
    	values.put(DatabaseHelper.PHOTO_DATE_FORMATTED, format.format(date));
    	
    	db.insert(DatabaseHelper.PHOTO_TABLE, null, values);
    }
    
    /*
     * Adds a photo to the conditions table, making that photo part of the condition's list.
     * If the condition does not exist, this creates it.
     */
    private void addPhotoToCondition(String photo, String cond) {
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHelper.PHOTO_FILE, photo);
    	values.put(DatabaseHelper.COND_NAME, cond);
    	try {
    		//Tests if there is a tuple containing the condition but no photo
    		if (findNullCond(cond)) {
    			//Place photo in null slot rather than creating a new tuple
    			String[] arg = {cond};
    			db.update(DatabaseHelper.COND_TABLE, values, 
    								   DatabaseHelper.COND_NAME + "=? AND " + 
    								   DatabaseHelper.PHOTO_FILE + "=NULL", arg);
    		} else {        	
    			db.insert(DatabaseHelper.COND_TABLE, null, values);
    		}
    	} catch (SQLException e) {
    		//Exceptions are caused by primary key violations.  When that happens, they should be ignored.
    	}
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
    	
    	try {
    		db.insert(DatabaseHelper.COND_TABLE, null, values);
    	} catch (SQLException e) {
    		//Exceptions are caused by primary key violations.  When that happens, they should be ignored.
    	}
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
    	
    	try {
    		db.insert(DatabaseHelper.TAGS_TABLE, null, values);
    	} catch (SQLException e) {
    		//Exceptions are caused by primary key violations.  When that happens, they should be ignored.
    	}
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
    
    /**
     * Removes all lines associated with a tag from the tags table.
     * @param tag
     */
    public void deleteTag(String tag) {
    	String[] args = {tag};
    	
    	db.delete(DatabaseHelper.TAGS_TABLE, DatabaseHelper.TAGS_NAME + "=?", args);
    }
    
    /**
     * Removes a condition from the given photo.  There is a database trigger to ensure
     * that this will not completely delete the condition.
     * @param filename
     * @param cond
     */
    public void deleteConditionFromPhoto(String filename, String cond) {
    	String[] args = {filename, cond};
    	
    	db.delete(DatabaseHelper.COND_TABLE, DatabaseHelper.COND_NAME + "=? AND " + DatabaseHelper.PHOTO_FILE + "=?", args);
    }
    
    /**
     * Removes a tag from the given photo.  If no more photos have that tag,
     * the tag no longer exists.
     * @param filename
     * @param tag
     */
    public void deleteTagFromPhoto(String filename, String tag) {
    	String[] args = {filename, tag};
    	
    	db.delete(DatabaseHelper.TAGS_TABLE, DatabaseHelper.TAGS_NAME + "=? AND " + DatabaseHelper.PHOTO_FILE + "=?", args);
    }
    
    /**
     * Deletes a photo from the photos table and removes all instances of it from
     * the conditions and tags tables.  There is a trigger in the database that
     * prevents a condition from being deleted when its last photo is deleted.
     * @param filename
     */
    public void deletePhoto(String filename) {
    	deleteMatchingCondition(filename);
    	
    	String[] args = {filename};
    	
    	db.delete(DatabaseHelper.PHOTO_TABLE, DatabaseHelper.PHOTO_FILE + "=?", args);
    }
    
    /*
     * When a photo is deleted the corresponding lines from the conditions table are deleted unless that
     * would remove the last instance of a condition from the table, in which case the photo's filename is instead
     * replaced with a null string.
     */
    private void deleteMatchingCondition(String filename) {
    	
    	String query = "SELECT " + DatabaseHelper.COND_NAME + ", COUNT(*) as c " +
    				   "FROM " + DatabaseHelper.COND_TABLE + " " +
    				   "WHERE " + DatabaseHelper.COND_NAME + " IN " +
    				  		"(SELECT " + DatabaseHelper.COND_NAME + " " + 
    				  		"FROM " + DatabaseHelper.COND_TABLE + " " +
    				  		"WHERE " + DatabaseHelper.COND_NAME + " = '" + filename + "') " +
    				   "GROUP BY " + DatabaseHelper.COND_NAME;
    	
    	Cursor c = db.rawQuery(query, null);
    	
    	c.moveToFirst();
    	
    	while (!c.isAfterLast()) {
    		//If there is more than one row for that condition, delete the row
    		if (c.getInt(1) > 1)
    			deleteConditionFromPhoto(filename, c.getString(0));
    		//Otherwise, leave the condition in the table with a null photo
    		else {
    			ContentValues values = new ContentValues();
    	    	values.put(DatabaseHelper.COND_NAME, c.getString(0));
    	    	String[] args = {filename, c.getString(0)};
    	    	
    	    	db.update(DatabaseHelper.COND_TABLE, values, DatabaseHelper.COND_NAME + "=? AND " + DatabaseHelper.PHOTO_FILE + "=?", args);
    		}
    		
    		c.moveToNext();
    	}
    	
    	c.close();
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
    
    /**
     * Adds the given password hash to the database
     * @param hash
     */
    public void setPasswordHash(String hash) {
    	ContentValues values = new ContentValues();
    	
    	values.put(DatabaseHelper.PASS, hash);
    	
    	db.insert(DatabaseHelper.PASS_TABLE, null, values);
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
}
