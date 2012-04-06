package cs.ualberta.conditionlog.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cs.ualberta.conditionlog.R;
import cs.ualberta.conditionlog.controller.PasswordManager;

/**
 * This is the view for querying the user for either a new password or to enter a set password
 * @author     rleung
 */
public class MainView extends Activity{

  	private EditText password;
	private Button newButton;
	private boolean passwordSet;

	/**
	 * on create, creates the buttons and their listeners
	 * checks for a set password and loads appropriate layout
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PasswordManager pm = new PasswordManager(getApplicationContext());
		passwordSet = pm.checkIfPasswordSet();
		
		if (passwordSet) {
			setContentView(R.layout.enterpassword);
			password = (EditText) findViewById(R.id.enteredPassword);
			newButton = (Button) findViewById(R.id.acceptEnteredPassword);
		} else {
			setContentView(R.layout.newpassword);
			password = (EditText) findViewById(R.id.newPassword);
			newButton = (Button) findViewById(R.id.acceptNewPassword);
		}
	
		
		newButton.setEnabled(false);
		newButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				String passwordText = password.getText().toString();
				PasswordManager pm = new PasswordManager(getApplicationContext());
				
				if (passwordSet) {
					
					if (!pm.testPassword(passwordText)) {
						wrongPasswordPopup();
					}
					else{
						Intent i = new Intent(getApplicationContext(), MainMenuView.class);
						startActivity(i);
						finish();
					}
				} else {
					pm.setPassword(passwordText);
					Intent i = new Intent(getApplicationContext(), MainMenuView.class);
					startActivity(i);
					finish();
				}
			}
		});

		LocalTextWatcher watcher = new LocalTextWatcher();
		password.addTextChangedListener(watcher);
		updateButtonState();
	}

	/**
	 * enables the button
	 */
	private void updateButtonState() {
		boolean enabled = checkForText(password);
		newButton.setEnabled(enabled);
	}

	/**
	 * checks if the edit text is empty
	 * @param edit text to check
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
	
	/**
	 * pops up a toast notification indicating a wrong password
	 */
	private void wrongPasswordPopup() {
		Context context = getApplicationContext();
		String text = "Wrong Password, Try Again";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}




}
