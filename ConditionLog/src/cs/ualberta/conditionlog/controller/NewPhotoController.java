

package cs.ualberta.conditionlog.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cs.ualberta.conditionlog.model.EncryptionHelper;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.format.Time;

/**
 * A controller for the NewPhotoView activity class that helps handle photos
 * @author  Andrew Neufeld
 * @uml.dependency  supplier="cs.ualberta.conditionlog.BogoPicGen"
 */
public class NewPhotoController extends BogoPicGen {

	/**
	 * Returns a created Bitmap of width x height. This is a wrapper function
	 * for the BogoPicGen and it's static method to create an image
	 * 
	 * @param width
	 *            the to-be-created image width size
	 * @param height
	 *            the to-be-created image height size
	 * @return Bitmap
	 * @see Bitmap
	 */
	public Bitmap createBogoPic(int width, int height) {
		Bitmap bmp = BogoPicGen.generateBitmap(width, height);
		return bmp;
	}

	/**
	 * Save the given Bitmap to the given File filepath. Returns true if
	 * successful and false if unsuccessful.
	 * 
	 * @param filepath filepath to the picture
	 * @param ourBMP bitmap of the picture
	 * @return boolean
	 */
	public boolean saveBMP(File filepath, Bitmap ourBMP) {
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ourBMP.compress(Bitmap.CompressFormat.PNG, 100, out);
			
			//Encrypts the output stream
			EncryptionHelper encrypt = new EncryptionHelper();
			out = encrypt.getEncryptionStream(out);
			
			out.flush();
			out.close();
			return true;

		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Returns a File with a filepath that is created from the current time.
	 * 
	 * @return File File path to the image
	 * @throws Exception Exception upon error in creating file/directories for the picture
	 */
	public File getPicturePath() throws Exception {
		Time now = new Time();
		now.setToNow();
		String timestamp = now.format("%y-%m-%d-%H-%M-%S"); // YY-MM-DD-H:M:S format
														// 24-hour clock time
		File extBaseDir = Environment.getExternalStorageDirectory();
		File file = new File(extBaseDir.getAbsolutePath() + "/SkinConditionLog");
		if (!file.exists()) {
			if (!file.mkdirs()) {
				throw new Exception(
						"Could not create directories/files for pic: "
								+ file.getAbsolutePath());
			}
		}
		File filepath = new File(file.getAbsolutePath() + "/" + timestamp);
		return filepath;
	}
}
