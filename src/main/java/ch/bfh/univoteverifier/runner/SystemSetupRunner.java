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

import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.Messenger;
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
	private final Messenger gm;

	/**
	 * Construct an SystemSetupRunner with a given Messenger.
	 *
	 */
	public SystemSetupRunner(Messenger gm) {
		super(null, RunnerName.SYSTEM_SETUP);

		//create the implementer we want
		prmVrf = new ParametersImplementer();
		this.gm = gm;
	}

	@Override
	public List<VerificationEvent> run() {

		try {
			//perform the checks we want - pay attention to exceptions!
			VerificationEvent v1 = prmVrf.vrfPrimeP();
			gm.sendVrfMsg(v1);
			Thread.sleep(1000);

			VerificationEvent v2 = prmVrf.vrfPrimeQ();
			gm.sendVrfMsg(v2);
			Thread.sleep(1000);

			VerificationEvent v3 = prmVrf.vrfGenerator();
			gm.sendVrfMsg(v3);
			Thread.sleep(1000);

			VerificationEvent v4 = prmVrf.vrfSafePrime();
			gm.sendVrfMsg(v4);
			Thread.sleep(1000);

			VerificationEvent v5 = prmVrf.vrfParamLen();
			gm.sendVrfMsg(v5);

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
