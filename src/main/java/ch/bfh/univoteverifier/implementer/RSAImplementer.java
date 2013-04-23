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
package ch.bfh.univoteverifier.implementer;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.StringConcatenator;
import ch.bfh.univoteverifier.utils.RSASignature;
import ch.bfh.univoteverifier.verification.VerificationEnum;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class holds all the methods that need a RSA Verification
 * @author snake
 */
public class RSAImplementer {
	
	private static final Logger LOGGER = Logger.getLogger(RSAImplementer.class.getName());
	private final ElectionBoardProxy ebp;
	private final StringConcatenator sc;
	
	/**
	 * Create a new RSAImplementer
	 * @param ebp the election board proxy from where get the data
	 */
	public RSAImplementer(ElectionBoardProxy ebp){
		this.ebp = ebp;
		sc = new StringConcatenator();
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

	/**
	 * Verify a RSA signature
	 * @param s the RSAPublicKey
	 * @param clearText the text from where to get the signature
	 * @param signature the pre-computed signature from the board
	 * @return 
	 */
	public boolean vrfRSASign(RSAPublicKey s, BigInteger clearText, BigInteger signature){
		BigInteger ver = clearText.modPow(s.getPublicExponent(), s.getModulus());
		
		boolean result = ver.equals(signature);
		
		return result;
	}
	
	
	/**
	 * Verify the signature of the election administrator certificate
	 * @return a verification result
	 * @throws ElectionBoardServiceFault
	 * @throws CertificateException
	 */
	public VerificationResult vrfEACertSign() throws ElectionBoardServiceFault, CertificateException{
		//get the certificte as a string
		String eaCertStr = CryptoFunc.getX509Certificate(ebp.getElectionSystemInfo().getElectionAdministration().getValue()).toString();
		String eID = ebp.getElectionDefinition().getElectionId();
	
		sc.pushObject(StringConcatenator.LEFT_DELIMITER);
		sc.pushObject(eID);
		sc.pushObject(StringConcatenator.INNER_DELIMITER);
		sc.pushObject(eaCertStr);
		sc.pushObject(StringConcatenator.RIGHT_DELIMITER);
		
		String strRes = sc.pullAll();
		BigInteger bi = new BigInteger(strRes.getBytes());
		
		//this is in the EA certificate - ToDo change it
		BigInteger signature = new BigInteger("1");
		
		boolean r = vrfRSASign(Config.em,bi,signature);
		
		return new VerificationResult(VerificationEnum.EL_SETUP_EA_CERT_SIGN, true);
	}
}
