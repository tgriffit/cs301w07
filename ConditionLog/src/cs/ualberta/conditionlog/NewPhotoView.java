/*
 * author: Andrew Neufeld
 * description: View to capture new photo and delegate what list to add it to.
 * date: March 14
 */

package cs.ualberta.conditionlog;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * This view handles the creation of new photos and then adds them to a condition list.
 * @author adneufel
 *
 */
public class NewPhotoView extends Activity {
	
	private static final int PHOTO_USE = 0;
	private File bmpFilepath;
	private Bitmap bmp;
	private NewPhotoController controller;
	
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
        case PHOTO_USE:
        	String lname = "";
        	File filepath = getBmpFilepath();
        	
        	if (resultCode == RESULT_OK) {
    			lname = intent.getStringExtra("name");
    			DatabaseAdapter dbadapter = new DatabaseAdapter(getApplicationContext());
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
	
	private void setBmpFilepath(File file) {
		this.bmpFilepath = file;
	}
	
	private File getBmpFilepath() {
		return this.bmpFilepath;
	}
	
	private void setController(NewPhotoController cntrl) {
		this.controller = cntrl;
	}
	
	private NewPhotoController getController() {
		return this.controller;
	}
	
}
