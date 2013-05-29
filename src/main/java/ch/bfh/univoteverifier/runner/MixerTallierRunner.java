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
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.implementer.ProofImplementer;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;
import javax.naming.InvalidNameException;

/**
 * This class represent a MixerTallierRunner.
 *
 * @author Scalzi Giuseppe
 */
public class MixerTallierRunner extends Runner {

	private final RSAImplementer rsaImpl;
	private final ProofImplementer proofImpl;
	private final ParametersImplementer prmImpl;
	private final ElectionBoardProxy ebp;

	/**
	 * Construct an MixerTallierRunner with a given ElectionBoardProxy.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param msgr the Messenger used to send the results.
	 */
	public MixerTallierRunner(ElectionBoardProxy ebp, Messenger msgr) throws CertificateException, ElectionBoardServiceFault, InvalidNameException {
		super(RunnerName.MIXING_TALLING, msgr);
		this.ebp = ebp;
		rsaImpl = new RSAImplementer(ebp, runnerName);
		proofImpl = new ProofImplementer(ebp, runnerName);
		prmImpl = new ParametersImplementer(ebp, runnerName);
	}

	@Override
	public List<VerificationResult> run() throws InterruptedException {
		try {
			//shuffled encrypted votes by mixer and signature
			for (String mName : ebp.getElectionDefinition().getMixerId()) {
				VerificationResult v1 = proofImpl.vrfEncryptedVotesByProof(mName);
				msgr.sendVrfMsg(v1);
				partialResults.add(v1);
				Thread.sleep(SLEEP_TIME);

				VerificationResult v2 = rsaImpl.vrfMixedEncryptedVotesBySign(mName);
				msgr.sendVrfMsg(v2);
				partialResults.add(v2);
				Thread.sleep(SLEEP_TIME);
			}

			//shuffled mixed encrypted votes set
			VerificationResult v3 = prmImpl.vrfMixedEncryptedVotes();
			msgr.sendVrfMsg(v3);
			partialResults.add(v3);
			Thread.sleep(SLEEP_TIME);

			//signature of shuffled mixed encrypted votes set
			VerificationResult v4 = rsaImpl.vrfMixedEncryptedVotesSign();
			msgr.sendVrfMsg(v4);
			partialResults.add(v4);
			Thread.sleep(SLEEP_TIME);

			//NIZKP of decrypted votes and signature
			for (String tName : ebp.getElectionDefinition().getTallierId()) {
				VerificationResult v5 = proofImpl.vrfDecryptedVotesByProof(tName);
				msgr.sendVrfMsg(v5);
				partialResults.add(v5);
				Thread.sleep(SLEEP_TIME);

				VerificationResult v6 = rsaImpl.vrfDecryptedVotesBySign(tName);
				msgr.sendVrfMsg(v6);
				partialResults.add(v6);
				Thread.sleep(SLEEP_TIME);
			}

			//plaintext votes set
			VerificationResult v7 = prmImpl.vrfVotes();
			msgr.sendVrfMsg(v7);
			partialResults.add(v7);
			Thread.sleep(SLEEP_TIME);

			//plaintext votes set signature
			VerificationResult v8 = rsaImpl.vrfPlaintextVotesSign();
			msgr.sendVrfMsg(v8);
			partialResults.add(v8);
			Thread.sleep(SLEEP_TIME);

		} catch (InterruptedException | ElectionBoardServiceFault ex) {
			msgr.sendElectionSpecError(ebp.getElectionID(), ex);
		}

		return Collections.unmodifiableList(partialResults);
	}
}
