package cs.ualberta.conditionlog;

import java.util.ArrayList;

import android.content.Context;

public class ConditionList extends PhotoList {

	private String name = "";
	private ArrayList<String> filenames = null;
	
	ConditionList(String name) {
		super(name);
	}
	
	/*
	 * Loads photos into the list that match the list's condition
	 */
	public void loadPhotos(Context context) {
		//Loads each photo that matches the condition name into the list
		DatabaseAdapter dbA = new DatabaseAdapter(context);
		dbA.open();
		filenames = dbA.loadPhotosByCondition(name);
		dbA.close();
	}

}
