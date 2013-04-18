package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.verification.VerificationEnum;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;


/**
 *
 * @author snake
 */
public class CryptoFunc {
	
	private static final Logger LOGGER = Logger.getLogger(CryptoFunc.class.getName());
	private static final String HASH_ALGORITHM = "SHA-256";
	
	
	/**
	 * Compute the hash of a number and then put it into a BigInteger.
	 * Change the value in the configuration file to chose the appropriate hash algorithm
	 * @param val BigInteger the value used to compute the hash
	 * @return BigInteger the hash as BigInteger representation
	 * @throws NoSuchAlgorithmException
	 */
	public static BigInteger sha(BigInteger val, BigInteger modValue) throws NoSuchAlgorithmException{
		BigInteger result;
		
		MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
		
		md.update(val.toByteArray());
		
		result = new BigInteger(md.digest()).mod(modValue);
		
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
		return CryptoFunc.sha(new BigInteger(concat), Config.q);
		
	}
	
	
}
