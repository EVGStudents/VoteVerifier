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

import ch.bfh.univote.common.Certificate;
import java.math.BigInteger;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class CertificatesImplementer {

	private static final Logger LOGGER = Logger.getLogger(ProofImplementer.class.getName());


	public boolean vrfCert(Certificate c, BigInteger publicKey){
		return false;
	}
	
}
