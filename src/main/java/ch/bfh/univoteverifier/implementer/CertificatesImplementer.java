/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.implementer;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class is used to check X509 certificates
 * @author snake
 */
public class CertificatesImplementer {

	private static final Logger LOGGER = Logger.getLogger(CertificatesImplementer.class.getName());

	/**
	 * Verify a certificate
	 * @param c the list of certificates that compose the certification path. The last certificate
	 * of the list must be the CA certificate
	 * @return true if the certificate algorithm path validation succeed
	 * @throws CertificateException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchAlgorithmException
	 * @throws CertPathValidatorException 
	 */
	public boolean vrfCert(List<X509Certificate> c) throws CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CertPathValidatorException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		//generate the certification path
		CertPath cp = cf.generateCertPath(c);

		//get the CA certificate as trust anchor
		X509Certificate trust = c.get(c.size()-1);
		TrustAnchor anchor = new TrustAnchor(trust, null);
		
		PKIXParameters params = new PKIXParameters(Collections.singleton(anchor));
		params.setRevocationEnabled(false);

		//create a new instance of the certificate path validator algorithm to check the certificates
		CertPathValidator cpv = CertPathValidator.getInstance("PKIX");

		//this method throw an exception if the certification path doesn't validate
		cpv.validate(cp, params);
		
		return true;
	}

}
