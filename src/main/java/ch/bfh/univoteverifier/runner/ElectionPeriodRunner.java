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

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.Report;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.implementer.ProofImplementer;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.implementer.SchnorrImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.Collections;
import java.util.List;

/**
 * This class represent an ElectionPeriodRunner.
 *
 * @author Scalzi Giuseppe
 */
public class ElectionPeriodRunner extends Runner {

	private final CertificatesImplementer certImpl;
	private final RSAImplementer rsaImpl;
	private final ProofImplementer proofImpl;
	private final ParametersImplementer prmImpl;
	private final SchnorrImplementer schnImpl;

	/**
	 * Construct an ElectionPeriodRunner with a given ElectionBoardProxy and
	 * Messenger.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param msgr the Messenger used to send the results.
	 */
	public ElectionPeriodRunner(ElectionBoardProxy ebp, Messenger msgr) {
		super(ebp, RunnerName.ELECTION_PERIOD, msgr);
		certImpl = new CertificatesImplementer(ebp, runnerName);
		rsaImpl = new RSAImplementer(ebp, runnerName);
		proofImpl = new ProofImplementer(ebp, runnerName);
		prmImpl = new ParametersImplementer(ebp, runnerName);
		schnImpl = new SchnorrImplementer(ebp, runnerName);
	}

	@Override
	public List<VerificationResult> run() throws InterruptedException {
		try {
			//lately registered voters certificate
			VerificationResult v1 = certImpl.vrfLatelyRegisteredVotersCertificate();
			msgr.sendVrfMsg(v1);
			partialResults.add(v1);
			Thread.sleep(SLEEP_TIME);

			//RSA signature of lately registered voters certificate and id
			VerificationResult v2 = rsaImpl.vrfLatelyRegisteredVotersCertificateSign();
			msgr.sendVrfMsg(v2);
			partialResults.add(v2);
			Thread.sleep(SLEEP_TIME);

			//NIZKP of late registered verification key and signature - Proof Not yet available
			for (String mName : ebp.getElectionDefinition().getMixerId()) {
				VerificationResult v3 = proofImpl.vrfLatelyVerificationKeysByProof(mName);
				msgr.sendVrfMsg(v3);
				partialResults.add(v3);
				Thread.sleep(SLEEP_TIME);

				//signature
				VerificationResult v4 = rsaImpl.vrfLatelyVerificationKeysBySign(mName);
				msgr.sendVrfMsg(v4);
				partialResults.add(v4);
				Thread.sleep(SLEEP_TIME);
			}

			//check that the late mixed verification key set is equal to the set of last mixer
			VerificationResult v5 = prmImpl.vrfLatelyVerificatonKeys();
			msgr.sendVrfMsg(v5);
			partialResults.add(v5);
			Thread.sleep(SLEEP_TIME);

			//signature over late mixed verification key set
			VerificationResult v6 = rsaImpl.vrfLatelyVerificationKeysSign();
			msgr.sendVrfMsg(v6);
			partialResults.add(v6);
			Thread.sleep(SLEEP_TIME);

			//NIZKP of late  renewal of registration and signature for each mixer
			for (String mName : ebp.getElectionDefinition().getMixerId()) {
				//proof
				VerificationResult v10 = proofImpl.vrfLateRenewalOfRegistrationProofBy(mName);
				msgr.sendVrfMsg(v10);
				partialResults.add(v10);
				Thread.sleep(SLEEP_TIME);

				//rsa signature
				VerificationResult v11 = rsaImpl.vrfLateRenewalOfRegistrationSignBy(mName);
				msgr.sendVrfMsg(v11);
				partialResults.add(v11);
				Thread.sleep(SLEEP_TIME);
			}

			//check over last mixer late renewal verification key set
			VerificationResult v12 = prmImpl.vrfLateRenewalOfRegistrationKeys();
			msgr.sendVrfMsg(v12);
			partialResults.add(v12);
			Thread.sleep(SLEEP_TIME);

			//signature of last late renewal verification key set
			VerificationResult v13 = rsaImpl.vrfLateRenewalOfRegistrationSign();
			msgr.sendVrfMsg(v13);
			partialResults.add(v13);
			Thread.sleep(SLEEP_TIME);

			//Ballots verifications
			boolean result = true;
			Exception exc = null;
			Report rep;

			try {
				for (Ballot b : ebp.getBallots().getBallot()) {
					boolean vkVerification = prmImpl.vrfBallotVerificationKey(b.getVerificationKey()).getResult();

					//we want to verify the proof that come from a ballot and not from a QR-Code so ElectionReceipt is null.
					boolean signatureVerification = schnImpl.vrfBallotSignature(b, null).getResult();
					boolean proofVerification = proofImpl.vrfBallotProof(b, null).getResult();

					//if one of these checks fail, break andcreate the verification result
					if (!(vkVerification && signatureVerification && proofVerification)) {
						result = false;
						break;
					}
				}
			} catch (NullPointerException ex) {
				exc = ex;
			}


			VerificationResult v7 = new VerificationResult(VerificationType.EL_PERIOD_BALLOT, result, ebp.getElectionID(), runnerName, ImplementerType.PARAMETER, EntityType.VOTERS);

			if (exc != null) {
				rep = new Report(exc);
				v7.setReport(rep);
			} else if (!result) {
				rep = new Report(FailureCode.INVALID_ALL_BALLOTS);
				v7.setReport(rep);
			}
			msgr.sendVrfMsg(v7);
			partialResults.add(v7);
			Thread.sleep(SLEEP_TIME);

			//signature over ballots set
			VerificationResult v8 = rsaImpl.vrfBallotsSetSign();
			msgr.sendVrfMsg(v8);
			partialResults.add(v8);
			Thread.sleep(SLEEP_TIME);

		} catch (com.sun.xml.ws.client.ClientTransportException | ElectionBoardServiceFault ex) {
			msgr.sendElectionSpecError(ex);
		}

		return Collections.unmodifiableList(partialResults);
	}
}
