package cs.ualberta.conditionlog.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.crypto.CipherOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Handles the saving and loading of encrypted files.
 * @author tgriffit
 * @date March 29, 2012
 */

public class FileController {

	/**
	 * Loads the given file and decrypts it.
	 * @param filename - the path to the file
	 */
	public Bitmap load(String filename) {
		//Currently does not decrypt
		return BitmapFactory.decodeFile(filename);
	}
}
