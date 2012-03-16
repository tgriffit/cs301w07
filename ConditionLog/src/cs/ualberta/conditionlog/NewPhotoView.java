<<<<<<< HEAD
package cs.ualberta.conditionlog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewPhotoView extends Activity {
	
	private static final int LIST_SELECT = 0;
	private Bitmap ourBMP;
	private Button acceptButton;
	private ImageButton imageButton;
	private Button cancelButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpic);
        
        imageButton = (ImageButton) findViewById(R.id.TakeAPhoto);
        imageButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			setBogoPic();
    			acceptButton.setEnabled(true);
    		}
    	});
        
        acceptButton = (Button) findViewById(R.id.Accept);
        acceptButton.setEnabled(false);
        acceptButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			acceptBogoPic();
    		}
    	});
        
        cancelButton = (Button) findViewById(R.id.Cancel);
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
        case LIST_SELECT:
        	String lname = "";
        	if (resultCode == RESULT_OK) {
    			lname = intent.getStringExtra("name");
    			
    			// debug info
    			Context context = getApplicationContext();
    			Toast toast = Toast.makeText(context, 
    					"photo would have been added to list: " + lname, Toast.LENGTH_LONG);
    			toast.show();
    			
    			// add picture to selected list!
    			// need storage functions...
    			
    			setResult(RESULT_OK);
        		finish();
        	} 
            break;
        }
    }

	protected void cancelBogoPic() {
		setResult(RESULT_CANCELED);
		finish();
	}

	protected void acceptBogoPic() {
		Intent intent = new Intent(this, ListSelectionView.class);
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
			// start a new SelectionView instance to select where photo goes
			startActivityForResult(intent, LIST_SELECT);
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
				
				//Insert the photo's information into the database
				DatabaseAdapter dbA = new DatabaseAdapter(getApplicationContext());
				dbA.open();
				dbA.addPhotoToDB(filepath.getAbsolutePath());
				dbA.close();
				
			} catch (FileNotFoundException e) {
				setResult(RESULT_CANCELED);
			} catch (IOException e) {
				setResult(RESULT_CANCELED);
			} catch (SQLException e) {
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
=======
/*
 * author: Andrew Neufeld
 * description: View to capture new photo and delegate what list to add it to.
 * date: March 14
 */

package cs.ualberta.conditionlog;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewPhotoView extends Activity {
	
	private static final int LIST_SELECT = 0;
	private File bmpFilepath;
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
    
    private void newBogoPic() {
		NewPhotoController controller = getController();
		Bitmap bmp = controller.createBogoPic(400, 300);
		setBogoPic(bmp);
		try {
			File filepath = controller.getPicturePath();
			setBmpFilepath(filepath);
			controller.saveBMP(filepath, bmp);
		} catch (Exception e) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT);
			toast.show();
			e.printStackTrace();
		}
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
        case LIST_SELECT:
        	String lname = "";
        	if (resultCode == RESULT_OK) {
    			lname = intent.getStringExtra("name");
    			
    			// debug info
    			Context context = getApplicationContext();
    			Toast toast = Toast.makeText(context, 
    					"photo: " + getBmpFilepath().getAbsolutePath() + 
    					" would have been added to list: " + lname, 250000);
    			toast.show();
    			
    			// add picture to selected list!
    			// need storage functions...
    			
    			setResult(RESULT_OK);
        		finish();
        	} 
            break;
        }
    }

	private void cancelBogoPic() {
		setResult(RESULT_CANCELED);
		finish();
	}

	private void acceptBogoPic() {
		Intent intent = new Intent(this, ListSelectionView.class);
		File filepath = getBmpFilepath();
		if (filepath != null) {
			intent.putExtra("filename", filepath.getAbsolutePath());
			// start a new SelectionView instance to select where photo goes
			startActivityForResult(intent, LIST_SELECT);
		} else {
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
>>>>>>> 464efd438d20a2e7556dcb4ab72bd418598de6a2
}