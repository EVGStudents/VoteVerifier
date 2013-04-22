/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.implementer;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.utils.RSASignature;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class holds all the methods that need a RSA Verification
 * @author snake
 */
public class RSAImplementer {

	private static final Logger LOGGER = Logger.getLogger(RSAImplementer.class.getName());
	private ElectionBoardProxy ebp;

	/**
	 * Create a new RSAImplementer 
	 * @param ebp the election board proxy from where get the data
	 */
	public RSAImplementer(ElectionBoardProxy ebp){
			this.ebp = ebp;
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
}
