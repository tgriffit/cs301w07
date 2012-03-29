/**
* Controls the ConditionLog's database, allowing connections to be made with it.
* @author tgriffit
* @date March 15, 2012
*/

package cs.ualberta.conditionlog.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

private static final String DB_NAME = "condition_log.db";

private static final int DATABASE_VERSION = 2;

//Names of tables and columns so that table values can change without affecting code
public static final String PHOTO_TABLE	= "_photo";
public static final String PHOTO_FILE	= "_filename";
public static final String PHOTO_DATE 	= "_date_created";
public static final String COND_TABLE	= "_conditions";
public static final String COND_NAME	= "_condition";
public static final String TAGS_TABLE	= "_tags";
public static final String TAGS_NAME	= "_tag";
public static final String PASS_TABLE	= "_password";
public static final String PASS			= "_pass";
public static final String COND_TRIGGER = "_cond_trigger";

//Creates a table to store photos if the table does not already exist
private static final String CREATE_PHOTO_TABLE = "CREATE TABLE IF NOT EXISTS "+ PHOTO_TABLE +" (" +
												  PHOTO_FILE + " char(256), " +
												  PHOTO_DATE + " date, " +
												 "PRIMARY KEY (" + PHOTO_FILE + "));";

//Creates a table to store the conditions if the table does not already exist
private static final String CREATE_COND_TABLE = "CREATE TABLE IF NOT EXISTS " + COND_TABLE + " (" +
												 COND_NAME + " char(50), " +
												 PHOTO_FILE + " char(256), " +
												"PRIMARY KEY (" + COND_NAME + ", " + PHOTO_FILE + "), " +
												"FOREIGN KEY (" + PHOTO_FILE + ") REFERENCES " + PHOTO_TABLE + " ON DELETE CASCADE);";

//Creates a table to store the tags if the table does not already exist
private static final String CREATE_TAGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TAGS_TABLE + " (" +
												 TAGS_NAME + " char(50), " +
												 PHOTO_FILE + " char(256), " +
												"PRIMARY KEY (" + TAGS_NAME + ", " + PHOTO_FILE + "), " +
												"FOREIGN KEY (" + PHOTO_FILE + ") REFERENCES " + PHOTO_TABLE + " ON DELETE CASCADE);";

//Creates a table to store a password hash
private static final String CREATE_PASS_TABLE = "CREATE TABLE IF NOT EXISTS " + PASS_TABLE + " (" +
												 PASS + " char(50));";

//When a condition's last photo is deleted the condition is left with a null photo instead of deleting it
private static final String CREATE_COND_TRIGGER = "CREATE TRIGGER IF NOT EXISTS " + COND_TRIGGER + " " +
												  "BEFORE DELETE ON COND_TABLE " +
												  "DECLARE deleted_cond char(50);" +
												  "BEGIN " +
												  		"SELECT c." + COND_NAME + " into deleted_cond " +
												  		"FROM old.COND_TABLE as c " +
												  		"WHERE c." + COND_NAME + " NOT IN (SELECT " + COND_NAME +
										   								" FROM new." + COND_TABLE + ") " +
										   				"INSERT INTO " + COND_TABLE + " VALUES(deleted_cond, null); " +
										   		   "END";

/**
* Calls the default super constructor and requests the default
* cursor factory.
* @param context - the current application context
*/
public DatabaseHelper(Context context) {
super(context, DB_NAME, null, DATABASE_VERSION);
}

/**
* Overrides the default onCreate. When the DatabaseHelper is created it
* creates all of the tables used if they do not already exist.
*/
@Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PHOTO_TABLE);
        db.execSQL(CREATE_COND_TABLE);
        db.execSQL(CREATE_TAGS_TABLE);
        db.execSQL(CREATE_PASS_TABLE);
        db.execSQL(CREATE_COND_TRIGGER);
    }

/**
* Overrides the default onUpgrade. If the database version is changed, all the tables
* are dropped and re-created.
*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL("DROP TABLE IF EXISTS " + PHOTO_TABLE + ";");
     db.execSQL("DROP TABLE IF EXISTS " + COND_TABLE + ";");
     db.execSQL("DROP TABLE IF EXISTS " + TAGS_TABLE + ";");
     db.execSQL("DROP TABLE IF EXISTS " + PASS_TABLE + ";");
     db.execSQL("DROP TRIGGER IF EXISTS " + COND_TRIGGER + ";");
    
     onCreate(db);
    }

}