package cs.ualberta.conditionlog.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

public class EncryptionHelper {

	private static String photoPass = "";
	
	/**
	 * Generates a password hash that can be stored in the database
	 * or compared to the one that is stored.
	 * @param pass
	 */
	public String generatePasswordHash(String pass) {
		String hash = null;
		
		try {
			//Encrypts the password using the SHA-256 algorithm
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(pass.getBytes());
			hash = new String(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hash;
	}
	
	/**
	 * Generates a hash using a different algorithm than is used for
	 * the normal password.  This hash is used as the password for
	 * encrypting/decrypting photos.
	 * @param pass
	 */
	public void generatePhotoHash(String pass) {
		//Generate SHA-1 hash to encrypt/decrypt photos
	}
	
}
