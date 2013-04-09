package ch.bfh.univoteverifier.utils;

import ch.bfh.univoteverifier.common.Config;
import java.util.Random;
import java.math.BigInteger;

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
	    
	    BigInteger a = CryptoUtils.sha(hashVal);
	    	    
	    BigInteger b = r.subtract(privateKey.multiply(a)).mod(Config.q);
	    
	    System.out.println("(a,b): " + a.toString() + ", " + b.toString());
	    
	    SchnorrSignature ss = new SchnorrSignature(a, b);
	    
	    return ss;
	}



}
