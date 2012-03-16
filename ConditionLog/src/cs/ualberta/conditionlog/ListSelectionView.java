/**
 * This view class allows a view to be selected and viewed, or allows a condition list to be created.
 * @author adneufel
 * @date March 15th
 */
package cs.ualberta.conditionlog;

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

/**
 * @author     tgriffit
 * @uml.dependency   supplier="cs.ualberta.conditionlog.CreateListView"
 */
public class ListSelectionView extends Activity {
	
	private ArrayList<ArrayList<String>> lists;
	/**
	 * @uml.property  name="m_adapter"
	 * @uml.associationEnd  
	 */
	private LogArrayAdapter m_adapter;
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
        setContentView(R.layout.list);
        
        dbadapter = new DatabaseAdapter(getApplicationContext());
        dbadapter.open();
        // load the initial data for the list
        lists =  dbadapter.loadConditions();
        dbadapter.close();
        m_adapter = new LogArrayAdapter(this, R.layout.listrow, lists);
        
        // unused and hidden to hide unusable items on the view
        
        /*Button tagButton = (Button) findViewById(R.id.TagButton);
        tagButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				newTestTagList(); // change to tag list
				updateList();
				swapButtonState();
			}
		});
        
        Button logButton = (Button) findViewById(R.id.LogButton);
        logButton.setEnabled(false);
        logButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				newTestList(); // change to log list
				updateList();
				swapButtonState();
			}
		});*/
        
        // initialize the new log button
        Button newLogButton = (Button) findViewById(R.id.NewLogButton);
        newLogButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startCreateLog();
			}
		});
        
        // initialize the list view 
        listMenu = (ListView) findViewById(R.id.list);
        // set the adapter that will be used by the list view
        listMenu.setAdapter(this.m_adapter);
        listMenu.setOnItemClickListener(new OnItemClickListener() {
        	// on list selection return the list name if the list has photos in it. If not, toast a message and do nothing.
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	ArrayList<String> target = lists.get(position);
            	// target.get(0) is the name, target.get(1) is a filepath to a thumbnail image
            	String listname = target.get(0);
            	Context context = getApplicationContext();
            	// load the condition list named listname
            	ConditionList list = new ConditionList(listname, context);
            	if (list.getSize() > 0) {
            		// send the lsit name back to the parent activity
            		returnNameFinish(listname);
            	} else {
            		Toast toast = Toast.makeText(context, "No photos in that list to view.", Toast.LENGTH_LONG);
            		toast.show();
            	}
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
		// pass the name value inside the intent
		intent.putExtra("name", name);
    	setResult(RESULT_OK, intent);
    	finish();
	}
	
	// unused - for use with tags
    /*private void swapButtonState() {
    	Button tagButton = (Button) findViewById(R.id.TagButton);
    	Button logButton = (Button) findViewById(R.id.LogButton);
    	 
    	tagButton.setEnabled(!tagButton.isEnabled());
    	logButton.setEnabled(!logButton.isEnabled());
    }
	
    // unused - for use with tags and switching between log view and tag view on the list
	/*private void updateList() {
		TextView empty = (TextView) findViewById(R.id.empty);
		m_adapter.clear(); // clear old values
		if(m_photolists != null && m_photolists.size() > 0){
			empty.setVisibility(View.INVISIBLE);
	         m_adapter.notifyDataSetChanged();
	        for(int i=0;i<m_photolists.size();i++)
	         m_adapter.add(m_photolists.get(i)); // add items to the adapter
	    } else {
	    	empty.setVisibility(View.VISIBLE);
	    }
	    m_adapter.notifyDataSetChanged();
	 }*/
	
}

