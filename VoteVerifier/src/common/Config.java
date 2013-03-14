package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;

public class Config {

	public static final String CONFIG = "etc/config.properties";
	
	/**
	 * Tells whether we are working in debug mode
	 */
	public static final boolean DEBUG_MODE = true;
	
	/**
	 * Schnorr's parameters are fixed and don't change over the time.
	 */
	public static BigInteger p;
	public static BigInteger q;
	public static BigInteger g;
	public static String hashAlgorithm;
	
	static{
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream(CONFIG));
			
			p = new BigInteger(prop.getProperty("schnorr_p"));
			q = new BigInteger(prop.getProperty("schnorr_q"));
			g = new BigInteger(prop.getProperty("schnorr_g"));
			hashAlgorithm = prop.getProperty("hash_function");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
