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
import android.widget.Toast;
import cs.ualberta.conditionlog.R;
import cs.ualberta.conditionlog.controller.ListArrayAdapter;
import cs.ualberta.conditionlog.model.ConditionList;
import cs.ualberta.conditionlog.model.DatabaseAdapter;

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
	/**
	 * @uml.property name="dbadapter"
	 * @uml.associationEnd
	 */
	DatabaseAdapter dbadapter;

	private static final int CREATE_LOG = 0;

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
				currentLists = tagLists;
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
				currentLists = condLists;
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

		// initialize the list view
		listMenu = (ListView) findViewById(R.id.list);
		listMenu.setOnItemClickListener(new OnItemClickListener() {
			// on list selection return the list name if the list has photos in
			// it. If not, toast a message and do nothing.
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ArrayList<String> target = currentLists.get(position);
				// target.get(0) is the name, target.get(1) is a filepath to a
				// thumbnail image
				String listname = target.get(0);
				Context context = getApplicationContext();
				// load the condition list named listname
				ConditionList list = new ConditionList(listname, context);
				if (list.getSize() > 0) {
					// start new activity to view the selected condition
					viewList(listname);
				} else {
					Toast toast = Toast.makeText(context,
							"No photos in that list to view.",
							Toast.LENGTH_SHORT);
					toast.show();
				}
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
		}
	}

	// create a new activity of CreateListView
	private void startCreateLog() {
		Intent i = new Intent(this, CreateListView.class);
		startActivityForResult(i, CREATE_LOG);
	}

	private void viewList(String lname) {
		Intent i = new Intent(this, ConditionView.class);
		i.putExtra("name", lname);
		startActivity(i);
	}
	
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
	}

	// unused - for use with tags
	private void swapButtonState() {
		Button tagButton = (Button) findViewById(R.id.TagButton);
		Button logButton = (Button) findViewById(R.id.LogButton);

		tagButton.setEnabled(!tagButton.isEnabled());
		logButton.setEnabled(!logButton.isEnabled());
	}
}
