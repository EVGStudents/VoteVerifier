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
package ch.bfh.univoteverifier.utils;

import java.math.BigInteger;

/**
 * Class used to represent the Schnorr's signature
 * @author Scalzi Giuseppe
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