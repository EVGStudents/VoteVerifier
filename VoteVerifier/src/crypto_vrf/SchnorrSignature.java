package crypto_vrf;

import java.math.BigInteger;

/**
 * Class used to represent the Schnorr's signature
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