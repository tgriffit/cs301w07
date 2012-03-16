/**
 * 
 * @author 
 *
 */

package cs.ualberta.conditionlog;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PhotoList {
	private String name = "";
	private ArrayList<String> filenames = null;
	
	PhotoList(String name) {
		this.setName(name);
		filenames = new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addPhoto(String filename) {
		filenames.add(filename);
	}
	
	public void setFilenames(ArrayList<String> filenames) {
		this.filenames = filenames;
	}
	
	public int getSize() {
		return filenames.size();
	}
	
	public String[] toArray() {
		String[] array = new String[filenames.size()];
		filenames.toArray(array);
		return array;
	}
	
	/**
	 * Loads the image associated with each filename into an array of bitmaps.
	 * @return - an array containing one bitmap for each filename in the list
	 */
	public Bitmap[] toBmp() {
		Bitmap[] bmps = new Bitmap[filenames.size()];
		
		//Iterates through the list of filenames and loads each bmp
		for (int i = 0; i < filenames.size(); i++) {
			bmps[i] = BitmapFactory.decodeFile(filenames.get(i));
		}
		
		return bmps;
	}
	
}