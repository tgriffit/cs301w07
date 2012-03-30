/**
 * This view is a gallery view of the Condition list. 
 * It shows a large zoomed in image of the photo selected from the upper portion.
 * @author adneufel
 * @date March 15th
 */
package cs.ualberta.conditionlog.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import cs.ualberta.conditionlog.R;
import cs.ualberta.conditionlog.controller.ImageAdapter;
import cs.ualberta.conditionlog.model.ConditionList;
import cs.ualberta.conditionlog.model.PhotoList;
import cs.ualberta.conditionlog.model.TagList;

/**
 * @author     tgriffit
 * @uml.dependency   supplier="cs.ualberta.conditionlog.ImageAdapter"
 */
public class ConditionView extends Activity {
	private String name;
	private String type;
	/**
	 * @uml.property  name="list"
	 * @uml.associationEnd  
	 */
	private PhotoList list;
	private Bitmap[] bmps;
	private int imagePosition = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.condition);
	    
	    Intent intent = getIntent();
	    this.name = intent.getStringExtra("name");
	    this.type = intent.getStringExtra("type");
	    Toast toast = Toast.makeText(getApplicationContext(), "type: " + type, Toast.LENGTH_SHORT);
	    toast.show();
	    
	    create();
	}
	
	private void create(){
		// load a condition list of name
		if (type.equals("log")) {
		    list = new ConditionList(name, this);
		} else if (type.equals("tag")) {
			list = new TagList(name, this);
		}

	    // get an array of Bitmaps from the condition list
	    bmps = list.toBmp();
	    
	    ImageView iv = (ImageView) findViewById(R.id.galleryImage);
	    if (bmps.length > 0) {
	    	// if there is images in the condition list set the first as a thumbnail
	    	iv.setImageBitmap(bmps[0]);
	    } else {
		    // if there is no images in the condition list state so and return accordingly
	    	Toast toast = Toast.makeText(getApplicationContext(), "No photos to view in that list.", Toast.LENGTH_LONG);
	    	toast.show();
	    	setResult(MainView.RESULT_NOSELECT);
	    	finish();
	    }
	    
	    // initialize the gallery view
	    Gallery gallery = (Gallery) findViewById(R.id.gallery);
	    // create and set the gallery adapter of images from list
	    gallery.setAdapter(new ImageAdapter(this, bmps));

	    gallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            // set the current gallery item image from the bitmap array
	        	imagePosition = position;
	        	ImageView iv = (ImageView) findViewById(R.id.galleryImage);
	            iv.setImageBitmap(bmps[position]);
	        }
	    });
	    
	    //add the button to compare to this image
	    Button compareButton = (Button) findViewById(R.id.compareButton);
    	compareButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			startCompareView();
    		}
    	});
    	
    	//add the button to delete this image
    	Button deleteButton = (Button) findViewById(R.id.deleteButton);
    	deleteButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			deleteImage();
    		}
    	});
    	
    	Button deleteTagButton = (Button) findViewById(R.id.deleteTagButton);
    	deleteTagButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			deleteTag();
    		}
    	});
    	
    	Button addTagButton = (Button) findViewById(R.id.addTagButton);
    	addTagButton.setOnClickListener(new View.OnClickListener() {

    		public void onClick(View v) {
    			addTag();
    		}
    	});
	}
	/**
	 * starts the compare view
	 */
	private void startCompareView(){
		Intent i = new Intent(this, ComparisonView.class);
		i.putExtra("position", imagePosition);
		i.putExtra("name", name);
        startActivity(i);
	}
	
	private void deleteImage(){
		list.deletePhoto(this, imagePosition);
		create();
	}
	
	private void addTag(){
		AlertDialog.Builder tag = new AlertDialog.Builder(this);

		tag.setTitle("Add Tag");
		tag.setMessage("add which tag?");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		tag.setView(input);

		tag.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString();
		  list.addTagToPhoto(getApplicationContext(), imagePosition, value);
		  }
		});

		tag.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    
		  }
		});

		tag.show();
		
	}
	
	private void deleteTag() {
		
	}
	
}
