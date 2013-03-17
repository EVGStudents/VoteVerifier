package crypto_vrf;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import utils.SchnorrGenerator;

import common.Config;

public class SchnorrVrf {
	
	/**
	 * Temp
	 */
	private BigInteger publicKey = SchnorrGenerator.publicKey;


	/**
	 * Verify the given signature
	 * @return boolean return true if the signature is verified correctly, false otherwise
	 */
	public boolean verifySchnorrSignature(SchnorrSignature signature, BigInteger message){
		
		if(Config.DEBUG_MODE)
			System.out.println("The received signature is: " + signature.getA() + ", " + signature.getB() );
		
		BigInteger concat = Config.g.modPow(signature.getB(), Config.p).multiply(publicKey.modPow(signature.getA(), Config.p)).mod(Config.p);
		
		BigInteger hashResult = sha(new BigInteger(message.toString() + concat.toString()));
		
		boolean res = hashResult.equals(signature.getA());
		
		return res;
	}
	
	
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
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
