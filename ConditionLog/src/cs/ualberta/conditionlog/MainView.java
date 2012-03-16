/**
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

/**
 * @uml.dependency   supplier="cs.ualberta.conditionlog.PhotoUseSelectionView"
 * @uml.dependency   supplier="cs.ualberta.conditionlog.ListSelectionView"
 */
public class MainView extends Activity {
	
	static final int NEW_PHOTO = 0;
	static final int LIST_SELECT = 1;
	static final int RESULT_NOSELECT = 2;
	
	@Override
	/**
	 * adds the buttons to the main view, sets listeners
	 */
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
	/**
	 * on result, checks if a picture was taken or a list was selected and responds accordingly
	 */
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
	
	/**
	 * starts a new Photo view
	 */
	private void startNewPhotoView() {
		Intent i = new Intent(this, NewPhotoView.class);
        startActivityForResult(i, NEW_PHOTO);
	}
	
	/**
	 * create a ListSelectionView instance
	 */
	private void startListSelectView() {
		Intent i = new Intent(this, ListSelectionView.class);
        startActivityForResult(i, LIST_SELECT);
	}
	
	/**
	 * create a ConditonView instance to view selected list
	 * @param name	a name to be passed to the condition view for which condition
	 */
	private void startConditionView(String name) {
		Intent i = new Intent(this, ConditionView.class);
		i.putExtra("name", name);
        startActivityForResult(i, LIST_SELECT);
	}

	/**
	 * @uml.property  name="newPhotoView"
	 * @uml.associationEnd  inverse="mainView:cs.ualberta.conditionlog.NewPhotoView"
	 */
	private NewPhotoView newPhotoView;

	/**
	 * Getter of the property <tt>newPhotoView</tt>
	 * @return  Returns the newPhotoView.
	 * @uml.property  name="newPhotoView"
	 */
	public NewPhotoView getNewPhotoView() {
		return newPhotoView;
	}

	/**
	 * Setter of the property <tt>newPhotoView</tt>
	 * @param newPhotoView  The newPhotoView to set.
	 * @uml.property  name="newPhotoView"
	 */
	public void setNewPhotoView(NewPhotoView newPhotoView) {
		this.newPhotoView = newPhotoView;
	}
}