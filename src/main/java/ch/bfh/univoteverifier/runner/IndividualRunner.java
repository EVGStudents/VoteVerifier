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
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.implementer.SchnorrImplementer;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;

/**
 * This class represent the IndividualRunner used for an individual
 * verification.
 *
 * @author snake
 */
public class IndividualRunner extends Runner {

	private final ParametersImplementer paramImpl;
	private final CertificatesImplementer certImpl;
	private final ElectionReceipt er;
	private final RSAImplementer rsaImpl;
	private final SchnorrImplementer schnorrImpl;

	/**
	 * Construct an IndividualRunner with a given ElectionBoardProxy and
	 * Messenger.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param msgr the Messenger used to send the results.
	 */
	public IndividualRunner(ElectionBoardProxy ebp, Messenger msgr, ElectionReceipt er) throws CertificateException, ElectionBoardServiceFault, InvalidNameException {
		super(RunnerName.INDIVIDUAL, msgr);
		this.er = er;

		paramImpl = new ParametersImplementer(ebp, runnerName);
		certImpl = new CertificatesImplementer(ebp, runnerName);
		rsaImpl = new RSAImplementer(ebp, runnerName);
		schnorrImpl = new SchnorrImplementer(ebp, runnerName);
	}

	@Override
	public List<VerificationResult> run() {
		try {
			//Em certificate
			VerificationResult v1 = certImpl.vrfEMCertificate();
			msgr.sendVrfMsg(v1);
			partialResults.add(v1);
			Thread.sleep(1000);

			//RSA Signature - ToDo
			VerificationResult v2 = rsaImpl.vrfSingleBallotSign(er);
			msgr.sendVrfMsg(v2);
			partialResults.add(v2);
			Thread.sleep(1000);

			//B belongs to ballots - ToDo
			VerificationResult v3 = paramImpl.vrfBallotInSet(er.getVk());

			//Schnorr signature - ToDo
			VerificationResult v4;

		} catch (UnsupportedEncodingException | InterruptedException | ElectionBoardServiceFault | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException ex) {
			Logger.getLogger(IndividualRunner.class.getName()).log(Level.SEVERE, null, ex);
		}

		return Collections.unmodifiableList(partialResults);
	}
}
