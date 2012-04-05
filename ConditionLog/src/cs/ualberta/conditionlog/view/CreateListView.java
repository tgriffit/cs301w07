/**
 * author: Andrew Neufeld
 * description: View to name and create a new List
 * date: March 14
 */

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
 * @uml.dependency   supplier="cs.ualberta.conditionlog.PhotoUseSelectionView"
 */
public class CreateListView extends Activity {
	private EditText name;
	private Button newButton;
	
	@Override
	/**
	 * on create, creates the buttons and their listeners
	 * creates a database adapter to be used
	 */
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
	
	/**
	 * enables the button
	 */
	private void updateButtonState() {
		boolean enabled = checkForText(name);
		newButton.setEnabled(enabled);
	}
	
	/**
	 * checks if the edit text is empty
	 * @param edit	text to check
	 * @return 	true or false
	 */
	private boolean checkForText(EditText edit) {
		if (!edit.getText().toString().equals("")) { return true; }
	    return false;
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