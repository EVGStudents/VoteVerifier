package utils;

import java.security.*;
import java.util.Random;
import java.math.BigInteger;

import common.Config;
public class SchnorrGenerator {


	private static BigInteger privateKey = new BigInteger("75654354564"); //Belongs to Z_q

	public static BigInteger publicKey = Config.g.modPow(privateKey, Config.p);

	//random number generator for the BigInteger k and the message
	private Random randGen = new Random();


	/**
	 * Generate a signature using a message
	 * @param String message the message to sign
	 * @return SchnorrPair the result of the signature (s,e)
	 */
	public SchnorrSignature signatureGeneration(BigInteger message){
	
		int randInt = randGen.nextInt(Config.q.intValue());

		if(randInt == 0) randInt += 1; //the random value must be between 1 and q

	    BigInteger r = BigInteger.valueOf(randInt);
	    
	    System.out.println("The random value is: " + r.toString());
	    
	    BigInteger concatVal = Config.g.modPow(r, Config.p);
	    
	    BigInteger hashVal = new BigInteger(message.toString() + concatVal.toString());
	    
	    BigInteger a = sha(hashVal);
	    	    
	    BigInteger b = r.subtract(privateKey.multiply(a)).mod(Config.q);
	    
	    System.out.println("(a,b): " + a.toString() + ", " + b.toString());
	    
	    SchnorrSignature ss = new SchnorrSignature(a, b);
	    
	    return ss;
	}

	/**
	 * Verify a Schnorr signature with a given SchnorrPair
	 * @param schn SchnorrPair the pair of values (s,e)
	 * @param message String the message to be verified
	 */
	/*public static void signatureVerification(SchnorrPair schn, String message){
		BigInteger s = schn.s;
		BigInteger e = schn.e;

		//Verification
		System.out.println("\n=== Verification using (s,e) ===\n");

		BigInteger v = alpha.modPow(s, p).multiply(publicKey.modPow(e, p)).mod(p);

		System.out.println("The v value is (alpha^s * y^e mod p): " + alpha.toString() + "^" + s.toString() + " * " + publicKey.toString() + "^" + e.toString() + " mod " + p.toString() + " = " + v.toString());

		BigInteger messageVerificationConcat = new BigInteger(message.concat(v.toString()));

		System.out.println("The concatenated message for verification is message||v: " + message.toString() + "||" + v.toString());

		BigInteger eVerification = sha("SHA-256", messageVerificationConcat);

		System.out.println("The verified e value for SHA-256 mod q is: " + eVerification);

		if(e.equals(eVerification)) System.out.println("Signature for message " + message + " verified successfully.");
		else System.out.println("Verification failed.");
	}*/


	/**
	 * Compute the hash of a number and then put it into a BigInteger 
	 * @param shaVariant String the name of the hashing algorithm chose
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
