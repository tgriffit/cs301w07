package cs.ualberta.conditionlog.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

/**
 * Contains methods for adding entries to the database.  It inherits the open() and close()
 * methods from DatabaseAdapter, which are used in the same way.
 * @author tgriffit
 * @date April 5, 2012
 *
 */
public class DatabaseInputAdapter extends DatabaseAdapter {

	//Creates the database object
	public DatabaseInputAdapter(Context context) {
		super(context);
	}

	/**
     * Adds a photo to the photos table and to the conditions table with its
     * associated condition
     * @param photo - a filepath to a .bmp file
     * @param cond
     */
    public void addPhoto(String photo, String cond) {
    	addPhotoToDB(photo);
    	addPhotoToCondition(photo, cond.trim());
    }
    
    /*
     * Adds a photo to the photos table in the database.
     */
    private void addPhotoToDB(String filename) {
    	SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
    	Date date = new Date();
    	
    	//Inserts the new photo into the photos table
    	ContentValues values = new ContentValues();
    	values.put(DatabaseHelper.PHOTO_FILE, filename);
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
    	values.put(DatabaseHelper.COND_NAME, cond.trim());
    	
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
    	//If the tag is just whitespace ignore it
    	if (tag.trim().length() > 0) {
    		ContentValues values = new ContentValues();
    		//The name of the condition to store the photo under
    		values.put(DatabaseHelper.TAGS_NAME, tag.trim());
    		values.put(DatabaseHelper.PHOTO_FILE, photo);

    		try {
    			db.insert(DatabaseHelper.TAGS_TABLE, null, values);
    		} catch (SQLException e) {
    			//Exceptions are caused by primary key violations.  When that happens, they should be ignored.
    		}
    	}
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
}
