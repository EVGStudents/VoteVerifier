/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.implementer.ProofImplementer;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.implementer.SchnorrImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.Collections;
import java.util.List;

/**
 * This class represent the IndividualRunner used for an individual
 * verification.
 *
 * @author Scalzi Giuseppe
 */
public class IndividualRunner extends Runner {

	private final ParametersImplementer paramImpl;
	private final CertificatesImplementer certImpl;
	private final ElectionReceipt er;
	private final RSAImplementer rsaImpl;
	private final SchnorrImplementer schnorrImpl;
	private final ProofImplementer proofImpl;

	/**
	 * Construct an IndividualRunner with a given ElectionBoardProxy and
	 * Messenger.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param msgr the Messenger used to send the results.
	 */
	public IndividualRunner(ElectionBoardProxy ebp, Messenger msgr, ElectionReceipt er) {
		super(ebp, RunnerName.INDIVIDUAL, msgr);
		this.er = er;

		paramImpl = new ParametersImplementer(ebp, runnerName);
		certImpl = new CertificatesImplementer(ebp, runnerName);
		rsaImpl = new RSAImplementer(ebp, runnerName);
		schnorrImpl = new SchnorrImplementer(ebp, runnerName);
		proofImpl = new ProofImplementer(ebp, runnerName);
	}

	@Override
	public List<VerificationResult> run() throws InterruptedException {
		try {
			//Em certificate
			VerificationResult v1 = certImpl.vrfEMCertificate();
			msgr.sendVrfMsg(v1);
			partialResults.add(v1);
			Thread.sleep(SLEEP_TIME);

			//RSA Signature
			VerificationResult v2 = rsaImpl.vrfSingleBallotSign(er);
			msgr.sendVrfMsg(v2);
			partialResults.add(v2);
			Thread.sleep(SLEEP_TIME);

			//B belongs to ballots
			VerificationResult v3 = paramImpl.vrfBallotInSet(er.getVerificationKey());
			msgr.sendVrfMsg(v3);
			partialResults.add(v3);
			Thread.sleep(SLEEP_TIME);

			//Schnorr signature
			VerificationResult v4 = schnorrImpl.vrfBallotSignature(null, er);
			msgr.sendVrfMsg(v4);
			partialResults.add(v4);
			Thread.sleep(SLEEP_TIME);

			//proof
			VerificationResult v5 = proofImpl.vrfBallotProof(null, er);
			msgr.sendVrfMsg(v5);
			partialResults.add(v5);
			Thread.sleep(SLEEP_TIME);
		} catch (com.sun.xml.ws.client.ClientTransportException ex) {
			msgr.sendElectionSpecError(ex);
		}

		return Collections.unmodifiableList(partialResults);
	}
}
