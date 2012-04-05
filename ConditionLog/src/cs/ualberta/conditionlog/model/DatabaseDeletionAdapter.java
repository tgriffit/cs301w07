package cs.ualberta.conditionlog.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DatabaseDeletionAdapter extends DatabaseAdapter {

	public DatabaseDeletionAdapter(Context context) {
		super(context);
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
    	String[] args = {cond, filename};
    	
    	db.delete(DatabaseHelper.COND_TABLE, DatabaseHelper.COND_NAME + "=? AND " + DatabaseHelper.PHOTO_FILE + "=?", args);
    }
    
    /**
     * Removes a tag from the given photo.  If no more photos have that tag,
     * the tag no longer exists.
     * @param filename
     * @param tag
     */
    public void deleteTagFromPhoto(String filename, String tag) {
    	String[] args = {tag, filename};
    	
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
    	deleteMatchingTags(filename);
    	
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
    
    /*
     * Since this version of SQLite doesn't support foreign key constraints,
     * this method is used to delete all instances of a photo from the tags
     * table when it is deleted from the photo table.
     */
    private void deleteMatchingTags(String filename) {
    	String[] args = {filename};
    	
    	db.delete(DatabaseHelper.TAGS_TABLE, DatabaseHelper.PHOTO_FILE + "=?", args);
    
    }
    
    /**
     * Clears the password table.  Used for testing purposes.
     */
    public void deletePassword() {
    	db.delete(DatabaseHelper.PASS_TABLE, "1=1", null);
    }
}
