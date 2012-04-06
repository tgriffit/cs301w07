package cs.ualberta.conditionlog.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

import org.junit.Test;

import android.test.AndroidTestCase;
import cs.ualberta.conditionlog.model.DatabaseDeletionAdapter;
import cs.ualberta.conditionlog.model.DatabaseHelper;
import cs.ualberta.conditionlog.model.DatabaseInputAdapter;
import cs.ualberta.conditionlog.model.DatabaseOutputAdapter;
import cs.ualberta.conditionlog.model.EncryptionHelper;
import cs.ualberta.conditionlog.model.PhotoList;
import cs.ualberta.conditionlog.model.TagList;

/**
 * Tests the public methods of all the classes in the model package.
 * @author tgriffit
 * @date March 30, 2012
 */

public class ModelTesting extends AndroidTestCase {

	/**
	 * Tests for DatabaseInputAdapter
	 */
	@Test
	public void testDatabaseInputAdapter(){
		DatabaseInputAdapter adapter = new DatabaseInputAdapter(getContext());

		adapter.open();
		
		try{
			adapter.addCondition("cond");
		}catch(Exception e){
			System.out.print("add condition threw exception\n");
		}
		try{
			adapter.addPhoto("photo", "cond");
		}catch(Exception e){
			System.out.print("add photo threw exception\n");
		}
		try{
			adapter.addTag("photo", "tag");
		}catch(Exception e){
			System.out.print("add tag threw exception\n");
		}
		
		adapter.close();
	}
	
	/**
	 * Tests for DatabaseOutputAdapter
	 */
	@Test
	public void testDatabaseOutputAdapter(){
		DatabaseOutputAdapter adapter = new DatabaseOutputAdapter(getContext());
		adapter.open();
		
		assert(adapter.loadTags() != null);
		assert(adapter.loadConditions() != null);
		assert(adapter.loadPhotosByTag("tag") != null);
		assert(adapter.loadPhotosByCondition("cond")!= null);
		assert(adapter.loadPhotosByTime() != null);
		
		adapter.close();
	}
	
	/**
	 * Tests for DatabaseDeletionAdapter
	 */
	@Test
	public void testDatabaseDeletionAdapter(){
		DatabaseDeletionAdapter adapter = new DatabaseDeletionAdapter(getContext());
		adapter.open();
		
		try{
			adapter.deleteCondition("cond");
		}catch(Exception e){
			System.out.print("delete condition threw exception\n");
		}
		try{
			adapter.deletePhoto("photo");
		}catch(Exception e){
			System.out.print("delete photo threw exception\n");
		}
		try{
			adapter.deleteTagFromPhoto("photo", "tag");
		}catch(Exception e){
			System.out.print("delete tag from photo threw exception\n");
		}
		
		adapter.close();
	}

	/**
	 * Tests to ensure the DatabaseHelper constructs properly
	 */
	@Test
	public void testDatabaseHelper(){
		try {
			DatabaseHelper helper = new DatabaseHelper(getContext());
		} catch (Exception e) {
			System.out.println("DatabaseHelper did not constuct properly");
		}
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
		
		String hash1 = EncryptionHelper.generatePasswordHash("password");
		String hash2 = EncryptionHelper.generatePasswordHash("password");
		String hash3 = EncryptionHelper.generatePasswordHash("notpassword");
		assertEquals(hash1, hash2);
		assertFalse(hash1.equals(hash3));
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
