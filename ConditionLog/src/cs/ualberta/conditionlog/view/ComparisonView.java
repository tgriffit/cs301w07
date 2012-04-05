/**
 * This view is a gallery view of the Condition list, with a image for comparison added. 
 * It shows a large zoomed in image of the photo selected from the upper portion, and then the image to compare to.
 * @author Jack
 * @date March 15th
 */
package cs.ualberta.conditionlog.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import cs.ualberta.conditionlog.R;
import cs.ualberta.conditionlog.model.ConditionList;
import cs.ualberta.conditionlog.model.DatabaseAdapter;
import cs.ualberta.conditionlog.model.DatabaseOutputAdapter;
import cs.ualberta.conditionlog.model.PhotoList;
import cs.ualberta.conditionlog.model.TagList;
import cs.ualberta.conditionlog.controller.ImageAdapter;

/**
 * @author     Jack
 * @uml.dependency   supplier="cs.ualberta.conditionlog.ImageAdapter"
 */
public class ComparisonView extends Activity {
	private String name;
	/**
	 * @uml.property  name="clist"
	 * @uml.associationEnd  
	 */
	private PhotoList list;
	private Bitmap[] bmps;
	private int comparePosition;
	private String type;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.comparison);
	    
	    Intent intent = getIntent();
	    this.name = intent.getStringExtra("name");
	    this.comparePosition = intent.getIntExtra("position", 0);
	    this.type = intent.getStringExtra("type");
	    
	 // load a condition list of name
		if (type.equals("log"))
		    list = new ConditionList(name, this);
		else if (type.equals("tag"))
			list = new TagList(name, this);
		else if (type.equals("time")) {
			ArrayList<String> filenames;
			DatabaseOutputAdapter dba = new DatabaseOutputAdapter(getApplicationContext());
			dba.open();
			filenames = dba.loadPhotosByTime();
			dba.close();
			list = new PhotoList("time");
			list.setFilenames(filenames);
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
	    	setResult(MainMenuView.RESULT_NOSELECT);
	    	finish();
	    }
	    
	    ImageView ivCompare = (ImageView) findViewById(R.id.compareImage);
	    if (bmps.length > 0) {
	    	// if there is images in the condition list set the appropriate one as our compare image
	    	ivCompare.setImageBitmap(bmps[this.comparePosition]);
	    } else {
		    // if there is no images in the condition list state so and return accordingly
	    	Toast toast = Toast.makeText(getApplicationContext(), "No photos to view in that list.", Toast.LENGTH_LONG);
	    	toast.show();
	    	setResult(MainMenuView.RESULT_NOSELECT);
	    	finish();
	    }
	    
	    // initialize the gallery view
	    Gallery gallery = (Gallery) findViewById(R.id.gallery);
	    // create and set the gallery adapter of images from clist
	    gallery.setAdapter(new ImageAdapter(this, bmps));

	    gallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            // set the current gallery item image from the bitmap array
	        	ImageView iv = (ImageView) findViewById(R.id.galleryImage);
	            iv.setImageBitmap(bmps[position]);
	        }
	    });
	    
	    
	}
	
}