package crypto_vrf;

import java.math.BigInteger;

import common.Config;

public class PrimitivesVrf {

	
	public boolean areParametersLength(){
		System.out.println(Config.p.bitLength());
		
		if(Config.p.bitLength() == Config.pLength){
			if(Config.q.bitLength() == Config.qLength){
				if(Config.g.bitLength() == Config.gLength){
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	public boolean isSchnorrPPrime(){
		return Config.p.isProbablePrime(100);
	}
	
	
	public boolean isSchnorrQPrime(){
		return Config.q.isProbablePrime(100);
	}
	
	public boolean isPSafePrime(){
		BigInteger multiple = Config.p.subtract(BigInteger.valueOf(1)).divide(Config.q);
		
		System.out.println(multiple);
		
		if(multiple.multiply(Config.q).add(BigInteger.valueOf(1)).equals(Config.p)){
			return true;
		}
		
		return false;
	}
	

}
