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

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.implementer.ProofImplementer;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.Collections;
import java.util.List;

/**
 * This class represent an ElectionPreparationRunner.
 *
 * @author Scalzi Giuseppe
 */
public class ElectionPreparationRunner extends Runner {

	private final RSAImplementer rsaImpl;
	private final CertificatesImplementer certImpl;
	private final ParametersImplementer prmImpl;
	private final ProofImplementer proofImpl;

	/**
	 * Construct an ElectionPreparationRunner with a given
	 * ElectionBoardProxy and Messenger.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param msgr the Messenger used to send the results.
	 */
	public ElectionPreparationRunner(ElectionBoardProxy ebp, Messenger gm) {
		super(ebp, RunnerName.ELECTION_PREPARATION, gm);
		rsaImpl = new RSAImplementer(ebp, runnerName);
		certImpl = new CertificatesImplementer(ebp, runnerName);
		prmImpl = new ParametersImplementer(ebp, runnerName);
		proofImpl = new ProofImplementer(ebp, runnerName);
	}

	@Override
	public List<VerificationResult> run() throws InterruptedException {
		try {
			//RSA signature of election options
			VerificationResult v1 = rsaImpl.vrfElectionOptionsSign();
			msgr.sendVrfMsg(v1);
			partialResults.add(v1);
			Thread.sleep(SLEEP_TIME);

			//RSA signature of election data
			VerificationResult v2 = rsaImpl.vrfElectionDataSign();
			msgr.sendVrfMsg(v2);
			partialResults.add(v2);
			Thread.sleep(SLEEP_TIME);

			//RSA signature of electoral roll
			VerificationResult v3 = rsaImpl.vrfElectoralRollSign();
			msgr.sendVrfMsg(v3);
			partialResults.add(v3);
			Thread.sleep(SLEEP_TIME);

			//Voters certificate
			VerificationResult v4 = certImpl.vrfVotersCertificate();
			msgr.sendVrfMsg(v4);
			partialResults.add(v4);
			Thread.sleep(SLEEP_TIME);

			//RSA signature of election ID and voters certificate
			VerificationResult v5 = rsaImpl.vrfVotersCertIDSign();
			msgr.sendVrfMsg(v5);
			partialResults.add(v5);
			Thread.sleep(SLEEP_TIME);

			//Plausibility check and signature of mixed verifiction keys by
			try {
				for (String mName : ebp.getElectionDefinition().getMixerId()) {
					VerificationResult v6 = proofImpl.vrfVerificationKeysMixedByProof(mName);
					msgr.sendVrfMsg(v6);
					partialResults.add(v6);
					Thread.sleep(SLEEP_TIME);

					VerificationResult v7 = rsaImpl.vrfMixedVerificationKeysBySign(mName);
					msgr.sendVrfMsg(v7);
					partialResults.add(v7);
					Thread.sleep(SLEEP_TIME);
				}
			} catch (ElectionBoardServiceFault ex) {
				msgr.sendElectionSpecError(ex);
			}

			//mixed verification keys
			VerificationResult v8 = prmImpl.vrfVerificationKeysMixed();
			msgr.sendVrfMsg(v8);
			partialResults.add(v8);
			Thread.sleep(SLEEP_TIME);

			//mixed verification keys signature
			VerificationResult v9 = rsaImpl.vrfMixedVerificationKeysSign();
			msgr.sendVrfMsg(v9);
			partialResults.add(v9);
			Thread.sleep(SLEEP_TIME);
		} catch (com.sun.xml.ws.client.ClientTransportException ex) {
			msgr.sendElectionSpecError(ex);
		}
		return Collections.unmodifiableList(partialResults);
	}
}
