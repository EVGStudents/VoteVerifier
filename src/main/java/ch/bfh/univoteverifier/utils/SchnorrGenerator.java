/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.utils;

import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.Config;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * This class generates a Schnorr's signature
 *
 * @author snake
 */
public class SchnorrGenerator {

	private static final BigInteger privateKey = new BigInteger("75654354564"); //Belongs to Z_q
	public static BigInteger publicKey = Config.g.modPow(privateKey, Config.p);
	//random number generator for the BigInteger k and the message
	private final Random randGen = new Random();

	/**
	 * Generate a signature using a message
	 *
	 * @param String message the message to sign
	 * @return SchnorrPair the result of the signature (s,e)
	 */
	public SchnorrSignature signatureGeneration(BigInteger message) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		int randInt = randGen.nextInt(Config.q.intValue());

		if (randInt == 0) {
			randInt += 1; //the random value must be between 1 and q
		}
		BigInteger r = BigInteger.valueOf(randInt);

		BigInteger concatVal = Config.g.modPow(r, Config.p);

		BigInteger a = CryptoFunc.sha256(message.toString() + concatVal.toString());

		BigInteger b = r.subtract(privateKey.multiply(a)).mod(Config.q);

		SchnorrSignature ss = new SchnorrSignature(a, b);

		return ss;
	}
}
