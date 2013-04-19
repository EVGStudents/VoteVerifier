/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
