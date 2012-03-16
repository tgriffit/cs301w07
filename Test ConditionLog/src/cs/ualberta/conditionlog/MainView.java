/*
 * author: Andrew Neufeld
 * description: Main opening start view
 * date: March 10
 */

package cs.ualberta.conditionlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainView extends Activity {
	
	static final int NEW_PHOTO = 0;
	static final int LIST_SELECT = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        Button takePhotoButton = (Button) findViewById(R.id.TakePhotoButton);
    	takePhotoButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			startNewPhotoView();
    		}
    	});
    	
    	Button viewLogButton = (Button) findViewById(R.id.ViewLogsButton);
    	viewLogButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			startListSelectView();
    		}
    	});
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
        case NEW_PHOTO:
        	if (resultCode == RESULT_OK) {
        		// if here then a pic was successfully taken, and stored in a condition list
        	}
            break;
        case LIST_SELECT:
        	if (resultCode == RESULT_OK) {
        		// if here then a list was selected
        		String lname = intent.getStringExtra("name");
        		// start a ConditionView concerning this list
        		startConditionView(lname);
        	}
        	break;
        }
    }
	
	// create a NewPhotoView instance
	private void startNewPhotoView() {
		Intent i = new Intent(this, NewPhotoView.class);
        startActivityForResult(i, NEW_PHOTO);
	}
	
	// create a ListSelectionView instance
	private void startListSelectView() {
		Intent i = new Intent(this, ListSelectionView.class);
        startActivityForResult(i, LIST_SELECT);
	}
	
	// create a ConditonView instance to view selected list
	private void startConditionView(String name) {
		Intent i = new Intent(this, ConditionView.class);
		i.putExtra("name", name);
        startActivityForResult(i, LIST_SELECT);
	}
}