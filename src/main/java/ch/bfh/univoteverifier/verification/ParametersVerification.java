/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import java.math.BigInteger;

/**
 *
 * @author snake
 */
public class ParametersVerification {

	private final ElectionBoardProxy ebp;

	public ParametersVerification(ElectionBoardProxy ebp){
		this.ebp = ebp;	
	}

	/**
	 * Check if the parameters for the Schnorr's signature scheme
	 * are corrects by reading them from the configuration file
	 * @return boolean true if the parameters are correct, false otherwise
	 */
	public VerificationResult vrfParamLen(){
		int lengthPG = 1024;
		int lengthQ = 256;
		
		boolean r = Config.p.bitLength() == lengthPG && Config.q.bitLength() == lengthQ && Config.g.bitLength() == lengthPG;

		return new VerificationResult(VerificationEnum.SETUP_PARAM_LEN, r);
	}
	
	/**
	 * Check if p is a prime number
	 * @return true if p is prime, false otherwise
	 */
	public VerificationResult vrfPrimeP(){
		boolean r = Config.p.isProbablePrime(100);
		return new VerificationResult(VerificationEnum.SETUP_PARAM_LEN, r);
	}
		
	/**
	 * Check if q is a prime number
	 * @return true if q is prime, false otherwise
	 */
	public VerificationResult vrfPrimeQ(){
		boolean r = Config.q.isProbablePrime(100);
		return new VerificationResult(VerificationEnum.SETUP_PARAM_LEN, r);
	}

	/**
	 * Check if p is a safe prime (p = k*q + 1)
	 * @return true if p is a safe prime, false otherwise
	 */
	public VerificationResult vrfSafePrime(){
		BigInteger multiple = Config.p.subtract(BigInteger.valueOf(1)).divide(Config.q);
		
		boolean r = multiple.multiply(Config.q).add(BigInteger.valueOf(1)).equals(Config.p);
		return new VerificationResult(VerificationEnum.SETUP_PARAM_LEN, r);
	}
	
	
	/**
	 * Check if g is a generator of a subgroup H_q
	 * @return is g is a valid generator
	 */
	public VerificationResult vrfGenerator(){
		BigInteger res = Config.g.modPow(Config.q, Config.p);
		
		boolean r = res.equals(BigInteger.valueOf(1));
		return new VerificationResult(VerificationEnum.SETUP_PARAM_LEN, r);
	}
	
}
