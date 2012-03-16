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

public class ConditionView extends Activity {
	private String name;
	private ConditionList clist;
	//Bitmap bmp = BogoPicGen.generateBitmap(1000, 1000);
	private Bitmap[] bmps;// = { bmp, bmp, bmp, bmp, bmp, bmp, bmp};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.condition);
	    
	    Intent intent = getIntent();
	    this.name = intent.getStringExtra("name");
	    
	    clist = new ConditionList(name, this);
	    bmps = clist.toBmp();
	    String[] test = clist.toArray();
	    
	    for (int i = 0; i < test.length; i++) {
	    	Toast toast = Toast.makeText(this, test[i], Toast.LENGTH_LONG);
	    	toast.show();
	    }
	    
	    ImageView iv = (ImageView) findViewById(R.id.galleryImage);
	    iv.setImageBitmap(bmps[0]);
	    
	    Gallery gallery = (Gallery) findViewById(R.id.gallery);
	    gallery.setAdapter(new ImageAdapter(this, bmps));

	    gallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            ImageView iv = (ImageView) findViewById(R.id.galleryImage);
	            iv.setImageBitmap(bmps[position]);
	        }
	    });
	}
	
}
