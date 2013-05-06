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

import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represent a MixerTallierRunner.
 *
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner {

	private final ParametersImplementer paramImpl;
	private final CertificatesImplementer certImpl;

	/**
	 * Construct an SystemSetupRunner with a given Messenger.
	 *
	 */
	public SystemSetupRunner(ElectionBoardProxy ebp, Messenger msgr) {
		super(RunnerName.SYSTEM_SETUP, msgr);

		//create the implementer we want
		paramImpl = new ParametersImplementer(ebp);
		certImpl = new CertificatesImplementer(ebp);
	}

	@Override
	public List<VerificationResult> run() {

		try {
			//perform the checks we want - pay attention to exceptions!
			VerificationResult v1 = paramImpl.vrfPrime(Config.p, VerificationType.SETUP_SCHNORR_P);
			v1.setRunnerName(RunnerName.SYSTEM_SETUP);

			msgr.sendVrfMsg(v1);
			Thread.sleep(1000);

			VerificationResult v2 = paramImpl.vrfPrime(Config.q, VerificationType.SETUP_SCHNORR_Q);
			v2.setRunnerName(RunnerName.SYSTEM_SETUP);
			msgr.sendVrfMsg(v2);
			Thread.sleep(1000);

			VerificationResult v3 = paramImpl.vrfGenerator(Config.p, Config.q, Config.g, VerificationType.SETUP_SCHNORR_G);
			v3.setRunnerName(RunnerName.SYSTEM_SETUP);
			msgr.sendVrfMsg(v3);
			Thread.sleep(1000);

			VerificationResult v4 = paramImpl.vrfSafePrime(Config.p, Config.q, VerificationType.SETUP_SCHNORR_P_SAFE_PRIME);
			v4.setRunnerName(RunnerName.SYSTEM_SETUP);
			msgr.sendVrfMsg(v4);
			Thread.sleep(1000);

			VerificationResult v5 = paramImpl.vrfSchnorrParamLen(Config.p, Config.q, Config.g);
			v5.setRunnerName(RunnerName.SYSTEM_SETUP);
			msgr.sendVrfMsg(v5);

			//cache the results
			partialResults.add(v1);
			partialResults.add(v2);
			partialResults.add(v3);
			partialResults.add(v4);
			partialResults.add(v5);
		} catch (InterruptedException | ElectionBoardServiceFault ex) {
			Logger.getLogger(SystemSetupRunner.class.getName()).log(Level.SEVERE, null, ex);
		}

		return Collections.unmodifiableList(partialResults);
	}
}
