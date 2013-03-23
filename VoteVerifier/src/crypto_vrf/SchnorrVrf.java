package crypto_vrf;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import utils.CryptoUtils;
import utils.SchnorrGenerator;
import utils.SchnorrSignature;

import common.Config;

public class SchnorrVrf {
	
	/**
	 * Temp
	 */
	private BigInteger publicKey = SchnorrGenerator.publicKey;


	/**
	 * Verify the given signature
	 * @return boolean return true if the signature is verified correctly, false otherwise
	 */
	public boolean verifySchnorrSignature(SchnorrSignature signature, BigInteger message){
		
		if(Config.DEBUG_MODE)
			System.out.println("The received signature is: " + signature.getA() + ", " + signature.getB() );
		
		BigInteger concat = Config.g.modPow(signature.getB(), Config.p).multiply(publicKey.modPow(signature.getA(), Config.p)).mod(Config.p);
		
		BigInteger hashResult = CryptoUtils.sha(new BigInteger(message.toString() + concat.toString()));
		
		boolean res = hashResult.equals(signature.getA());
		
		return res;
	}
	
	
	
}
