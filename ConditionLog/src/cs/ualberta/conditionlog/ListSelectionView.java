package cs.ualberta.conditionlog;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListSelectionView extends ListActivity {
	
	private ArrayList<PhotoList> m_photolists = null;
	private PhotoListAdapter m_adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        m_photolists = new ArrayList<PhotoList>();
        
        // set the initial data in the ListView
        newTestList();
        
        m_adapter = new PhotoListAdapter(this, R.layout.listrow, m_photolists);
        setListAdapter(this.m_adapter);
        
        Button tagButton = (Button) findViewById(R.id.TagButton);
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
		});
    }
	
    private void swapButtonState() {
    	Button tagButton = (Button) findViewById(R.id.TagButton);
    	Button logButton = (Button) findViewById(R.id.LogButton);
    	 
    	tagButton.setEnabled(!tagButton.isEnabled());
    	logButton.setEnabled(!logButton.isEnabled());
    }
	
	private void updateList() {
		m_adapter.clear(); // clear old values
		if(m_photolists != null && m_photolists.size() > 0){
	         m_adapter.notifyDataSetChanged();
	        for(int i=0;i<m_photolists.size();i++)
	         m_adapter.add(m_photolists.get(i)); // add items to the adapter
	    }
	    m_adapter.notifyDataSetChanged();
	 }
	
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
	private void newTestTagList() {
		String[] testNames = { "one", "two", "three", "four" };
		m_photolists = new ArrayList<PhotoList>();
		PhotoList p;
		for (int i = 0; i < testNames.length; i++) {
			p = new PhotoList("tag " + testNames[i]);
			m_photolists.add(p);
		}
	}
}

