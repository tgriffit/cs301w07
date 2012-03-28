/**
 * This view class selects or creates a view for the photo to be placed within.
 * @author adneufel
 * @date March 15th
 */
package cs.ualberta.conditionlog.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import cs.ualberta.conditionlog.R;
import cs.ualberta.conditionlog.controller.ListArrayAdapter;
import cs.ualberta.conditionlog.model.DatabaseAdapter;

/**
 * @author         tgriffit
 * @uml.dependency   supplier="cs.ualberta.conditionlog.CreateListView"
 * @uml.dependency   supplier="cs.ualberta.conditionlog.ConditionView"
 */
public class PhotoUseSelectionView extends Activity {
	
	private ArrayList<ArrayList<String>> lists;
	/**
	 * @uml.property  name="m_adapter"
	 * @uml.associationEnd  
	 */
	private ListArrayAdapter m_adapter;
	private ListView listMenu;
	/**
	 * @uml.property  name="dbadapter"
	 * @uml.associationEnd  
	 */
	DatabaseAdapter dbadapter;
	
	private static final int CREATE_LOG = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);
        
        dbadapter = new DatabaseAdapter(getApplicationContext());
        dbadapter.open();
        // load the initial data for the listview from the database
        lists =  dbadapter.loadConditions();
        dbadapter.close();
        // fill m_adapter with values from the database that are in lists
        m_adapter = new ListArrayAdapter(this, R.layout.listrow, lists);
        
        // initialize the newLogButton
        Button newLogButton = (Button) findViewById(R.id.NewLogButton);
        newLogButton.setOnClickListener(new View.OnClickListener() {
			
        	// on click this button will create a new activity that can be used to name and create a new list
			public void onClick(View v) {
				startCreateLog();
			}
		});
        
        // initialize the listview
        listMenu = (ListView) findViewById(R.id.list);
        // set the adapter that will fill the list with the data
        listMenu.setAdapter(this.m_adapter);
        listMenu.setOnItemClickListener(new OnItemClickListener() {
        	// on click the selected list will be returned to the parent activity
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	ArrayList<String> target = lists.get(position);
            	String listname = target.get(0);
        		returnNameFinish(listname);
            }
        });
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
        case CREATE_LOG:
        	String lname = "";
        	if (resultCode == RESULT_OK) {
    			lname = intent.getStringExtra("name");
    			
    			// return the name of the newly created condition list to the parent activity
    			returnNameFinish(lname);
        	} 
            break;
        }
    }
	
	// create a new activity of CreateListView
	private void startCreateLog() {
		Intent i = new Intent(this, CreateListView.class);
        startActivityForResult(i, CREATE_LOG);
	}
	
	// return and pass the selected name through an intent
	private void returnNameFinish(String name) {
		Intent intent = new Intent();
		// pass the name in an intent
		intent.putExtra("name", name);
    	setResult(RESULT_OK, intent);
    	finish();
	}
	
}

