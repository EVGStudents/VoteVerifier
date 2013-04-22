package ch.bfh.univoteverifier.common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;


/**
 * This class contains some functions used by other classes
 * @author snake
 */
public class CryptoFunc {
	
	private static final Logger LOGGER = Logger.getLogger(CryptoFunc.class.getName());
	
	
	/**
	 * Compute the hash of a number and then put it into a BigInteger.
	 * Change the value in the configuration file to chose the appropriate hash algorithm
	 * @param val BigInteger the value used to compute the hash
	 * @return BigInteger the hash as BigInteger representation
	 * @throws NoSuchAlgorithmException
	 */
	public static BigInteger sha(BigInteger val) throws NoSuchAlgorithmException{
		BigInteger result;
		
		MessageDigest md = MessageDigest.getInstance(Config.hashAlgorithm);
		
		md.update(val.toByteArray());
		
		result = new BigInteger(md.digest()).mod(Config.q);
		
		return result;
	}
	
	
	/**
	 * ToDO - probably useless
	 * Concatenates n given BigInteger values into a string and pads them with
	 * the arbitrary string 001100
	 * @param c an array of BigInteger values
	 * @return and String value of the concatenated contents of the array
	 */
	public static BigInteger concatArrayContents(BigInteger[] c) throws NoSuchAlgorithmException{
		//TODO the calls to this method from NIZKP need to also send the Vi Voter ID?
		//		BigInteger bigConcat=  BigInteger.ZERO;
		String concat="";
		for(BigInteger ci:c){
			//001100 similates padding
			concat += ci.toString() +"001100";
		}
		return CryptoFunc.sha(new BigInteger(concat));
		
	}
	
	
}
