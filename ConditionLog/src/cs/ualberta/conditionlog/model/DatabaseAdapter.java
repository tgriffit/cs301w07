package cs.ualberta.conditionlog.model;

import android.content.Context;
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
	 * @uml.property  name="dbHelper"
	 * @uml.associationEnd  
	 */
	protected DatabaseHelper dbHelper;
	protected SQLiteDatabase db;
	
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
}
