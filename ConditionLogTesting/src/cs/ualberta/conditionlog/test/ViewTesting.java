package cs.ualberta.conditionlog.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import android.test.AndroidTestCase;

import cs.ualberta.conditionlog.view.*;

public class ViewTesting extends AndroidTestCase{

	/**
	 * MainView
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testMainVeiw(){
		MainView mainView = new MainView();
		//assertEquals(0, mainView.NEW_PHOTO);
	}

	
	/**
	 * ListSelectionView
	 */
	@Test
	public void testListSelectionView(){
		ListSelectionView view = new ListSelectionView();
		//assert(view.dbadapter != null);
	}
	
}
