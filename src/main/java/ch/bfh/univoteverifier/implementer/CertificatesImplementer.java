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

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
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
import java.util.Map.Entry;
import javax.naming.InvalidNameException;

/**
 * This class is used to check X509 certificates.
 *
 * @author snake
 */
public class CertificatesImplementer {

	private final ElectionBoardProxy ebp;

	public CertificatesImplementer(ElectionBoardProxy ebp) {
		this.ebp = ebp;
	}

	/**
	 * Verify a certificate path. The root certificate must be the last one
	 * in the list.
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
	 * Verify the certificate of the CA entity.
	 *
	 * @return a VerificationResult with the relative results.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 */
	public VerificationResult vrfCACertificate() throws ElectionBoardServiceFault, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
		X509Certificate caCert = CryptoFunc.getX509Certificate(ebp.getElectionSystemInfo().getCertificateAuthority().getValue());
		boolean r;

		try {
			List<X509Certificate> c = new ArrayList<>();
			c.add(caCert);
			r = vrfCert(c);
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
		}

		return new VerificationResult(VerificationType.SETUP_CA_CERT, r);
	}

	/**
	 * Verify the certificate of the EM entity.
	 *
	 * @return a VerificationResult with the relative results.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 */
	public VerificationResult vrfEMCertificate() throws ElectionBoardServiceFault, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
		X509Certificate emCert = CryptoFunc.getX509Certificate(ebp.getElectionSystemInfo().getElectionManager().getValue());
		boolean r;

		try {
			List<X509Certificate> c = new ArrayList<>();
			c.add(emCert);
			c.add(ebp.getCACert());
			r = vrfCert(c);
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
		}

		return new VerificationResult(VerificationType.SETUP_EM_CERT, r);
	}

	/**
	 * Verify the certificate of the EA entity.
	 *
	 * @return a VerificationResult with the relative results.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 */
	public VerificationResult vrfEACertificate() throws ElectionBoardServiceFault, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
		X509Certificate eaCert = CryptoFunc.getX509Certificate(ebp.getElectionSystemInfo().getElectionAdministration().getValue());
		boolean r;

		try {
			List<X509Certificate> c = new ArrayList<>();
			c.add(eaCert);
			c.add(ebp.getCACert());
			r = vrfCert(c);
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
		}

		return new VerificationResult(VerificationType.EL_SETUP_EA_CERT, r);
	}

	/**
	 * Verify the certificate of the talliers.
	 *
	 * @return a list of VerificationResult for each tallier.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 */
	public List<VerificationResult> vrfTalliersCertificates() throws ElectionBoardServiceFault, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidNameException {
		List<VerificationResult> talliersCert = new ArrayList<>();
		boolean r;

		//iterate through the certificates of the talliers
		for (Entry e : ebp.getTalliersCerts().entrySet()) {
			//check the certificate path
			try {
				List<X509Certificate> certPath = new ArrayList<>();
				certPath.add((X509Certificate) e.getValue());
				certPath.add(ebp.getCACert());
				r = vrfCert(certPath);
			} catch (CertPathValidatorException ex) {
				//we now that the certificate path verification has failed so the result is false
				r = false;
			}

			//create a VerificationResult and then set the entity name to the one we have
			VerificationResult vTallier = new VerificationResult(VerificationType.EL_SETUP_TALLIERS_CERT, r);
			vTallier.setEntityName((String) e.getKey());

			talliersCert.add(vTallier);
		}

		return talliersCert;
	}

	/**
	 * Verify the certificate of the mixers.
	 *
	 * @return a list of VerificationResult for each mixer.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 */
	public List<VerificationResult> vrfMixersCertificates() throws ElectionBoardServiceFault, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidNameException {
		List<VerificationResult> mixersCert = new ArrayList<>();
		boolean r;

		//iterate through the certificates of the mixer
		for (Entry e : ebp.getTalliersCerts().entrySet()) {
			//check the certificate path
			try {
				List<X509Certificate> certPath = new ArrayList<>();
				certPath.add((X509Certificate) e.getValue());
				certPath.add(ebp.getCACert());
				r = vrfCert(certPath);
			} catch (CertPathValidatorException ex) {
				//we now that the certificate path verification has failed so the result is false
				r = false;
			}

			//create a VerificationResult and then set the entity name to the one we have
			VerificationResult vMixer = new VerificationResult(VerificationType.EL_SETUP_MIXERS_CERT, r);
			vMixer.setEntityName((String) e.getKey());

			mixersCert.add(vMixer);
		}

		return mixersCert;
	}
}
