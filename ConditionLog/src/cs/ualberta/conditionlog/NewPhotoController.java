/*
 * author: Andrew Neufeld
 * description: View to create a new image and then select a list to add it to
 * date: March 14
 */

package cs.ualberta.conditionlog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.format.Time;

public class NewPhotoController {
	
	// Wrapper for the static method in BogoPicGen
	protected Bitmap createBogoPic(int width, int height) {
		Bitmap bmp = BogoPicGen.generateBitmap(400,300);
		return bmp;
	}
	
	protected boolean saveBMP(File filepath, Bitmap ourBMP) {
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ourBMP.compress(Bitmap.CompressFormat.JPEG, 75, out);
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	protected File getPicturePath() throws Exception {
		Time now = new Time();
		now.setToNow();
		String timestamp = now.format("%y-%m-%d-%T"); // YY-MM-DD-H:M:S format 24-hour clock time
		File extBaseDir = Environment.getExternalStorageDirectory();
		File file = new File(extBaseDir.getAbsoluteFile() + "/SkinConditionLog");
		if (!file.exists()) {
			if (!file.mkdirs()) {
				throw new Exception("Could not create directories/files for pic: " 
						+ file.getAbsolutePath());
			}
		}
		File filepath = new File(file.getAbsoluteFile() + "/" + timestamp);
		return filepath;
	}
}
