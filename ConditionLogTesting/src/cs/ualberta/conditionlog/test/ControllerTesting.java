package cs.ualberta.conditionlog.test;

import org.junit.Test;

import android.graphics.Bitmap;

import cs.ualberta.conditionlog.controller.*;

public class ControllerTesting {

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
		ListArrayAdapter adapter = new ListArrayAdapter(null, 0, null);
		assert(adapter.getView(0, null, null) != null);
	}
}
