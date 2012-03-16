/**
 * This view is a gallery view of the Condition list. 
 * It shows a large zoomed in image of the photo selected from the upper portion.
 * @author adneufel
 * @date March 15th
 */
package cs.ualberta.conditionlog;

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

/**
 * @author   tgriffit
 */
public class ConditionView extends Activity {
	private String name;
	/**
	 * @uml.property  name="clist"
	 * @uml.associationEnd  
	 */
	private ConditionList clist;
	private Bitmap[] bmps;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.condition);
	    
	    Intent intent = getIntent();
	    this.name = intent.getStringExtra("name");
	    
	    // load a condition list of name
	    clist = new ConditionList(name, this);
	    // get an array of Bitmaps from the condition list
	    bmps = clist.toBmp();
	    
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
