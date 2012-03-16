package cs.ualberta.conditionlog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "condition_log.db";
	
	private static final int DATABASE_VERSION = 2;
	
	//Names of tables and columns so that table values can change without affecting code
	public static final String PHOTO_TABLE = "_photo";
	public static final String PHOTO_FILE = "_filename";
	public static final String PHOTO_DATE = "_date_created";
	public static final String COND_TABLE = "_conditions";
	public static final String COND_NAME = "_condition";
	public static final String TAGS_TABLE = "_tags";
	public static final String TAGS_NAME = "_tag";
	
	//Creates a table to store photos if the table does not already exist
	private static final String CREATE_PHOTO_TABLE = "CREATE TABLE IF NOT EXISTS "+ PHOTO_TABLE +" (" +
															PHOTO_FILE + " char(256), " +
															PHOTO_DATE + " date, " +
															"PRIMARY KEY (" + PHOTO_FILE + "));";
	
	//Creates a table to store the conditions if the table does not already exist
	private static final String CREATE_COND_TABLE = "CREATE TABLE IF NOT EXISTS " + COND_TABLE + " (" +
															COND_NAME + " char(50), " +
															PHOTO_FILE + " int, " +
															"PRIMARY KEY (" + COND_NAME + ", " + PHOTO_FILE + "), " +
															"FOREIGN KEY (" + PHOTO_FILE + ") REFERENCES " + PHOTO_TABLE + " ON DELETE CASCADE);";
	
	//Creates a table to store the tags if the table does not already exist
	private static final String CREATE_TAGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TAGS_TABLE + " (" +
															TAGS_NAME + " char(50), " +
															PHOTO_FILE + " int, " +
															"PRIMARY KEY (" + TAGS_NAME + ", " + PHOTO_FILE + "), " +
															"FOREIGN KEY (" + PHOTO_FILE + ") REFERENCES " + PHOTO_TABLE + " ON DELETE CASCADE);";


	/*
	 * Calls the default super constructor and requests the default
	 * cursor factory.
	 */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	@Override
    public void onCreate(SQLiteDatabase db) {
		//Creates each table if they do not already exist
        db.execSQL(CREATE_PHOTO_TABLE);
        db.execSQL(CREATE_COND_TABLE);
        db.execSQL(CREATE_TAGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	//Drops the old tables
    	db.execSQL("DROP TABLE IF EXISTS " + PHOTO_TABLE +";");
    	db.execSQL("DROP TABLE IF EXISTS " + COND_TABLE +";");
    	db.execSQL("DROP TABLE IF EXISTS " + TAGS_TABLE +";");
    	
    	onCreate(db);
    }

}
