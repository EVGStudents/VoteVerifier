package ch.bfh.univoteverifier.utils;

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CryptoUtils {
	
	/**
	 * Compute the hash of a number and then put it into a BigInteger.
	 * Change the value in the configuration file to chose the appropriate hash algorithm
	 * @param val BigInteger the value used to compute the hash
	 * @return BigInteger the hash as BigInteger representation
	 */
	public static BigInteger sha(BigInteger val){

		BigInteger result = null;
		try {
			
			MessageDigest md = MessageDigest.getInstance(Config.hashAlgorithm);
			md.update(val.toByteArray());
			result = new BigInteger(md.digest()).mod(Config.q);
			
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
		}

		return result;
	}
	
	/**
	 * ToDO
	 * @param one
	 * @param two
	 * @return
	 */
	public static String concatenation(String one, String two){
		return "";
	}
	
	
	/**
	 * Concatenates n given BigInteger values into a string and pads them with
	 * the arbitrary string 001100
	 * @param c an array of BigInteger values
	 * @return and String value of the concatenated contents of the array
	 */
	public static BigInteger concatArrayContents(BigInteger[] c){
		//TODO the calls to this method from NIZKP need to also send the Vi Voter ID?
		//		BigInteger bigConcat=  BigInteger.ZERO;
		String concat="";
		for(BigInteger ci:c){
			//001100 similates padding
			concat += ci.toString() +"001100";
		}
		return CryptoUtils.sha(new BigInteger(concat));
		
	}

	public static VerificationResult getVrfRes(Verification v, boolean res){
		return new VerificationResult(v, res);
	}
	
}
