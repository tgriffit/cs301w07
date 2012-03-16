package cs.ualberta.conditionlog;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;


public class ConditionPublicMethodTesting {
	
	/**
	 * MainView
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testMainVeiw(){
		MainView mainView = new MainView();
		assertEquals(0, mainView.NEW_PHOTO);
		
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
	 * PhotoList
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
	 * PhotoListAdapter
	 */
	
	@Test
	public void testPhotoListAdapter(){
		PhotoListAdapter adapter = new PhotoListAdapter(null, 0, null);
		assert(adapter.getView(0, null, null) != null);
	}
	
	/*(
	 * LogArrayAdapter
	 */
	
	@Test
	public void testLogArrayAdapter(){
		LogArrayAdapter adapter = new LogArrayAdapter(null, 0, null);
		assert(adapter.getView(0, null, null) != null);
	}
	
	/**
	 * ListSelectionView
	 */
	
	@Test
	public void testListSelectionView(){
		ListSelectionView view = new ListSelectionView();
		assert(view.dbadapter != null);
	}
	
	/**
	 * DatabaseHelper
	 */
	
	@SuppressWarnings("static-access")
	@Test
	public void testDatabaseHelper(){
		DatabaseHelper helper = new DatabaseHelper(null);
		assertEquals("_photo", helper.PHOTO_TABLE);
		
	}
	
	/**
	 * DatabaseAdapter
	 */
	
	@Test
	public void testDatabaseAdapter(){
		DatabaseAdapter adapter = new DatabaseAdapter(null);
		adapter.open();
		assert(adapter.loadTags() != null);
		assert(adapter.loadPhotosByTag("tag") != null);
		assert(adapter.loadPhotosByCondition("cond")!= null);
		assert(adapter.loadConditions() != null);
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
		
	}
	

	
}


