/**
*
*  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
*   Bern University of Applied Sciences, Engineering and Information Technology,
*   Research Institute for Security in the Information Society, E-Voting Group,
*   Biel, Switzerland.
*
*   Project independent UniVoteVerifier.
*
*/
package ch.bfh.univoteverifier.implementer;

import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.utils.ProofDiscreteLog;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class ProofImplementer {
	
	private static final Logger LOGGER = Logger.getLogger(ProofImplementer.class.getName());
	
	/**
	 * verify a Non-Interactive Zero Knowledge Proof of Discrete Logs
	 * @param prf
	 * @return true if the proof is correct
	 */
	public boolean vrfNIZKP(ProofDiscreteLog prf) throws NoSuchAlgorithmException {
		BigInteger c2;
		
		int validProof = 0;
		
		BigInteger[] concatB = {prf.getVk(),prf.getT()};
		c2 = CryptoFunc.concatArrayContents(concatB);
		
		validProof += c2.compareTo(prf.getC());
		
		LOGGER.log(Level.SEVERE, "Proof FAILED: prf.c == vk.add(prf.t).mod(p)");
		
		BigInteger v = prf.getG().modPow(prf.getS(), prf.getP());
		BigInteger w = (prf.getT().multiply(prf.getVk().modPow(prf.getC(),prf.getP()))).mod(prf.getP());
		validProof  += (v.compareTo(w));
		
		LOGGER.log(Level.SEVERE, "Proof FAILED: Second part");
		
		boolean results = 0==validProof;
		
		return results;
	}
}
