/*
 * author: Andrew Neufeld
 * description: View to select or create a list, returns name of list
 * date: March 14
 */

package cs.ualberta.conditionlog;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ListSelectionView extends Activity {
	
	private ArrayList<ArrayList<String>> lists;
	private LogArrayAdapter m_adapter;
	private ListView listMenu;
	DatabaseAdapter dbadapter;
	
	private static final int CREATE_LOG = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        dbadapter = new DatabaseAdapter(getApplicationContext());
        dbadapter.open();
        // set the initial data in the ListView
        
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
        
        Button newLogButton = (Button) findViewById(R.id.NewLogButton);
        newLogButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startCreateLog();
			}
		});
        
        listMenu = (ListView) findViewById(R.id.list);
        listMenu.setAdapter(this.m_adapter);
        listMenu.setOnItemClickListener(new OnItemClickListener() {
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
    			
    			// return the selected lists name to NewPhotoView
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

