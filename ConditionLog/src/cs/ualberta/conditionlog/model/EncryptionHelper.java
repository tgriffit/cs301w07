package cs.ualberta.conditionlog.model;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Handles the encryption and decryption of files as well as creating password
 * hashes so that the password can be stored in an unencrypted database.
 * @author tgriffit
 *
 */
public class EncryptionHelper {
	
	//The algorithm used to encrypt/decrypt the photos
	private static String ALGORITHM = "AES";
	//The hashing algorithm used to store passwords
	private static String HASH_ALGORITHM = "SHA-256";

	private static Cipher ecipher;	//Cipher used to encrypt files
	private static Cipher dcipher;	//Cipher used to decrypt files
	
	/**
	 * Generates a password hash that can be stored in the database
	 * or compared to the one that is stored.
	 * @param pass
	 */
	public static String generatePasswordHash(String pass) {
		String hash = null;
		
		try {
			//Encrypts the password using the SHA-256 algorithm
			MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
			messageDigest.update(pass.getBytes());
			hash = new String(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hash;
	}
	
	/**
	 * Initializes the encryption and decryption ciphers that will be used
	 * to create the appropriate cipher streams.
	 * @param pass
	 */
	public static void init(String pass) {
		try {
			//Generates a key object based on the password
			KeySpec keySpec = new PBEKeySpec(pass.toCharArray());
			SecretKey key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);
			
			ecipher = Cipher.getInstance(ALGORITHM);
			dcipher = Cipher.getInstance(ALGORITHM);
			
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			dcipher.init(Cipher.DECRYPT_MODE, key);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} 
	}

	public CipherOutputStream getEncryptionStream(OutputStream out) {
		return new CipherOutputStream(out, ecipher);
	}
	
	public CipherInputStream getDecryptionStream(InputStream in) {
		return new CipherInputStream(in, dcipher);
	}
	
}
