package crypto_vrf;

import java.math.BigInteger;

import common.Config;

public class PrimitivesVrf {

	/**
	 * Check if the parameters for the Schnorr's signature scheme 
	 * are corrects by reading them from the configuration file
	 * @return boolean true if the parameters are correct, false otherwise
	 */
	public boolean areParametersLength(){
			
		if(Config.p.bitLength() == Config.pLength){
			if(Config.q.bitLength() == Config.qLength){
				if(Config.g.bitLength() == Config.gLength){
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
	public boolean isSchnorrPPrime(){
		return Config.p.isProbablePrime(100);
	}
	
	/**
	 * Check if q is a prime number
	 * @return true if q is a prime number, false otherwise
	 */
	public boolean isSchnorrQPrime(){
		return Config.q.isProbablePrime(100);
	}
	
	/**
	 * Check if p is a safe prime (p = k*q + 1) 
	 * @return true if p is a safe prime, false otherwise
	 */
	public boolean isPSafePrime(){
		BigInteger multiple = Config.p.subtract(BigInteger.valueOf(1)).divide(Config.q);
		
		System.out.println(multiple);
		
		if(multiple.multiply(Config.q).add(BigInteger.valueOf(1)).equals(Config.p)){
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Check if g is a generator of a subgroup H_q 
	 * @return
	 */
	public boolean isGenerator(){
		BigInteger res = Config.g.modPow(Config.q, Config.p);
		
		if(res.equals(BigInteger.valueOf(1))){
			return true;
		}
		
		return false;
	}
	

}
