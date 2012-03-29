package cs.ualberta.conditionlog.controller;

import cs.ualberta.conditionlog.model.EncryptionHelper;
import cs.ualberta.conditionlog.model.DatabaseAdapter;
/**
 * Provides methods for checking whether a password has been specified, creating a
 * password, and testing whether a password is valid.
 * @author tgriffit
 * @date March 29
 */
public class PasswordManager {
	
	/**
	 * Checks whether or not a password has been saved in the database
	 * @return true if a password has been specified or false if not
	 */
	public boolean checkIfPasswordSet() {
		return true;
	}
	
	/**
	 * Tests whether or not a password is valid.
	 * @param pass
	 * @return true if the password is correct or false if it isn't
	 */
	public boolean testPassword(String pass) {
		//load password hash from db
		String dbHash = "";
		
		EncryptionHelper encrypt = new EncryptionHelper();
		String encHash = encrypt.generatePasswordHash(pass);
		
		if (dbHash.equals(encHash)) {
			encrypt.generatePhotoHash(pass);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Saves a hashed password in the database.
	 * @param pass
	 */
	public void setPassword(String pass) {
		EncryptionHelper encrypt = new EncryptionHelper();
		String hash = encrypt.generatePasswordHash(pass);
		
		//save hash to db
		
		
	}
}
