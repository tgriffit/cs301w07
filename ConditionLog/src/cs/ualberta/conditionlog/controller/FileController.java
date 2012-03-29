package cs.ualberta.conditionlog.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Handles the saving and loading of encrypted files.
 * @author tgriffit
 * @date March 29, 2012
 */

public class FileController {

	/**
	 * Encrypts the bmp and saves it.
	 * @param bmp - an image to save
	 */
	public void save(Bitmap bmp) {
		
	}
	
	/**
	 * Loads the given file and decrypts it.
	 * @param filename - the path to the file
	 */
	public Bitmap load(String filename) {
		//Currently does not decrypt
		return BitmapFactory.decodeFile(filename);
	}
}
