/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 * This class contains some functions used by other classes
 *
 * @author snake
 */
public class CryptoFunc {

	private static final Logger LOGGER = Logger.getLogger(CryptoFunc.class.getName());
	private static final String HASH_256 = "sha-256";
	private static final String HASH_1 = "sha-1";

	/**
	 * Compute the hash of a number and then put it into a BigInteger.
	 * Change the value in the configuration file to chose the appropriate
	 * hash algorithm.
	 *
	 * @param val BigInteger the value used to compute the hash
	 * @return the hash as BigInteger representation
	 * @throws NoSuchAlgorithmException
	 */
	public static BigInteger sha256(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance(HASH_256);
		String utfStr = new String(s.getBytes(), "UTF-8");
		md.update(utfStr.getBytes());
		return new BigInteger(1, md.digest());
	}

	/**
	 * Compute the sha-1 hash of a given string.
	 *
	 * @param s the string used to compute the hash
	 * @return a BigInteger corresponding to the hash
	 * @throws NoSuchAlgorithmException
	 */
	public static BigInteger sha1(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance(HASH_1);
		String utfStr = new String(s.getBytes(), "UTF-8");
		md.update(utfStr.getBytes());
		return new BigInteger(1, md.digest());
	}

	/**
	 * Decode a base64 encoded string.
	 *
	 * @param encodedBase64 the encoded string
	 * @return the byte array of the decoded string, can be used in
	 * BigInteger constructur
	 */
	public static String decodeBase64(String encodedBase64) throws UnsupportedEncodingException {
		return new String(DatatypeConverter.parseBase64Binary(encodedBase64), "UTF-8");
	}

	/**
	 * Get an X509 certificate from a byte array.
	 *
	 * @param b the byte array
	 * @return an X509 certificate
	 * @throws CertificateException
	 */
	public static X509Certificate getX509Certificate(byte[] b) throws CertificateException {
		InputStream is = new ByteArrayInputStream(b);
		X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
		return cert;
	}
}
