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
	public static boolean checkIfPasswordSet() {
		//if (getPasswordHash.equals(""))
		return true;
	}
	
	/**
	 * Tests whether or not a password is valid.
	 * @param pass
	 * @return true if the password is correct or false if it isn't
	 */
	public static boolean testPassword(String pass) {
		//load password hash from db
		String dbHash = "";
		
		String encHash = EncryptionHelper.generatePasswordHash(pass);
		
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
	public static void setPassword(String pass) {
		String hash = EncryptionHelper.generatePasswordHash(pass);
		
		//save hash to db
		
		EncryptionHelper.init(pass);
		
	}
}
