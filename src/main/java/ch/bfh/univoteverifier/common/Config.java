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

import java.io.FileInputStream;
import java.io.IOException;
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
	public static final String CONFIG = "src/main/java/ch/bfh/univoteverifier/resources/config.properties";
	private static final Properties prop = new Properties();

	static {
		try {
			prop.load(new FileInputStream(CONFIG));
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
}
