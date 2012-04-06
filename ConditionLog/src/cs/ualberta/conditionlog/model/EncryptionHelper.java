package cs.ualberta.conditionlog.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Handles the encryption and decryption of files as well as creating password
 * hashes so that the password can be stored in an unencrypted database.
 * @author tgriffit
 *
 */
public class EncryptionHelper {
	
	//The algorithm used to encrypt/decrypt the photos
	private static String ALGORITHM = "AES/CBC/PKCS5Padding";
	//The algorithm used to generate the key
	private static String KEY_ALGORITHM = "AES";
	//The hashing algorithm used to store passwords
	private static String HASH_ALGORITHM = "SHA-256";
	//The maximum size of a byte array
	private static int PHOTO_SIZE = 800000;
	//The initialization vector used in encryption
	private static byte[] iv = {(byte)0xc1, (byte)0x1d, (byte)0x53, (byte)0xf0,
		(byte)0x02, (byte)0xf4, (byte)0x8b, (byte)0x33, (byte)0x96, (byte)0x7e,
		(byte)0xa3, (byte)0x2e, (byte)0xb4, (byte)0x1f, (byte)0x56, (byte)0x70};

	private static Cipher ecipher;	//Cipher used to encrypt files
	private static Cipher dcipher;	//Cipher used to decrypt files
	
	/**
	 * Generates a password hash that can be stored in the database
	 * or compared to the one that is stored.
	 * @param pass
	 */
	public static String generatePasswordHash(String pass) {
		String hash = "";
		
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
			//Converts the password into a 128 bit byte array
			byte[] passBytes = padPass(pass);
			//Generates a key object based on the password
			SecretKeySpec key = new SecretKeySpec(passBytes, KEY_ALGORITHM);
			//SecretKey key = SecretKeyFactory.getInstance(KEY_ALGORITHM).generateSecret(keySpec);
			
			ecipher = Cipher.getInstance(ALGORITHM);
			dcipher = Cipher.getInstance(ALGORITHM);
			
			ecipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
			dcipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	}
	
	public static Bitmap loadBMP(String filename) {

		try {
			//Decrypts the input into a byte array
			FileInputStream in = new FileInputStream(filename);
			CipherInputStream cin = new CipherInputStream(in, dcipher);
			byte[] bmp = new byte[PHOTO_SIZE];
			cin.read(bmp);
			
			//Reads the byte array as a bmp
			ByteArrayInputStream bin = new ByteArrayInputStream(bmp);
			return BitmapFactory.decodeStream(bin);
			
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Save the given Bitmap to the given File filepath. Returns true if
	 * successful and false if unsuccessful.
	 * 
	 * @param filepath filepath to the picture
	 * @param ourBMP bitmap of the picture
	 * @return boolean
	 */
	public static boolean saveBMP(FileOutputStream out, Bitmap ourBMP) {
		try {
			CipherOutputStream cout = new CipherOutputStream(out, ecipher);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			
			//Converts the Bitmap to a byte array
			ourBMP.compress(Bitmap.CompressFormat.PNG, 100, bout);
			byte[] bmp = bout.toByteArray();
			
			cout.write(bmp);
			cout.flush();
			cout.close();
			return true;

		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	/*
	 * Turns the password into a 128-bit byte array.
	 */
	private static byte[] padPass(String pass) {
		String padded = "";
		
		if (pass.length() >= 16)
			padded = pass.substring(0, 16);
		else {
			padded = pass;
			
			while(padded.length() < 16)
				padded += "a";
		}
		
		return padded.getBytes();
	}
}
