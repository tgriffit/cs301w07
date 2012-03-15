package cs.ualberta.conditionlog;

import java.util.ArrayList;
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
	
	
	public void open() {
		
	}
	
	/*
     * Adds all filenames corresponding to photos of a given condition
     * to an ArrayList, sorted by the date the photo was created
     */
    public ArrayList<String> loadPhotosByCondition(String cond) {
    	ArrayList<String> photoList = new ArrayList<String>();
    	
    	//Select all photos from photos p, conditions c where p.pid=c.pid and condition='cond';
    	//add each one to photoList
    	
    	return photoList;
    }
    
    /*
     * Adds all filenames corresponding to photos with a given tag to an
     * ArrayList, sorted by the date the photo was created
     */
    public ArrayList<String> loadPhotosByTag(String tag) {
    	ArrayList<String> photoList = new ArrayList<String>();
    	
    	//Select all photos from photos p, tags t where p.pid=t.pid and tag='tag';
    	//for each one found, call PhotoList.add()
    	
    	return photoList;
    }
    
    /*
     * Returns a list containing all conditions added to the database so far
     */
    public ArrayList<String> listConditions() {
    	ArrayList<String> conds = new ArrayList<String>();
    	
    	//select cond from conditions group by cond
    	//add each to conds
    	
    	return conds;
    }
    
    /*
     * Adds a photo to the database
     */
    public void addPhotoToDB(String filename) {
    	//selects greatest pid from the photos table
    	//inserts new entry with pid = greatest_pid + 1, date = current time
    }

}
