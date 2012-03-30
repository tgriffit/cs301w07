/**
 * This view class allows a view to be selected and viewed, or allows a condition list to be created.
 * @author adneufel
 * @date March 15th
 */
package cs.ualberta.conditionlog.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cs.ualberta.conditionlog.R;
import cs.ualberta.conditionlog.controller.ListArrayAdapter;
import cs.ualberta.conditionlog.model.ConditionList;
import cs.ualberta.conditionlog.model.DatabaseAdapter;
import cs.ualberta.conditionlog.model.PhotoList;
import cs.ualberta.conditionlog.model.TagList;

/**
 * @author tgriffit
 * @uml.dependency supplier="cs.ualberta.conditionlog.CreateListView"
 */
public class ListSelectionView extends Activity {

	private ArrayList<ArrayList<String>> condLists;
	private ArrayList<ArrayList<String>> tagLists;
	private ArrayList<ArrayList<String>> currentLists;
	// private ArrayList<ArrayList<String>> timeLists;
	/**
	 * @uml.property name="condAdapter"
	 * @uml.associationEnd
	 */
	private ListArrayAdapter condAdapter;
	private ListArrayAdapter tagAdapter;
	private ListView listMenu;
	private String selectedList = null;
	private String listType = "log";
	/**
	 * @uml.property name="dbadapter"
	 * @uml.associationEnd
	 */
	DatabaseAdapter dbadapter;

	private static final int CREATE_LOG = 0;
	private static final int VIEW_LOG = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		updateLists();
		
		Button tagButton = (Button) findViewById(R.id.TagButton);
		tagButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// get the list menu view
				listMenu = (ListView) findViewById(R.id.list);
				// swap the adapter to the one filled with tag info
				listMenu.setAdapter(tagAdapter);
				// get the DeleteButton
				Button deleteButton = (Button) findViewById(R.id.DeleteLogButton);
				// ensure the delete log button is not enabled while the list is sorted by tag
				deleteButton.setEnabled(false);
				swapButtonState();
			}
		});

		Button logButton = (Button) findViewById(R.id.LogButton);
		logButton.setEnabled(false);
		logButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// get the list menu view
				listMenu = (ListView) findViewById(R.id.list);
				// set the adapter to the one filled with cond info
				listMenu.setAdapter(condAdapter);
				swapButtonState();
			}
		});

		// initialize the new log button
		Button newLogButton = (Button) findViewById(R.id.NewLogButton);
		newLogButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startCreateLog();
			}
		});
		
		// initialize the new log button
		Button viewLogButton = (Button) findViewById(R.id.ViewLogButton);
		viewLogButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Context context = getApplicationContext();
				PhotoList list;
				// load the list by the selectedList name
				if (listType.equals("log")) {
					list = new ConditionList(selectedList, context);
				} else {
					// crashes with SQL exception on Taglist constructor
					list = new TagList(selectedList, context);
				}
				
				if (list.getSize() > 0) {
					// start new activity to view the selected condition
					viewList(listType, selectedList);
				} else {
					Toast toast2 = Toast.makeText(context,
							"No photos in that list to view.",
							Toast.LENGTH_SHORT);
					toast2.show();
				}
			}
		});
		
		// initialize the new log button
		Button deleteLogButton = (Button) findViewById(R.id.DeleteLogButton);
		deleteLogButton.setEnabled(false);
		deleteLogButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//maybe have a popup "are you sure" button-thing
				if (listType.equals("log")) {
					dbadapter = new DatabaseAdapter(getApplicationContext());
					dbadapter.open();
					// load the initial data for the list
					dbadapter.deleteCondition(selectedList);
					dbadapter.close();
					updateLists();
				}
			}
		});

		// initialize the list view
		listMenu = (ListView) findViewById(R.id.list);
		listMenu.setOnItemClickListener(new OnItemClickListener() {
			// on list selection return the list name if the list has photos in
			// it. If not, toast a message and do nothing.
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Button deleteLogButton = (Button) findViewById(R.id.DeleteLogButton);
				if (!deleteLogButton.isEnabled() && listType.equals("log"))
					deleteLogButton.setEnabled(true);
				
				ArrayList<String> target = currentLists.get(position);
				// target.get(0) is the name, target.get(1) is a filepath to a
				// thumbnail image
				String listname = target.get(0);
				selectedList = listname;
				setSelectedText(selectedList);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		switch (requestCode) {
		case CREATE_LOG:
			// on good result update the cond list!
			if (resultCode == RESULT_OK) {
				updateLists();
			}
			break;
		case VIEW_LOG:
			// on any result refresh the lists
			updateLists();
			break;
		}
	}

	// create a new activity of CreateListView
	private void startCreateLog() {
		Intent i = new Intent(this, CreateListView.class);
		startActivityForResult(i, CREATE_LOG);
	}
	
	// set the selectedText TextView to show the currently selected lists' name
	private void setSelectedText(String text) {
		TextView selectedText = (TextView) findViewById(R.id.selectedListText);
		selectedText.setText(text);
	}

	// start a ConditionView gallery and pass it relevant info
	private void viewList(String ltype, String lname) {
		Intent i = new Intent(this, ConditionView.class);
		i.putExtra("name", lname);
		i.putExtra("type", ltype);
		startActivityForResult(i, VIEW_LOG);
	}
	
	// update the lists and refresh the view of them
	private void updateLists() {
		dbadapter = new DatabaseAdapter(getApplicationContext());
		dbadapter.open();
		// load the initial data for the list
		condLists = dbadapter.loadConditions();
		tagLists = dbadapter.loadTags();

		dbadapter.close();

		condAdapter = new ListArrayAdapter(this, R.layout.listrow, condLists);
		tagAdapter = new ListArrayAdapter(this, R.layout.listrow, tagLists);
		
		// get the listMenu view
		listMenu = (ListView) findViewById(R.id.list);
		// set the adapter that will be used by the list view
		listMenu.setAdapter(this.condAdapter);
		
		// set the currently focused list
		currentLists = condLists;
		if (currentLists.size() > 0) {
			selectedList = currentLists.get(0).get(0); // get the name of the first list
			setSelectedText(selectedList);
		} else
			setSelectedText("No lists to select");
	}

	// swap button enabled states as well as all relevant list-viewing state variables
	private void swapButtonState() {
		Button tagButton = (Button) findViewById(R.id.TagButton);
		Button logButton = (Button) findViewById(R.id.LogButton);
		
		if(listType.equals("log")) 
			listType = "tag";
		else if (listType.equals("tag"))
			listType="log";
		
		if (currentLists.equals(tagLists))
			currentLists = condLists;
		else if (currentLists.equals(condLists))
			currentLists = tagLists;
		
		selectedList = currentLists.get(0).get(0);
		setSelectedText(selectedList);
		
		tagButton.setEnabled(!tagButton.isEnabled());
		logButton.setEnabled(!logButton.isEnabled());
	}
}
