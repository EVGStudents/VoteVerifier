/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.common;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains all the variables for the configuration.
 *
 * @author Scalzi Giuseppe
 */
public class Config {

	/**
	 * Configuration file path.
	 */
	public static final String CONFIG = "config.properties";
	private static final Properties prop = new Properties();

	static {
		try {
			InputStream is = Config.class.getClassLoader().getResourceAsStream(CONFIG);
			prop.load(is);
		} catch (IOException ex) {
			Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	/**
	 * Schnorr's parameters are fixed and don't change over the time, as
	 * well as the hash algorithm for Schnorr's signatures and proof of
	 * knowledge.
	 */
	public static final BigInteger p = new BigInteger(prop.getProperty("schnorr_p"));
	public static final BigInteger q = new BigInteger(prop.getProperty("schnorr_q"));
	public static final BigInteger g = new BigInteger(prop.getProperty("schnorr_g"));
	public static final String hashAlgorithm = prop.getProperty("hash_function");
	/**
	 * URL of the WSDL of UniVote.
	 */
	public static final String wsdlLocation = prop.getProperty("wsdl_url");
	/**
	 * The sleep time between each checks of a verification.
	 */
	public static final int sleepTime = Integer.valueOf(prop.getProperty("sleep_time"));
	/**
	 * Number of verifications for the universal. Multiply the
	 * VERIFICATIONS_FOR_TALLIER and VERIFICATIONS_FOR_MIXER for the number
	 * of talliers and mixers. Then sum UNIVERSAL_VERIFICATION_COUNT to get
	 * the total.
	 */
	public static final int UNIVERSAL_VERIFICATION_COUNT = 46;
	public static final int VERIFICATIONS_FOR_TALLIER = 4;
	public static final int VERIFICATIONS_FOR_MIXER = 10;
	/**
	 * Number of verifications for the individual
	 */
	public static final int INDIVIDUAL_VERIFICATION_COUNT = 5;
}
