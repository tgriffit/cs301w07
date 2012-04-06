
package cs.ualberta.conditionlog.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cs.ualberta.conditionlog.R;

/**
 * This activity is the main menu for the application and is the main point in the app that the user returns to after doing anything.
 * @author  jack
 * @uml.dependency  supplier="cs.ualberta.conditionlog.ListSelectionView"
 * @uml.dependency  supplier="cs.ualberta.conditionlog.PhotoUseSelectionView"
 */
public class MainMenuView extends Activity {
	
	static final int NEW_PHOTO = 0;
	static final int RESULT_NOSELECT = 1;
	
	/**
	 * This is the initializer method that creates the objects for the interface and is only called internally
	 */
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
        startActivity(i);
	}

	/**
	 * @uml.property  name="newPhotoView"
	 * @uml.associationEnd  
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

