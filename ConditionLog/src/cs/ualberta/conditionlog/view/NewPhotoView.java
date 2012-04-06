package cs.ualberta.conditionlog.view;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import cs.ualberta.conditionlog.R;
import cs.ualberta.conditionlog.controller.NewPhotoController;
import cs.ualberta.conditionlog.model.DatabaseInputAdapter;

/**
 * This view handles the creation of new photos and then opens another activity that allows the photo use to be selected.
 * @author   adneufel
 */
public class NewPhotoView extends Activity {
	
	private static final int PHOTO_USE = 0;
	/**
	 * @uml.property  name="bmpFilepath"
	 */
	private File bmpFilepath;
	private Bitmap bmp;
	/**
	 * @uml.property  name="controller"
	 * @uml.associationEnd  
	 */
	private NewPhotoController controller;
	
	/**
	 * This is the initializer method that creates the interface objects and is only called internally
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpic);
        
        setController(new NewPhotoController());
        
        ImageButton imageButton = (ImageButton) findViewById(R.id.TakeAPhoto);
        imageButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			Button acceptButton = (Button) findViewById(R.id.Accept);
    			newBogoPic();
    			acceptButton.setEnabled(true);
    		}
    	});
        
        Button acceptButton = (Button) findViewById(R.id.Accept);
        acceptButton.setEnabled(false);
        acceptButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			acceptBogoPic();
    		}
    	});
        
        Button cancelButton = (Button) findViewById(R.id.Cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			cancelBogoPic();
    		}
    	});
    }
    
    /**
     * This method is called when a sub-activity finishes and returns back here. Then it deals with the returns data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
        case PHOTO_USE:
        	String lname = "";
        	File filepath = getBmpFilepath();
        	
        	if (resultCode == RESULT_OK) {
    			lname = intent.getStringExtra("name");
    			DatabaseInputAdapter dbadapter = new DatabaseInputAdapter(getApplicationContext());
    			dbadapter.open();
    			dbadapter.addPhoto(filepath.getAbsolutePath(), lname);
    			dbadapter.close();
    			
    			setResult(RESULT_OK);
        		finish();
        	} 
            break;
        }
    }
    
    private void newBogoPic() {
		NewPhotoController controller = getController();
		bmp = controller.createBogoPic(400, 400);
		setBogoPic(bmp);
	}

	private void cancelBogoPic() {
		setResult(RESULT_CANCELED);
		finish();
	}

	private void acceptBogoPic() {
		Intent intent = new Intent(this, PhotoUseSelectionView.class);
		NewPhotoController controller = getController();
		File filepath;
		try {
			filepath = controller.getPicturePath();
			setBmpFilepath(filepath);
			controller.saveBMP(filepath, bmp);
			
			intent.putExtra("filename", filepath.getAbsolutePath());
			
			// start a new SelectionView instance to select where photo goes
			startActivityForResult(intent, PHOTO_USE);
		} catch (Exception e) {
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	
	private void setBogoPic(Bitmap bmp) {
		ImageButton imageButton = (ImageButton) findViewById(R.id.TakeAPhoto);
		imageButton.setImageBitmap(bmp);
	}
	
	/**
	 * @param  file
	 * @uml.property  name="bmpFilepath"
	 */
	private void setBmpFilepath(File file) {
		this.bmpFilepath = file;
	}
	
	/**
	 * @return
	 * @uml.property  name="bmpFilepath"
	 */
	private File getBmpFilepath() {
		return this.bmpFilepath;
	}
	
	/**
	 * @param  cntrl
	 * @uml.property  name="controller"
	 */
	private void setController(NewPhotoController cntrl) {
		this.controller = cntrl;
	}
	
	/**
	 * @return
	 * @uml.property  name="controller"
	 */
	private NewPhotoController getController() {
		return this.controller;
	}
	
}
