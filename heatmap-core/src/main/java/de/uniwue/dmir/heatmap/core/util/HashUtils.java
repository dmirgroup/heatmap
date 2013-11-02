package de.uniwue.dmir.heatmap.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class to make working with digest algorithms easier (MD5, SHA-256, etc.).
 * 
 * @author Martin Becker
 *
 */
public class HashUtils {

	/**
	 * @param plain plain text
	 * 
	 * @return MD5 hash (in hex)
	 * 
	 * @throws RuntimeException if MD5 algorithm does not exist
	 */
	public static final String digestMd5(String plain) {
		try {
			return digest(plain, "MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param plain plain text
	 * 
	 * @return SHA-256 hash (in hex)
	 * 
	 * @throws RuntimeException if SHA-256 algorithm does not exist
	 */
	public static final String digestSha256(String plain) {
		try {
			return digest(plain, "SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param plain plain text
	 * @param algorithm digest algorithm
	 * 
	 * @return digest in hex according to the given algorithm
	 * 
	 * @throws NoSuchAlgorithmException if the digest algorithm does not exist
	 */
	public static String digest(String plain, String algorithm) 
	throws NoSuchAlgorithmException {
	
		MessageDigest messageDigest;
		messageDigest = MessageDigest.getInstance(algorithm);

		byte[] bytes = toByteArray(plain);
		byte[] digestedBytes = messageDigest.digest(bytes);
		
		String hex = toHexString(digestedBytes);
		
		return hex;
	}

	/**
	 * @param plain plain text
	 * 
	 * @return byte representation of the text using UTF-8 character set
	 * 
	 * @throws RuntimeException if encoding UTF-8 is not supported
	 */
	public static final byte[] toByteArray(String plain) {
		try {
			return plain.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param bytes byte array
	 * 
	 * @return hex representation
	 */
	public static final String toHexString(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (byte b : bytes) {
			String part = Integer.toHexString((int) (b & 0xff));
			stringBuffer.append(part);
		}
		return stringBuffer.toString();
	}
	
}
