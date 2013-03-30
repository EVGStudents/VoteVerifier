package crypto_vrf;

import java.math.BigInteger;

import common.Verification;


public class PrimitivesVrf {
	
	private BigInteger p;
	private BigInteger q;
	private BigInteger g;
	
	/**
	 * Contruct a PrimitivesVrf object used to check
	 * the p,q and g values
	 * @param p BigInteger the prime number p
	 * @param q BigInteger the prime number q
	 * @param g BigInteger the prime number g
	 */
	public PrimitivesVrf(BigInteger p, BigInteger q, BigInteger g){
		this.p = p;
		this.q = q;
		this.g = g;
	}
	
	/**
	 * Check if the parameters for the Schnorr's signature scheme 
	 * are corrects by reading them from the configuration file
	 * @return boolean true if the parameters are correct, false otherwise
	 */
	public boolean areParametersLength(int pLength, int qLength, int gLength){
			
		if(p.bitLength() == pLength){
			if(q.bitLength() == qLength){
				if(g.bitLength() == gLength){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Check if p is a prime number
	 * @return true if p is prime, false otherwise
	 */
	public boolean isPPrime(){
		return p.isProbablePrime(100);
	}
	
	/**
	 * Check if q is a prime number
	 * @return true if q is a prime number, false otherwise
	 */
	public boolean isQPrime(){
		return q.isProbablePrime(100);
	}
	
	/**
	 * Check if p is a safe prime (p = k*q + 1) 
	 * @return true if p is a safe prime, false otherwise
	 */
	public boolean isPSafePrime(){
		BigInteger multiple = p.subtract(BigInteger.valueOf(1)).divide(q);
				
		if(multiple.multiply(q).add(BigInteger.valueOf(1)).equals(p)){
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Check if g is a generator of a subgroup H_q 
	 * @return
	 */
	public boolean isGenerator(){
		BigInteger res = g.modPow(q, p);
		
		if(res.equals(BigInteger.valueOf(1))){
			return true;
		}
		
		return false;
	}
	

}
