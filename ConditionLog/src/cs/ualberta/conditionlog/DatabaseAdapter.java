package cs.ualberta.conditionlog;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * This class acts as an interface for manipulating the database.
 * If you want to use it, create a DatabaseAdapter object, then call open() on it.
 * When you no longer need access to the database, call close() on the object.
 *
 */
public class DatabaseAdapter {
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	/*
	 * Creates the database helper to manage the connection
	 */
	public DatabaseAdapter(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/*
	 * Opens a connection to the database
	 */
	public void open() {
		db = dbHelper.getWritableDatabase();
	}
	
	/*
	 * Closes the database connection
	 */
	public void close() {
		dbHelper.close();
	}
	
	/*
     * Adds all filenames corresponding to photos of a given condition
     * to an ArrayList, sorted by the date the photo was created
     */
    public ArrayList<String> loadPhotosByCondition(String cond) {
    	ArrayList<String> photoList = new ArrayList<String>();
    	
    	//Default query commands do not like selecting from multiple tables, so use rawQuery
    	String q = "SELECT ? FROM ?, ? WHERE ?=? ODER BY ? ASC";
    	String[] args = {DatabaseHelper.PHOTO_TABLE + "." + DatabaseHelper.PHOTO_FILE,
    	                 DatabaseHelper.PHOTO_TABLE, DatabaseHelper.COND_TABLE,
    	                 DatabaseHelper.COND_NAME, cond, DatabaseHelper.PHOTO_DATE};
    	
    	Cursor c = db.rawQuery(q, args);
    	c.moveToFirst();
    	while(!c.isAfterLast()) {
    		photoList.add(c.getString(0));
    		c.moveToNext();
    	}
    	
    	c.close();
    	
    	return photoList;
    }
    
    /*
     * Adds all filenames corresponding to photos with a given tag to an
     * ArrayList, sorted by the date the photo was created
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
    
    /*
     * Returns a list containing all conditions added to the database so far and a photo
     * from each to use as a thumbnail
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
    
    /*
     * Loads every tag that has been added to the database so far and a thumbnail
     * for each one
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
    
    /*
     * Adds a photo to the database
     */
    public void addPhotoToDB(String filename) {
    	
    	//Inserts the new photo into the photos table
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHelper.PHOTO_FILE, filename);
    	//Uses the SQLite function date() to set the photo's date to the current time
    	values.put(DatabaseHelper.PHOTO_DATE, "date('now')");
    	
    	long insertTest = db.insert(DatabaseHelper.PHOTO_TABLE, null, values);
    	
    	//If the values can't be inserted into the table, throw an exception
    	if (insertTest < 0)
    		throw new SQLException();
    }
    
    /*
     * Adds a photo to a condition by inserting it into the conditions table
     * under that condition
     */
    public void addPhotoToCondition(String photo, String cond) {
    	//Inserts a tuple representing the relationship in the conditions table
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHelper.PHOTO_FILE, photo);
    	//Uses the SQLite function date() to set the photo's date to the current time
    	values.put(DatabaseHelper.COND_NAME, cond);
    	
    	long insertTest = db.insert(DatabaseHelper.COND_TABLE, null, values);
    	
    	//If the values can't be inserted into the table, throw an exception
    	if (insertTest < 0)
    		throw new SQLException();
    }

    /*
     * Adds a condition with a null 
     */
    public void addCondition(String cond)
    {
    	
    }
}
