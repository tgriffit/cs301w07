package cs.ualberta.conditionlog.controller;

import android.content.Context;
import cs.ualberta.conditionlog.model.EncryptionHelper;
import cs.ualberta.conditionlog.model.DatabaseAdapter;

/**
 * Provides methods for checking whether a password has been specified, creating a
 * password, and testing whether a password is valid.
 * @author tgriffit
 * @date March 29
 */

public class PasswordManager {
	
	Context context;
	
	public PasswordManager(Context context) {
		this.context = context;
	}
	
	private String getPassword() {
		DatabaseAdapter db = new DatabaseAdapter(context);
		
		db.open();
		String pass = db.getPasswordHash();
		db.close();
		
		return pass;
	}
	
	/**
	 * Checks whether or not a password has been saved in the database
	 * @return true if a password has been specified or false if not
	 */
	public boolean checkIfPasswordSet() {
		String pass = getPassword();
		
		if (pass.equals(""))
			return false;
		return true;
	}
	
	/**
	 * Tests whether or not a password is valid.
	 * @param pass
	 * @return true if the password is correct or false if it isn't
	 */
	public boolean testPassword(String pass) {
		String dbHash = getPassword();
		
		//Generates the hash of the given password
		String encHash = EncryptionHelper.generatePasswordHash(pass);
		
		//If the hash matches the password hash stored in the database then the passwords are the same
		if (dbHash.equals(encHash)) {
			EncryptionHelper.init(pass);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Saves a hashed password in the database.
	 * @param pass
	 */
	public void setPassword(String pass) {
		String hash = EncryptionHelper.generatePasswordHash(pass);
		DatabaseAdapter db = new DatabaseAdapter(context);
		
		db.open();
		db.setPasswordHash(hash);
		db.close();
		
		EncryptionHelper.init(pass);
	}
	
	
	
	/**
	 * Removes the password from the database.  Any photos encrypted with the old
	 * password will become unreadable, so use this carefully.  Included for testing
	 * purposes.
	 */
	public void resetPassword() {
		DatabaseAdapter db = new DatabaseAdapter(context);
		
		db.open();
		db.deletePassword();
		db.close();
	}
	
	/**
	 * Finds the actual hash stored in the database.  Used during testing
	 * so that the old password can be backed up.
	 * @return
	 */
	public String getHashForTesting() {
		String hash = getPassword();
		
		return hash;
	}
	
	/**
	 * Used to restore the old password hash after testing without knowing the
	 * plaintext for the password.
	 * @param hash
	 * @return
	 */
	public void setHashAfterTesting(String hash) {
		DatabaseAdapter db = new DatabaseAdapter(context);
		
		db.open();
		db.setPasswordHash(hash);
		db.close();
	}
}
