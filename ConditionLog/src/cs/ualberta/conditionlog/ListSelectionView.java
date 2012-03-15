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

public class ListSelectionView extends Activity {
	
	private ArrayList<PhotoList> m_photolists = null;
	private LogArrayAdapter m_adapter;
	private ListView listMenu;
	
	private static final int CREATE_LOG = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
       m_photolists = new ArrayList<PhotoList>();
        
        // set the initial data in the ListView
        newTestList();
        
        m_adapter = new LogArrayAdapter(this, R.layout.listrow, m_photolists);
        
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
            	PhotoList target = m_photolists.get(position);
            	String listname = target.getName();
        		Context context = getApplicationContext();
    			Toast toast = Toast.makeText(context, listname, Toast.LENGTH_SHORT);
    			toast.show();
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
    			
    			// debug info
    			Context context = getApplicationContext();
    			Toast toast = Toast.makeText(context, 
    					"name of new log: " + lname, Toast.LENGTH_LONG);
    			toast.show();
    			
    			returnNameFinish(lname);
        	} 
            break;
        }
    }
	
	private void startCreateLog() {
		Intent i = new Intent(this, CreateListView.class);
        startActivityForResult(i, CREATE_LOG);
	}
	
	private void returnNameFinish(String name) {
		Intent intent = new Intent();
		intent.putExtra("name", name);
    	setResult(RESULT_OK, intent);
    	finish();
	}
	
    /*private void swapButtonState() {
    	Button tagButton = (Button) findViewById(R.id.TagButton);
    	Button logButton = (Button) findViewById(R.id.LogButton);
    	 
    	tagButton.setEnabled(!tagButton.isEnabled());
    	logButton.setEnabled(!logButton.isEnabled());
    }
	
	private void updateList() {
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
	
	// Create a fake list of PhotoList objects in order to test the ListSelectionView user interface.
	private void newTestList() {
		String[] testNames = { "one", "two", "three", "four" };
		m_photolists = new ArrayList<PhotoList>();
		PhotoList p;
		for (int i = 0; i < testNames.length; i++) {
			p = new PhotoList(testNames[i]);
			m_photolists.add(p);
		}
	}
	
	// Create a fake list again, but with slightly different names so as to simulate a tag list
	/*private void newTestTagList() {
		String[] testNames = { "one", "two", "three", "four" };
		m_photolists = new ArrayList<PhotoList>();
		PhotoList p;
		for (int i = 0; i < testNames.length; i++) {
			p = new PhotoList("tag " + testNames[i]);
			m_photolists.add(p);
		}
	} */
}

