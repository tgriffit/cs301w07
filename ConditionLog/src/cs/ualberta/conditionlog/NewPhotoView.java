package cs.ualberta.conditionlog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewPhotoView extends Activity {
	
	private static final int PHOTO_SELECT = 0;
	private Bitmap ourBMP;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpic);
        
        ImageButton imageButton = (ImageButton) findViewById(R.id.TakeAPhoto);
        imageButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			setBogoPic();
    		}
    	});
        
        Button acceptButton = (Button) findViewById(R.id.Accept);
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
        case PHOTO_SELECT:
        	if (resultCode == RESULT_OK) {
        		// if here then a pic was successfully taken and stored
        		setResult(RESULT_OK);
        		finish();
        	} else {
        		cancelBogoPic();
        	}
            break;
        }
    }

	protected void cancelBogoPic() {
		setResult(RESULT_CANCELED);
		finish();
	}

	protected void acceptBogoPic() {
		Intent intent = new Intent(this, PhotoUseSelectionView.class);
		File filepath = null;
		try {
			filepath = getPicturePath(intent);
		} catch (Exception e) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT);
			toast.show();
		}
		if (filepath != null) {
			saveBMP(filepath, ourBMP);
			intent.putExtra("filename", filepath.getAbsolutePath());
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "starting activity!", Toast.LENGTH_SHORT);
			toast.show();
			// start a new SelectionView instance to select where photo goes
			startActivityForResult(intent, PHOTO_SELECT);
		} 
		// if the above finish() was not executed then something failed. Return bad result.
		//setResult(RESULT_CANCELED, intent);
		//finish();
	}

	private void saveBMP(File filepath, Bitmap ourBMP) {
			OutputStream out;
			try {
				out = new FileOutputStream(filepath);
				ourBMP.compress(Bitmap.CompressFormat.JPEG, 75, out);
				out.close();
			} catch (FileNotFoundException e) {
				setResult(RESULT_CANCELED);
			} catch (IOException e) {
				setResult(RESULT_CANCELED);
			}
			
	}

	private File getPicturePath(Intent intent) throws Exception {
		Time now = new Time();
		now.setToNow();
		String timestamp = now.toString(); // YYYMMDDTHHMMSS format
		File extBaseDir = Environment.getExternalStorageDirectory();
		File file = new File(extBaseDir.getAbsoluteFile() + "/SkinConditionLog");
		if (!file.exists()) {
			if (!file.mkdirs()) {
				throw new Exception("Could not create directories/files for pic: " 
						+ file.getAbsolutePath());
			}
		}
		File filepath = new File(file.getAbsoluteFile() + timestamp);
		return filepath;
	}
	
	protected void setBogoPic() {
		ImageButton imageButton = (ImageButton) findViewById(R.id.TakeAPhoto);
		ourBMP = BogoPicGen.generateBitmap(400,300);
		imageButton.setImageBitmap(ourBMP);
	}
}