/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;

/**
 * This class represent an ElectionSetupRunner.
 *
 * @author snake
 */
public class ElectionSetupRunner extends Runner {

	private final RSAImplementer rsaImpl;
	private final CertificatesImplementer certImpl;
	private final ParametersImplementer prmImpl;

	/**
	 * Construct an ElectionSetupRunner with a given ElectionBoardProxy.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 */
	public ElectionSetupRunner(ElectionBoardProxy ebp, Messenger msgr) {
		super(RunnerName.ELECTION_SETUP, msgr);
		rsaImpl = new RSAImplementer(ebp);
		certImpl = new CertificatesImplementer(ebp);
		prmImpl = new ParametersImplementer();
	}

	@Override
	public List<VerificationResult> run() {

		//EA certificate verification
		try {
			VerificationResult v1 = certImpl.vrfEACertificate();
			msgr.sendVrfMsg(v1);
		} catch (CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | ElectionBoardServiceFault ex) {
			Logger.getLogger(ElectionSetupRunner.class.getName()).log(Level.SEVERE, null, ex);
		}

		//RSA signature of (id|EA)
		try {
			VerificationResult v2 = rsaImpl.vrfEACertIDSign();
			msgr.sendVrfMsg(v2);
		} catch (ElectionBoardServiceFault | CertificateException | NoSuchAlgorithmException | UnsupportedEncodingException ex) {
			Logger.getLogger(ElectionSetupRunner.class.getName()).log(Level.SEVERE, null, ex);
		}

		//RSA signature of (id|descr|l|T|M)
		try {
			VerificationResult v3 = rsaImpl.vrfBasicParamSign();
			msgr.sendVrfMsg(v3);
		} catch (ElectionBoardServiceFault | NoSuchAlgorithmException | UnsupportedEncodingException ex) {
			Logger.getLogger(ElectionSetupRunner.class.getName()).log(Level.SEVERE, null, ex);
		}

		//talliers certificates
		try {
			List<VerificationResult> tCertsEvent = certImpl.vrfTalliersCertificates();

			for (VerificationResult v : tCertsEvent) {
				msgr.sendVrfMsg(v);
			}
		} catch (ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | InvalidNameException ex) {
			Logger.getLogger(ElectionSetupRunner.class.getName()).log(Level.SEVERE, null, ex);
		}

		//mixers certificates
		try {
			List<VerificationResult> mCertsEvent = certImpl.vrfMixersCertificates();

			for (VerificationResult v : mCertsEvent) {
				msgr.sendVrfMsg(v);
			}
		} catch (ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | InvalidNameException ex) {
			Logger.getLogger(ElectionSetupRunner.class.getName()).log(Level.SEVERE, null, ex);
		}

		//RSA Signature of (id|Z_T|Z_M)
		//ToDO


		//ElGamal parameters
		//ToDO





		return null;
	}
}
