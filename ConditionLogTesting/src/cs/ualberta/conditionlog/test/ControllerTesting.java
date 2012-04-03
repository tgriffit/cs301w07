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
	
	/**
	 * Tests PasswordManager without deleting the old password
	 */
	@Test
	public void testPasswordManager(){
		PasswordManager manager = new PasswordManager(getContext());
		String oldPass = "";
		//save old password
		if (manager.checkIfPasswordSet())
			oldPass = manager.getHashForTesting();
		
		manager.resetPassword();
		assertFalse(manager.checkIfPasswordSet());
		
		manager.setPassword("password");
		assertTrue(manager.checkIfPasswordSet());
		assertTrue(manager.testPassword("password"));
		manager.resetPassword();
		
		//restore old password
		if (!oldPass.equals(""))
			manager.setHashAfterTesting(oldPass);
	}
}
