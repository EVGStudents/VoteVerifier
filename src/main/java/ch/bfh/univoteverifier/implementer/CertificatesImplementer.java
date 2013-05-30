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

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.VoterCertificates;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Report;
import ch.bfh.univoteverifier.common.RunnerName;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * This class is used to check X509 certificates.
 *
 * @author Scalzi Giuseppe
 */
public class CertificatesImplementer extends Implementer {

	/**
	 * Create a new CertificatesImplementer with a given ElectionBoardProxy
	 * and RunnerName.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param rn the RunnerName who needs this implementer.
	 */
	public CertificatesImplementer(ElectionBoardProxy ebp, RunnerName rn) {
		super(ebp, rn, ImplementerType.CERTIFICATE);
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
	 * Specification: 1.3.2, a.
	 *
	 * @return a VerificationResult with the relative results.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 */
	public VerificationResult vrfCACertificate() {
		boolean r = false;
		Exception exc = null;
		CertPathValidatorException invalidCert = null;
		Report rep;

		try {
			X509Certificate caCert = ebp.getCACert();
			List<X509Certificate> c = new ArrayList<>();
			c.add(caCert);
			r = vrfCert(c);
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
			invalidCert = ex;
		} catch (NoSuchAlgorithmException | ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.SETUP_CA_CERT, r, ebp.getElectionID(), rn, it, EntityType.CA);


		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_CERTIFICATE);
			rep.setAdditionalInformation(invalidCert.getMessage());
			v.setReport(rep);
		}

		return v;

	}

	/**
	 * Verify the certificate of the EM entity.
	 *
	 * Specification: 1.3.2, b.
	 *
	 * @return a VerificationResult with the relative results.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfEMCertificate() {
		Exception exc = null;
		boolean r = false;
		CertPathValidatorException invalidCert = null;
		Report rep;

		try {
			X509Certificate emCert = ebp.getEMCert();
			List<X509Certificate> c = new ArrayList<>();
			c.add(emCert);
			r = vrfCert(c);
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
			invalidCert = ex;
		} catch (NoSuchAlgorithmException | ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.SETUP_EM_CERT, r, ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_CERTIFICATE);
			rep.setAdditionalInformation(invalidCert.getMessage());
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the certificate of the EA entity.
	 *
	 * Specification: 1.3.4, a.
	 *
	 * @return a VerificationResult with the relative results.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfEACertificate() {
		boolean r = false;
		Exception exc = null;
		CertPathValidatorException invalidCert = null;
		Report rep;

		try {
			X509Certificate eaCert = ebp.getEACert();
			List<X509Certificate> c = new ArrayList<>();
			c.add(eaCert);
			r = vrfCert(c);
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
			invalidCert = ex;
		} catch (NoSuchAlgorithmException | ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_EA_CERT, r, ebp.getElectionID(), rn, it, EntityType.EA);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_CERTIFICATE);
			rep.setAdditionalInformation(invalidCert.getMessage());
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the certificate of a talliers
	 *
	 * Specification: 1.3.4, b.
	 *
	 * @param tallierName The name of tallier.
	 * @return a VerificationResult.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfTallierCertificate(String tallierName) {
		Exception exc = null;
		boolean r = false;
		CertPathValidatorException invalidCert = null;
		Report rep;

		//check the certificate path
		try {
			List<X509Certificate> certPath = new ArrayList<>();
			certPath.add(ebp.getTalliersCerts().get(tallierName));
			r = vrfCert(certPath);
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
			invalidCert = ex;
		} catch (NoSuchAlgorithmException | ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException ex) {
			exc = ex;
		}

		//create a VerificationResult and then set the entity name to the one we have
		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_TALLIERS_CERT, r, ebp.getElectionID(), rn, it, EntityType.TALLIER);
		v.setEntityName(tallierName);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_CERTIFICATE);
			rep.setAdditionalInformation(invalidCert.getMessage());
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the certificate of a mixer.
	 *
	 * Specification: 1.3.4, b.
	 *
	 * @param mixerName The name of the mixer.
	 * @return a VerificationResult.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfMixerCertificate(String mixerName) {
		Exception exc = null;
		boolean r = false;
		CertPathValidatorException invalidCert = null;
		Report rep;

		//check the certificate path
		try {
			List<X509Certificate> certPath = new ArrayList<>();
			certPath.add(ebp.getMixersCerts().get(mixerName));
			r = vrfCert(certPath);
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
			invalidCert = ex;
		} catch (NoSuchAlgorithmException | ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException ex) {
			exc = ex;
		}

		//create a VerificationResult and then set the entity name to the one we have
		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_MIXERS_CERT, r, ebp.getElectionID(), rn, it, EntityType.MIXER);
		v.setEntityName(mixerName);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_CERTIFICATE);
			rep.setAdditionalInformation(invalidCert.getMessage());
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the certificate of the voters.
	 *
	 * Specification: 1.3.5, c.
	 *
	 * @return a VerificationResult.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfVotersCertificate() {
		Exception exc = null;
		CertPathValidatorException invalidCert = null;
		boolean r = false;
		Report rep;

		try {
			VoterCertificates vc = ebp.getVoterCerts();

			for (Certificate c : vc.getCertificate()) {
				X509Certificate xCert = CryptoFunc.getX509Certificate(c.getValue(), false);

				//check the certificate path
				List<X509Certificate> certPath = new ArrayList<>();
				certPath.add(xCert);
				certPath.add(ebp.getCACert());
				r = vrfCert(certPath);
			}
		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
			invalidCert = ex;
			Logger.getLogger(this.getClass().getName()).log(Level.INFO, ex.getMessage());
		} catch (NoSuchAlgorithmException | ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException ex) {
			exc = ex;
		}

		//create a VerificationResult and then set the entity name to the one we have
		VerificationResult v = new VerificationResult(VerificationType.EL_PREP_VOTERS_CERT, r, ebp.getElectionID(), rn, it, EntityType.CA);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_CERTIFICATE);
			rep.setAdditionalInformation(invalidCert.getMessage());
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the certificates of lately registered voters.
	 *
	 * Specification: 1.3.6, a.
	 *
	 * @return a VerificationResult.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 * @throws InvalidAlgorithmParameterException if the parameters for the
	 * PKIX algorithm are not correct.
	 * @throws NoSuchAlgorithmException if the algorithm specified for the
	 * certificate path validator doesn't exist.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfLatelyRegisteredVotersCertificate() {
		Exception exc = null;
		boolean r = false;
		CertPathValidatorException invalidCert = null;
		Report rep;

		try {
			List<Certificate> vc = ebp.getLatelyRegisteredVoterCerts();
			for (Certificate c : vc) {
				X509Certificate xCert = CryptoFunc.getX509Certificate(c.getValue(), true);
				//check the certificate path
				List<X509Certificate> certPath = new ArrayList<>();
				certPath.add(xCert);
				certPath.add(ebp.getCACert());
				r = vrfCert(certPath);
			}

		} catch (CertPathValidatorException ex) {
			//we now that the certificate path verification has failed so the result is false
			r = false;
			invalidCert = ex;
		} catch (NullPointerException | SOAPFaultException | NoSuchAlgorithmException | ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException ex) {
			exc = ex;
		}

		//create a VerificationResult and then set the entity name to the one we have
		VerificationResult v = new VerificationResult(VerificationType.EL_PERIOD_LATE_NEW_VOTER_CERT, r, ebp.getElectionID(), rn, it, EntityType.CA);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_CERTIFICATE);
			rep.setAdditionalInformation(invalidCert.getMessage());
			v.setReport(rep);
		}

		return v;
	}
}
