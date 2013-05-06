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

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.VerificationType;
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

	private final ParametersImplementer prmVrf;

	/**
	 * Construct an SystemSetupRunner with a given Messenger.
	 *
	 */
	public SystemSetupRunner(Messenger msgr) {
		super(RunnerName.SYSTEM_SETUP, msgr);

		//create the implementer we want
		prmVrf = new ParametersImplementer();
	}

	@Override
	public List<VerificationResult> run() {

		try {
			//perform the checks we want - pay attention to exceptions!
			VerificationResult v1 = prmVrf.vrfPrime(Config.p, VerificationType.SETUP_SCHNORR_P);
			msgr.sendVrfMsg(v1);
			Thread.sleep(1000);

			VerificationResult v2 = prmVrf.vrfPrime(Config.q, VerificationType.SETUP_SCHNORR_Q);
			msgr.sendVrfMsg(v2);
			Thread.sleep(1000);

			VerificationResult v3 = prmVrf.vrfGenerator(Config.p, Config.q, Config.g, VerificationType.SETUP_SCHNORR_G);
			msgr.sendVrfMsg(v3);
			Thread.sleep(1000);

			VerificationResult v4 = prmVrf.vrfSafePrime(Config.p, Config.q, VerificationType.SETUP_SCHNORR_P_SAFE_PRIME);
			msgr.sendVrfMsg(v4);
			Thread.sleep(1000);

			VerificationResult v5 = prmVrf.vrfSchnorrParamLen(Config.p, Config.q, Config.g);
			msgr.sendVrfMsg(v5);

			//cache the results
			partialResults.add(v1);
			partialResults.add(v2);
			partialResults.add(v3);
			partialResults.add(v4);
			partialResults.add(v5);
		} catch (InterruptedException ex) {
			Logger.getLogger(SystemSetupRunner.class.getName()).log(Level.SEVERE, null, ex);
		}

		return Collections.unmodifiableList(partialResults);
	}
}
