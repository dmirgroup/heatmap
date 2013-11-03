/**
 * Heatmap Framework - Core
 *
 * Copyright (C) 2013	Martin Becker
 * 						becker@informatik.uni-wuerzburg.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
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
			
			if(part.length() < 2) {
				stringBuffer.append(0);
			}
			
			stringBuffer.append(part);
		}
		return stringBuffer.toString();
	}
	
}
