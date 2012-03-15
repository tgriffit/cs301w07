package cs.ualberta.conditionlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainView extends Activity {
	
	static final int NEW_PHOTO = 0;
	
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
    			// add button events here
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
        }
    }
	
	// create a NewPhotoView instance
	private void startNewPhotoView() {
		Intent i = new Intent(this, NewPhotoView.class);
        startActivityForResult(i, NEW_PHOTO);
	}
	
}