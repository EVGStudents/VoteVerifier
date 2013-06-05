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
import javax.xml.bind.DatatypeConverter;

/**
 * This class contains some functions used by other classes.
 *
 * @author Scalzi Giuseppe
 */
public class CryptoFunc {

	private static final String HASH_256 = "sha-256";
	private static final String HASH_1 = "sha-1";
	private static final String ENCODING = "UTF-8";

	/**
	 * Compute the sha-256 hash of a given string.
	 *
	 * @param val BigInteger the value used to compute the hash.
	 * @return the hash as BigInteger representation.
	 * @throws NoSuchAlgorithmException if the hash algorithm cannot be
	 * found.
	 * @throws UnsupportedEncodingException if the encoding cannot be found.
	 */
	public static BigInteger sha256(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance(HASH_256);
		String utfStr = new String(s.getBytes(), ENCODING);
		md.update(utfStr.getBytes());
		return new BigInteger(1, md.digest());
	}

	/**
	 * Compute the sha-1 hash of a given string.
	 *
	 * @param s the string used to compute the hash.
	 * @return a BigInteger corresponding to the hash.
	 * @throws NoSuchAlgorithmException if the hash algorithm cannot be
	 * found.
	 * @throws UnsupportedEncodingException if the encoding cannot be found.
	 */
	public static BigInteger sha1(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance(HASH_1);
		String utfStr = new String(s.getBytes(), ENCODING);
		md.update(utfStr.getBytes());
		return new BigInteger(1, md.digest());
	}

	/**
	 * Decode a base64 encoded string.
	 *
	 * @param encodedBase64 the encoded string.
	 * @return the byte array of the decoded string, can be used in
	 * BigInteger constructor.
	 */
	public static byte[] decodeBase64(String encodedBase64) {
		return DatatypeConverter.parseBase64Binary(encodedBase64);
	}

	/**
	 * Get an X509 certificate from a byte array.
	 *
	 * @param b the byte array that shall contain the data of a X509
	 * certificate.
	 * @param newLine if true, the byte array will be parsed and a "\n" will
	 * be added after "-----BEGIN CERTIFICATE-----" and before "-----END
	 * CERTIFICATE-----".
	 * @return an X509 certificate.
	 * @throws CertificateException if the certificate factory cannot found
	 * the specified type.
	 */
	public static X509Certificate getX509Certificate(byte[] b, boolean newLine) throws CertificateException {
		String bStr = new String(b);

		if (newLine) {
			StringBuilder sb = new StringBuilder(bStr);
			//insert after -----BEGIN CERTIFICATE-----
			sb.insert(27, '\n');
			//insert before -----END CERTIFICATE-----
			sb.insert(sb.length() - 25, '\n');
			bStr = sb.toString();
		}

		InputStream is = new ByteArrayInputStream(bStr.getBytes());
		X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);

		return cert;
	}

	/**
	 * Decode a special string encoded with the variant of base64 used for
	 * UniVote.
	 *
	 * @param str the encoded string
	 * @return a BigInteger representing the decoded string.
	 */
	public static BigInteger decodeSpecialBase64(String str) {
		String base64code = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_=";
		BigInteger total = BigInteger.ZERO;

		//for each char in the string
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			//check if it exists in the base64 alphabet
			for (int j = 0; j < base64code.length(); j++) {
				if (base64code.charAt(j) == c) {
					BigInteger val = BigInteger.valueOf(j);
					BigInteger base64mul = BigInteger.valueOf(64);
					total = total.add(val.multiply(base64mul.pow(str.length() - i - 1)));
				}
			}
		}

		return total;
	}
}
