package ch.bfh.univoteverifier.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

	/**
	 * Configuration file path
	 */
	public static final String CONFIG = "src/ch/bfh/univoteverifier/properties/config.properties";
	
	/**
	 * Tells whether we are working in debug mode
	 */
	public static final boolean DEBUG_MODE = true;
	
	
	private static final Properties prop = new Properties();
	
	static {
		try {
			prop.load(new FileInputStream(CONFIG));
		} catch (IOException ex) {
			Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Schnorr's parameters are fixed and don't change over the time.
	 */
	public static final BigInteger p= new BigInteger(prop.getProperty("schnorr_p"));
	public static final BigInteger q= new BigInteger(prop.getProperty("schnorr_q"));
	public static final BigInteger g= new BigInteger(prop.getProperty("schnorr_g"));
	public static final String hashAlgorithm= prop.getProperty("hash_function");
	public static final int pLength= Integer.parseInt(prop.getProperty("p_length"));
	public static final int qLength= Integer.parseInt(prop.getProperty("q_length"));
	public static final int gLength= Integer.parseInt(prop.getProperty("g_length"));
	
	
}