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

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationEvent;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to check X509 certificates.
 *
 * @author snake
 */
public class CertificatesImplementer {

	private static final Logger LOGGER = Logger.getLogger(CertificatesImplementer.class.getName());

	/**
	 * Verify a certificate.
	 *
	 * @param c the certificate to be verified.
	 * @return true if the certificate algorithm path validation succeed.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 * @throws CertPathValidatorException if the certificate path doesn't
	 * validate.
	 */
	public boolean vrfCert(List<X509Certificate> certList) throws CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CertPathValidatorException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		//generate the certification path
		CertPath cp = cf.generateCertPath(certList);

		//get the CA certificate as trust anchor
		TrustAnchor anchor = new TrustAnchor(certList.get(certList.size() - 1), null);

		PKIXParameters params = new PKIXParameters(Collections.singleton(anchor));

		//we don't have CRL for univote, so disable revocation mechanism
		params.setRevocationEnabled(false);

		//create a new instance of the certificate path validator algorithm to check the certificates
		//PKIX is the algorithm used to check X.509 certificates path
		CertPathValidator cpv = CertPathValidator.getInstance("PKIX");

		cpv.validate(cp, params);

		return true;
	}

	/**
	 * Verify the ElectionManager certificate
	 *
	 * @return a VerificationEvent with the relative results
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 */
	public VerificationEvent vrfEMCert() throws CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
		boolean r = false;

		try {
			List<X509Certificate> c = new ArrayList<>();
			c.add(Config.emCert);
			c.add(Config.caCert);
			r = vrfCert(c);
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
			LOGGER.log(Level.SEVERE, null, ex);
		}

		VerificationEvent v = new VerificationEvent(VerificationType.SETUP_EM_CERT, r);
		return v;

	}

	/**
	 * Verify the ElectionAdministrator certificate
	 *
	 * @return a VerificationEvent with the relative results
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 */
	public VerificationEvent vrfEACert() throws CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
		boolean r = false;

		try {
			List<X509Certificate> c = new ArrayList<>();
			c.add(Config.eaCert);
			c.add(Config.caCert);
			r = vrfCert(c);
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
			LOGGER.log(Level.SEVERE, null, ex);
		}

		VerificationEvent v = new VerificationEvent(VerificationType.EL_SETUP_EA_CERT, r);
		return v;
	}
}
