/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.utils.ProofDiscreteLog;
import ch.bfh.univoteverifier.utils.RSASignature;
import ch.bfh.univoteverifier.utils.SchnorrSignature;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is able to perform all the verification of the
 * crypto primitives of UniVote
 * @author snake
 */
public class PrimitivesVerifier {

	private static final Logger LOGGER = Logger.getLogger(PrimitivesVerifier.class.getName());
	private static final int P_LENGTH = 1024; 
	private static final int Q_LENGTH = 256; 
	private static final int G_LENGTH = 1024; 
	
	/**
	 * verify a Non-Interactive Zero Knowledge Proof of Discrete Logs
	 * @param prf 
	 * @return true if the proof is correct
	 */
	public boolean vrfNIZKP(ProofDiscreteLog prf) throws NoSuchAlgorithmException {
		BigInteger c2;
		
		int validProof = 0;
		
		BigInteger[] concatB = {prf.vk,prf.t};
		c2 = CryptoFunc.concatArrayContents(concatB);
		
		validProof += c2.compareTo(prf.c);
	
		LOGGER.log(Level.SEVERE, "Proof FAILED: prf.c == vk.add(prf.t).mod(p)");
		
		BigInteger v = prf.g.modPow(prf.s, prf.p);
		BigInteger w = (prf.t.multiply(prf.vk.modPow(prf.c,prf.p))).mod(prf.p);
		validProof  += (v.compareTo(w));
		
		LOGGER.log(Level.SEVERE, "Proof FAILED: Second part");
		
		boolean results = 0==validProof;
		
		return results;
	}
	
	/**
	 * ToDo
	 * @param s
	 * @param mIn
	 * @return
	 */
	public boolean vrfRSASign(RSASignature s, BigInteger mIn){
		BigInteger ver = s.sig.modPow(s.e, s.n);
		
		boolean result = ver.equals(mIn);

		LOGGER.log(Level.SEVERE, "RSA Verification failed");
		
		return result;
	}
	
	/**
	 * Check if the parameters for the Schnorr's signature scheme
	 * are corrects by reading them from the configuration file
	 * @param pLen 
	 * @param qLen 
	 * @param gLen 
	 * @return boolean true if the parameters are correct, false otherwise
	 */
	public boolean vrfParamLen(int pLen, int qLen, int gLen){
		return pLen == P_LENGTH && qLen == Q_LENGTH && gLen == G_LENGTH;
	}
	
	/**
	 * Check if p is a prime number
	 * @param p 
	 * @return true if p is prime, false otherwise
	 */
	public boolean vrfPrimeNumber(BigInteger p){
		return p.isProbablePrime(100);
	}
	
	/**
	 * Check if p is a safe prime (p = k*q + 1)
	 * @param p 
	 * @param q 
	 * @return true if p is a safe prime, false otherwise
	 */
	public boolean vrfSafePrime(BigInteger p, BigInteger q){
		BigInteger multiple = p.subtract(BigInteger.valueOf(1)).divide(q);
		
		return multiple.multiply(q).add(BigInteger.valueOf(1)).equals(p);
	}
	
	
	/**
	 * Check if g is a generator of a subgroup H_q
	 * @param g 
	 * @param p 
	 * @param q 
	 * @return
	 */
	public boolean vrfGenerator(BigInteger g, BigInteger p, BigInteger q){
		BigInteger res = g.modPow(q, p);
		
		return res.equals(BigInteger.valueOf(1));
	}
	
	
	/**
	 * ToDO
	 * @return
	 */
	public boolean vrfCert(){
		return false;
	}
	
	/**
	 * Verify the given signature
	 * @param signature 
	 * @param message 
	 * @param publicKey 
	 * @return boolean return true if the signature is verified correctly, false otherwise
	 */
	public boolean vrfSchnorrSign(SchnorrSignature signature, BigInteger message, BigInteger publicKey) throws NoSuchAlgorithmException{
		
			LOGGER.log(Level.INFO, "The received signature is: {0}, {1}", new Object[]{signature.getA(), signature.getB()});
		
		BigInteger concat = Config.g.modPow(signature.getB(), Config.p).multiply(publicKey.modPow(signature.getA(), Config.p)).mod(Config.p);
		
		BigInteger hashResult = CryptoFunc.sha(new BigInteger(message.toString() + concat.toString()), Config.q);
		
		boolean res = hashResult.equals(signature.getA());
		
		return res;
	}
	
}
