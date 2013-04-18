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
	 * Schnorr's parameters are fixed and don't change over the time.
	 */
	public static final BigInteger p= new BigInteger(prop.getProperty("schnorr_p"));
	public static final BigInteger q= new BigInteger(prop.getProperty("schnorr_q"));
	public static final BigInteger g= new BigInteger(prop.getProperty("schnorr_g"));
	public static final String hashAlgorithm= prop.getProperty("hash_function");
}
