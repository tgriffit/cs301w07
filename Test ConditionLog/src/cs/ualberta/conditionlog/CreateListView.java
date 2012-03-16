/*
 * author: Andrew Neufeld
 * description: View to name and create a new List
 * date: March 14
 */

package cs.ualberta.conditionlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateListView extends Activity {
	private EditText name;
	private Button newButton;
	
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
				DatabaseAdapter dbadapter = new DatabaseAdapter(getApplicationContext());
				dbadapter.open();
				dbadapter.addCondition(nameText);
				dbadapter.close();
				
				// pass the name of the log back to ListSelectionView
				Intent intent = new Intent();
				intent.putExtra("name", nameText);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
        
		LocalTextWatcher watcher = new LocalTextWatcher();
	    name.addTextChangedListener(watcher);
	    updateButtonState();
    }
	
	private void updateButtonState() {
		boolean enabled = checkForText(name);
		newButton.setEnabled(enabled);
	}
	
	private boolean checkForText(EditText edit) {
		if (!edit.getText().toString().equals("")) { return true; }
	    return false;
	}
	
	// Small subclass that has only one function: to watch to see if the EditText box has any characters in it.
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