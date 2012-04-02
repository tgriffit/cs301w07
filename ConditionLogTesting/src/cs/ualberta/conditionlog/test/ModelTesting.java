package cs.ualberta.conditionlog.test;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

import org.junit.Test;

import android.test.AndroidTestCase;

import cs.ualberta.conditionlog.model.*;

/**
 * Tests the public methods of all the classes in the model package.
 * @author tgriffit
 * @date March 30, 2012
 */

public class ModelTesting extends AndroidTestCase {

	/**
	 * DatabaseAdapter
	 */
	@Test
	public void testDatabaseAdapter(){
		DatabaseAdapter adapter = new DatabaseAdapter(getContext());
		adapter.open();
		
		assert(adapter.loadTags() != null);
		assert(adapter.loadConditions() != null);
		assert(adapter.loadPhotosByTag("tag") != null);
		assert(adapter.loadPhotosByCondition("cond")!= null);
		assert(adapter.loadPhotosByTime() != null);
		try{
			adapter.addCondition("cond");
		}catch(Exception e){
			System.out.print("add condition threw exception\n");
		}
		try{
			adapter.addPhoto("photo", "cond");
		}catch(Exception e){
			System.out.print("add condition threw exception\n");
		}
		try{
			adapter.addTag("photo", "tag");
		}catch(Exception e){
			System.out.print("add condition threw exception\n");
		}
			
		adapter.close();
	}
	

	/**
	 * Tests to ensure the DatabaseHelper is created properly
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testDatabaseHelper(){
		DatabaseHelper helper = new DatabaseHelper(getContext());
		assertEquals("_photo", helper.PHOTO_TABLE);
	}
	
	
	/**
	 * Tests for the EncryptionHelper
	 */
	@Test
	public void testEncryptionHandler() {
		assert(EncryptionHelper.generatePasswordHash("password") != null);
		
		//Initialize the static variables of the encryption handler so further testing can be done
		EncryptionHelper.init("Password");
		
		EncryptionHelper helper = new EncryptionHelper();
		
		//Creates input/output streams that don't connect to anything
		OutputStream out = new PipedOutputStream();
		InputStream in = new PipedInputStream();
		
		assert(helper.getEncryptionStream(out) != null);
		assert(helper.getDecryptionStream(in) != null);
	}
	

	/**
	 * Tests for PhotoLists
	 */
	@Test
	public void testPhotoList(){
		PhotoList list = new PhotoList("name");
		assertEquals("name", list.getName());
		ArrayList<String> filename = new ArrayList<String>();
		list.setFilenames(filename);
		assertEquals(0, list.getSize());
		assert(list.toBmp() != null);
	}
	
	/**
	 * Specific tests for ConditionLists
	 */
	@Test
	public void testConditionList(){
		TagList list = new TagList("cond", getContext());
		assert(list != null);
		assert(list.getName().equals("cond"));
	}
	
	/**
	 * Specific tests for TagLists
	 */
	@Test
	public void testTagList(){
		TagList list = new TagList("tag", getContext());
		assert(list != null);
		assert(list.getName().equals("tag"));
	}
}
