package cs.ualberta.conditionlog.test;

import java.util.ArrayList;

import org.junit.Test;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;

import cs.ualberta.conditionlog.controller.*;

public class ControllerTesting extends AndroidTestCase {

	/**
	 * Tests BogoPicGen
	 */
	@Test
	public void testBogoPicGen(){
		Bitmap bmp = BogoPicGen.generateBitmap(10,10);
		assert(bmp.getHeight() > 0);
		assert(bmp.getWidth() > 0);
	}
	
	/**
	 * NewPhotoController
	 */
	@Test
	public void testNewPhotoControllerGet(){
		NewPhotoController controller = new NewPhotoController();
		try {
			assert(controller.getPicturePath() != null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.print("NewPhotoController.getPath threw exception\n");
		}	
	}
	
	/**
	 * LogArrayAdapter
	 */
	@Test
	public void testLogArrayAdapter(){
		ArrayList<ArrayList<String>> testArray = new ArrayList<ArrayList<String>>();
		
		ListArrayAdapter adapter = new ListArrayAdapter(getContext(), 0, testArray);
		assert(adapter.getView(0, null, null) != null);
	}
}
