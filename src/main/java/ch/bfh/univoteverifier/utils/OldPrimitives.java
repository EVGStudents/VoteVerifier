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

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class OldPrimitives {

	private static final Logger LOGGER = Logger.getLogger(OldPrimitives.class.getName());

	/**
	 * ToDo - This method is used in the class utils. Maybe move it there in
	 * another class
	 *
	 * @param s
	 * @param mIn
	 * @return
	 */
	public boolean vrfRSASign(RSASignature s, BigInteger mIn) {
		BigInteger ver = s.getSig().modPow(s.getE(), s.getN());

		boolean result = ver.equals(mIn);

		LOGGER.log(Level.SEVERE, "RSA Verification failed");

		return result;
	}

	/**
	 * Verify the given signature. - ToDo move this in a class in the utils
	 * package
	 *
	 * @param signature the signature to check
	 * @param message the content to sign
	 * @param publicKey the public key used to verify the signature
	 * @return boolean return true if the signature is verified correctly,
	 * false otherwise
	 */
	public boolean vrfSchnorrSign(SchnorrSignature signature, BigInteger message, BigInteger publicKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		BigInteger concat = Config.g.modPow(signature.getB(), Config.p).multiply(publicKey.modPow(signature.getA(), Config.p)).mod(Config.p);

		BigInteger hashResult = CryptoFunc.sha256(message.toString() + concat.toString());

		boolean res = hashResult.equals(signature.getA());

		return res;
	}

	/**
	 * verify a Non-Interactive Zero Knowledge Proof of Discrete Logs
	 *
	 * @param prf
	 * @return true if the proof is correct
	 */
	public boolean vrfNIZKP(ProofDiscreteLog prf) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		BigInteger c2;

		int validProof = 0;

		BigInteger[] concatB = {prf.getVk(), prf.getT()};
		c2 = concatArrayContents(concatB);

		validProof += c2.compareTo(prf.getC());

		LOGGER.log(Level.SEVERE, "Proof FAILED: prf.c == vk.add(prf.t).mod(p)");

		BigInteger v = prf.getG().modPow(prf.getS(), prf.getP());
		BigInteger w = (prf.getT().multiply(prf.getVk().modPow(prf.getC(), prf.getP()))).mod(prf.getP());
		validProof += (v.compareTo(w));

		LOGGER.log(Level.SEVERE, "Proof FAILED: Second part");

		boolean results = 0 == validProof;

		return results;
	}

	/**
	 * Concatenates n given BigInteger values into a string and pads them
	 * with the arbitrary string 001100.
	 *
	 * @param c an array of BigInteger values
	 * @return and String value of the concatenated contents of the array
	 */
	private BigInteger concatArrayContents(BigInteger[] c) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//TODO the calls to this method from NIZKP need to also send the Vi Voter ID?
		//		BigInteger bigConcat=  BigInteger.ZERO;
		String concat = "";
		for (BigInteger ci : c) {
			//001100 similates padding
			concat += ci.toString() + "001100";
		}
		return CryptoFunc.sha256(concat);

	}
}
