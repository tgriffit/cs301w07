package cs.ualberta.conditionlog.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cs.ualberta.conditionlog.R;
import cs.ualberta.conditionlog.model.DatabaseInputAdapter;

/**
 * An activity view that allows a new condition log to be created.
 * @author  Andrew Neufeld
 * @uml.dependency  supplier="cs.ualberta.conditionlog.PhotoUseSelectionView"
 */
public class CreateListView extends Activity {
	private EditText name;
	private Button newButton;
	
	/**
	 * An initializer method that creates the view objects for the interface buttons and is only called internally.
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);
        
        name = (EditText) findViewById(R.id.editName);
        newButton = (Button) findViewById(R.id.CreateLogButton);
		
		newButton.setEnabled(false);
		newButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				String nameText = name.getText().toString();
				
				// create the log in the database
				DatabaseInputAdapter dbadapter = new DatabaseInputAdapter(getApplicationContext());
				dbadapter.open();
				dbadapter.addCondition(nameText);
				dbadapter.close();
				
				setResult(RESULT_OK);
				getIntent().putExtra("name", nameText);
				finish();
			}
		});
        
		LocalTextWatcher watcher = new LocalTextWatcher();
	    name.addTextChangedListener(watcher);
	    updateButtonState();
    }

	private void updateButtonState() {
		boolean enabled;
		String boxText = name.getText().toString();
		if (boxText.equals(""))
			enabled = false;
		else
			enabled = true;
		newButton.setEnabled(enabled);
	}
	
	/**
	 *  Small subclass that has only one function: to watch to see if the EditText box has any characters in it.
	 * @author Jack
	 *
	 */
	private class LocalTextWatcher implements TextWatcher {
	    public void afterTextChanged(Editable s) {
	    	updateButtonState();
	    }

	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	    }
	}
}