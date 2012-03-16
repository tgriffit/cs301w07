package cs.ualberta.conditionlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.Toast;

public class ConditionView extends Activity {
	private String name;
	private ConditionList clist;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.condition);
	    
	    Intent intent = getIntent();
	    this.name = intent.getStringExtra("name");
	    
	    clist = new ConditionList(name, this);
	    
	    Gallery gallery = (Gallery) findViewById(R.id.gallery);
	    gallery.setAdapter(new ImageAdapter(this, clist));

	    gallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(ConditionView.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	
}
