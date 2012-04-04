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
	 * ImageAdapter
	 */
	@Test
	public void testImageAdapter(){
		Bitmap[] bmps = {BogoPicGen.generateBitmap(10,10), BogoPicGen.generateBitmap(10,10),
				         BogoPicGen.generateBitmap(10,10), BogoPicGen.generateBitmap(10,10)};
		
		ImageAdapter adapter = new ImageAdapter(getContext(), bmps);
		assertEquals(adapter.getCount(), 4);
		assert(adapter.getItem(0) != null);
		assertEquals(adapter.getItemId(0), 0);
	}
	
	/**
	 * ListArrayAdapter
	 */
	@Test
	public void testListArrayAdapter(){
		ArrayList<ArrayList<String>> testArray = new ArrayList<ArrayList<String>>();
		
		ListArrayAdapter adapter = new ListArrayAdapter(getContext(), 0, testArray);
		assert(adapter.getView(0, null, null) != null);
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
	
	/**
	 * PhotoUseListController
	 */
	public void testPhotoUseListController(){
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		ArrayList<String> l = new ArrayList<String>();
		l.add("tag"); 
		l.add("nottag");
		list.add(l);
		
		String[] testlist = PhotoUseListController.getNamesFromListArray(list);
		assertEquals(testlist[0], "tag");
	}
}
