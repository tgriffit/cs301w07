package cs.ualberta.conditionlog.model;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * This class stores an ArrayList consisting of the filepaths of photos.
 * There are two classes that extend PhotoList.  Both ConditionList and TagList
 * use most of PhotoList's methods, but they have different constructors that
 * load photos from their respective databases.
 * @author tgriffit
 * @date March 15, 2012
 *
 */
public class PhotoList {
	/**
	 * @uml.property  name="name"
	 */
	private String name = "";
	private ArrayList<String> filenames = null;
	
	/**
	 * Constructor that initializes the instance variables.
	 * @param name - the condition or tag for the list
	 */
	public PhotoList(String name) {
		this.name = name;
		filenames = new ArrayList<String>();
	}
	
	/**
	 * Returns the name of the list, ie. the condition or tag it is associated with
	 * @return   - the name as a string
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Used to load a list of file names into the ArrayList
	 * @param filenames   - a list containing filepaths to photos
	 * @uml.property  name="filenames"
	 */
	public void setFilenames(ArrayList<String> filenames) {
		this.filenames = filenames;
	}
	
	/**
	 * Returns the number of photos stored in the list
	 * @return - the size of the ArrayList
	 */
	public int getSize() {
		return filenames.size();
	}
	
	/**
	 * Loads the image associated with each filename into an array of bitmaps.
	 * @return - an array containing one bitmap for each filename in the list
	 */
	public Bitmap[] toBmp() {
		Bitmap[] bmps = new Bitmap[filenames.size()];
		
		//Iterates through the list of filenames and loads each bmp
		for (int i = 0; i < filenames.size(); i++)
			bmps[i] = EncryptionHelper.loadBMP(filenames.get(i));
		
		return bmps;
	}
	
	public String getFileName(int position) {
		return filenames.get(position);
	}
	
	public void deletePhoto(Context context, int position) {
		DatabaseDeletionAdapter dbA = new DatabaseDeletionAdapter(context);
		dbA.open();
		dbA.deletePhoto(getFileName(position));
		dbA.close();
	}
	
	public void deleteTagFromPhoto(Context context, int position, String tag) {
		DatabaseDeletionAdapter dbA = new DatabaseDeletionAdapter(context);
		dbA.open();
		dbA.deleteTagFromPhoto(getFileName(position), tag);
		dbA.close();
	}
	
	public void addTagToPhoto(Context context, int position, String tag) {
		DatabaseInputAdapter dbA = new DatabaseInputAdapter(context);
		dbA.open();
		dbA.addTag(getFileName(position), tag);
		dbA.close();
	}
	
	public String getTimestamp(String filename, Context context){
		DatabaseOutputAdapter dba = new DatabaseOutputAdapter(context);
		dba.open();
		String timestamp = dba.getPhotoTimestamp(filename);
		dba.close();
		return timestamp;
	}
	
	public ArrayList<String> getTags(String filename, Context context){
		DatabaseOutputAdapter dba = new DatabaseOutputAdapter(context);
		dba.open();
		ArrayList<String> tags = dba.loadTagsForPhoto(filename);
		dba.close();
		
		return tags;
	}

	
}