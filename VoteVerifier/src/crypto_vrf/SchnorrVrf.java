package crypto_vrf;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import common.Config;

public class SchnorrVrf {
	
	/**
	 * Inner class used to represent the Schnorr's signature
	 * @author snake
	 *
	 */
	public class SchnorrSignature{
		BigInteger a;
		BigInteger b;
		
		/**
		 * Construct a new Schnorr's Signature using the previously computed a and b
		 * @param a first parameter of Schnorr's signature, the hash value
		 * @param b second parameter
		 */
		public SchnorrSignature(BigInteger a, BigInteger b){
			this.a = a;
			this.b = b;
		}
		
		/**
		 * Get the first value of the signature
		 * @return BigInteger the first value
		 */
		public BigInteger getA(){
			return a;
		}
		
		/**
		 * Get the second value of the signautre
		 * @return BigInteger the second value
		 */
		public BigInteger getB(){
			return b;
		}
	}
	
	
	private SchnorrSignature signature;
	private BigInteger message;
	private BigInteger publicKey;

	/**
	 * Construct a verificator for the Schnorr's signature
	 * @param s SchnorrSignature the signature (a,b)
	 * @param m BigInteger the message used to compute the signature
	 */
	public SchnorrVrf(SchnorrSignature s, BigInteger m){
		signature = s;
		message = m;
	}
	
	/**
	 * Verify the given signature
	 * @return boolean return true if the signature is verified correctly, false otherwise
	 */
	public boolean isSchnorrVerified(){
		//compute the value to be concatenated to m
		BigInteger concatValue = 
		Config.g.modPow(signature.getA(), Config.p).multiply(publicKey.modPow(signature.getB(), Config.p)).mod(Config.p);

		//temporary string to store the concatenation m||g^a*publicKey^b
		String concatenation = message.toString() + concatValue.toString();
		
		BigInteger hashParameter = new BigInteger(concatenation);
		
		//verify the signature: a must be equal to hash(hashParameter)
		return hashParameter.equals(sha(hashParameter));
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
