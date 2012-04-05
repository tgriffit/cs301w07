
package cs.ualberta.conditionlog.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cs.ualberta.conditionlog.R;

/**
 * @uml.dependency   supplier="cs.ualberta.conditionlog.PhotoUseSelectionView"
 * @uml.dependency   supplier="cs.ualberta.conditionlog.ListSelectionView"
 */
public class MainMenuView extends Activity {
	
	static final int NEW_PHOTO = 0;
	static final int RESULT_NOSELECT = 1;
	
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

