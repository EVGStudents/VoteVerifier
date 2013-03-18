package common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;

public class Config {

	/**
	 * Configuration file path
	 */
	public static final String CONFIG = "etc/config.properties";
	
	/**
	 * Tells whether we are working in debug mode
	 */
	public static final boolean DEBUG_MODE = true;
	
	
	private static Properties prop = new Properties();
	static {try {
		prop.load(new FileInputStream(CONFIG));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}}
	
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
	
//	static{
//		Properties prop = new Properties();
//		
//		try {
//			prop.load(new FileInputStream(CONFIG));
//			
//			p = new BigInteger(prop.getProperty("schnorr_p"));
//			q = new BigInteger(prop.getProperty("schnorr_q"));
//			g = new BigInteger(prop.getProperty("schnorr_g"));
//			
//			hashAlgorithm = prop.getProperty("hash_function");
//			
//			pLength = Integer.parseInt(prop.getProperty("p_length"));
//			qLength = Integer.parseInt(prop.getProperty("q_length"));
//			gLength = Integer.parseInt(prop.getProperty("g_length"));
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}
