package cs.ualberta.conditionlog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainView extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button takePhotoButton = (Button) findViewById(R.id.TakePhotoButton);
    	takePhotoButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			// add button events here
    		}
    	});
    	
    	Button viewLogButton = (Button) findViewById(R.id.ViewLogsButton);
    	viewLogButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			// add button events here
    		}
    	});
    }
	
}