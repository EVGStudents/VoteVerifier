/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.implementer;

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.utils.SchnorrSignature;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class SchnorrImplementer {
	
	
	private static final Logger LOGGER = Logger.getLogger(SchnorrImplementer.class.getName());
	private final ElectionBoardProxy ebp;
	
	/**
	 * Construct a schnorr implementer
	 * @param ebp the election board proxy from where get the data
	 */
	public SchnorrImplementer(ElectionBoardProxy ebp){
		this.ebp =  ebp;
	}
	
	/**
	 * Verify the given signature
	 * @param signature the signature to check
	 * @param message the content to sign
	 * @param publicKey the public key used to verify the signature
	 * @return boolean return true if the signature is verified correctly, false otherwise
	 */
	public boolean vrfSchnorrSign(SchnorrSignature signature, BigInteger message, BigInteger publicKey) throws NoSuchAlgorithmException{
		
		BigInteger concat = Config.g.modPow(signature.getB(), Config.p).multiply(publicKey.modPow(signature.getA(), Config.p)).mod(Config.p);
		
		BigInteger hashResult = CryptoFunc.sha(new BigInteger(message.toString() + concat.toString()));
		
		boolean res = hashResult.equals(signature.getA());
		
		return res;
	}
}
