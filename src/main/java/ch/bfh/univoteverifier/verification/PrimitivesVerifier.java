/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.utils.CryptoUtils;
import ch.bfh.univoteverifier.utils.ProofDiscreteLog;
import ch.bfh.univoteverifier.utils.RSASignature;
import ch.bfh.univoteverifier.utils.SchnorrSignature;
import java.math.BigInteger;

/**
 * This class is able to perform all the verification of the
 * crypto primitives of UniVote
 * @author snake
 */
public class PrimitivesVerifier {
	
	/**
	 * verify a Non-Interactive Zero Knowledge Proof of Discrete Logs
	 * @param proof and helper class object with fields t,c,s corresponding to a ZKP
	 * @return true if the proof is correct
	 */
	public boolean vrfNIZKP(ProofDiscreteLog prf){
		BigInteger c2;
		
		int validProof = 0;
		
		BigInteger[] concatB = {prf.vk,prf.t};
		c2 = CryptoUtils.concatArrayContents(concatB);
		
		validProof += c2.compareTo(prf.c);
		
		if(Config.DEBUG_MODE == true && validProof != 0)
			System.out.println("FAILED: prf.c == vk.add(prf.t).mod(p)");
		
		BigInteger v = prf.g.modPow(prf.s, prf.p);
		BigInteger w = (prf.t.multiply(prf.vk.modPow(prf.c,prf.p))).mod(prf.p);
		validProof  += (v.compareTo(w));
		
		if(Config.DEBUG_MODE == true)
			if (validProof!=0)System.out.println("FAILED: Second Part");
		
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
		
		if(Config.DEBUG_MODE == true)
			System.out.println(result);
		
		return result;
	}
	
	/**
	 * Check if the parameters for the Schnorr's signature scheme
	 * are corrects by reading them from the configuration file
	 * @return boolean true if the parameters are correct, false otherwise
	 */
	public boolean vrfParamLen(BigInteger p, BigInteger q, BigInteger g,int pLength, int qLength, int gLength){
		return p.bitLength() == pLength && q.bitLength() == qLength && g.bitLength() == gLength;
	}
	
	/**
	 * Check if p is a prime number
	 * @return true if p is prime, false otherwise
	 */
	public boolean vrfPrimeNumber(BigInteger p){
		return p.isProbablePrime(100);
	}
	
	/**
	 * Check if p is a safe prime (p = k*q + 1)
	 * @return true if p is a safe prime, false otherwise
	 */
	public boolean vrfSafePrime(BigInteger p, BigInteger q){
		BigInteger multiple = p.subtract(BigInteger.valueOf(1)).divide(q);
		
		return multiple.multiply(q).add(BigInteger.valueOf(1)).equals(p);
	}
	
	
	/**
	 * Check if g is a generator of a subgroup H_q
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
	 * @return boolean return true if the signature is verified correctly, false otherwise
	 */
	public boolean vrfSchnorrSign(SchnorrSignature signature, BigInteger message, BigInteger publicKey){
		
		if(Config.DEBUG_MODE)
			System.out.println("The received signature is: " + signature.getA() + ", " + signature.getB() );
		
		BigInteger concat = Config.g.modPow(signature.getB(), Config.p).multiply(publicKey.modPow(signature.getA(), Config.p)).mod(Config.p);
		
		BigInteger hashResult = CryptoUtils.sha(new BigInteger(message.toString() + concat.toString()));
		
		boolean res = hashResult.equals(signature.getA());
		
		return res;
	}
	
}
