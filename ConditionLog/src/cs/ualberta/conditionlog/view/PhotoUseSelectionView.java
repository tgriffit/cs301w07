/**
 * This view class selects or creates a view for the photo to be placed within.
 * @author adneufel
 * @date March 15th
 */
package cs.ualberta.conditionlog.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import cs.ualberta.conditionlog.R;
import cs.ualberta.conditionlog.controller.PhotoUseListController;
import cs.ualberta.conditionlog.model.DatabaseOutputAdapter;

/**
 * @author           tgriffit
 * @uml.dependency   supplier="cs.ualberta.conditionlog.CreateListView"
 * @uml.dependency   supplier="cs.ualberta.conditionlog.ConditionView"
 */
public class PhotoUseSelectionView extends Activity {
	
	private ArrayList<ArrayList<String>> lists;
	/**
	 * @uml.property  name="m_adapter"
	 * @uml.associationEnd  
	 */
	private ArrayAdapter<String> m_adapter;
	/**
	 * @uml.property  name="dbadapter"
	 * @uml.associationEnd  
	 */
	//private DatabaseAdapter dbadapter;
	private String[] namesList;
	private String picFile;
	private String selectedList = null;
	
	private EditText tagBox;
	
	private static final int CREATE_LOG = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);
        
        Intent i = getIntent();
        picFile = i.getStringExtra("filename");

        tagBox = (EditText) findViewById(R.id.tagBox);
        
        Spinner listMenu = (Spinner) findViewById(R.id.spinner);
        updateSpinner();
        listMenu.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        ImageView photo = (ImageView) findViewById(R.id.PhotoUsePreview);
        photo.setImageBitmap(BitmapFactory.decodeFile(picFile));
        
        // initialize the newLogButton
        Button newLogButton = (Button) findViewById(R.id.NewPhotoLogButton);
        newLogButton.setOnClickListener(new View.OnClickListener() {
			
        	// on click this button will create a new activity that can be used to name and create a new list
			public void onClick(View v) {
				// save the tags
				//String[] tags = tagBox.getText().toString().split("\\s+");
				//PhotoUseListController.savePhotoTags(getApplicationContext(), picFile, tags);
				// and then start the CreateLog activity
				startCreateLog();
			}
		});
        
        Button saveButton = (Button) findViewById(R.id.SavePhotoButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
			
        	// on click this button will create a new activity that can be used to name and create a new list
			public void onClick(View v) {
				if (selectedList == null) {
					Toast toast = Toast.makeText(getApplicationContext(), "Select or create a condition log", Toast.LENGTH_SHORT);
					toast.show();
					return;
				}
				
				String rawText = tagBox.getText().toString();
				if (!rawText.equals("")) {
					String[] tags = rawText.split("\\s+");
					PhotoUseListController.savePhotoTags(getApplicationContext(), picFile, tags);
				}
				
				// and now return the list name and finish
				if (selectedList != null) {
					returnNameFinish(selectedList);
				} else {
					// get first name from the list to return as the default value if no new list is selected
					String defaultName = namesList[0];
					returnNameFinish(defaultName);
				}
			}
		});
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
        case CREATE_LOG:
        	if (resultCode == RESULT_OK) {
    			// update spinner to add newly created list to the spinner menu
        		updateSpinner();
        	} 
            break;
        }
    }
	
	private void updateSpinner() {
        DatabaseOutputAdapter dbadapter = new DatabaseOutputAdapter(getApplicationContext());
        dbadapter.open();
        // load the initial data for the listview from the database
        lists =  dbadapter.loadConditions();
        dbadapter.close();
        // fill m_adapter with values from the database that are in lists
        namesList = PhotoUseListController.getNamesFromListArray(lists);
		// initialize the listview
        Spinner listMenu = (Spinner) findViewById(R.id.spinner);
        // set the adapter that will fill the list with the data
        m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namesList);
        m_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listMenu.setAdapter(this.m_adapter);
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
	
	// tiny class needed for the spinner item selection
	private class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	// get tags from the EditText 
	    	selectedList = parent.getItemAtPosition(pos).toString();
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    	// do nothing
	    }
	}
}

