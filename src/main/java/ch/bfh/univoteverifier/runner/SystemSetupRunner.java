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
import ch.bfh.univoteverifier.common.GUIMessenger;
import java.util.Collections;
import java.util.List;

/**
 * This class represent a MixerTallierRunner
 *
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner {

	private final ParametersImplementer prmVrf;
	private final GUIMessenger gm;

	/**
	 * Construct an SystemSetupRunner with a given ElectionBoardProxy
	 *
	 * @param ebp the ElectionBoardProxy from where get the data
	 */
	public SystemSetupRunner(GUIMessenger gm) {
		super(null, RunnerName.SYSTEM_SETUP);

		//create the implementer we want
		prmVrf = new ParametersImplementer();
		this.gm = gm;
	}

	@Override
	public List<VerificationEvent> run() {
		//perform the checks we want - pay attention to exceptions!
		VerificationEvent v1 = prmVrf.vrfPrimeP();
		gm.sendVrfMsg(v1);

		VerificationEvent v2 = prmVrf.vrfPrimeQ();
		gm.sendVrfMsg(v2);

		VerificationEvent v3 = prmVrf.vrfGenerator();
		gm.sendVrfMsg(v3);

		VerificationEvent v4 = prmVrf.vrfSafePrime();
		gm.sendVrfMsg(v4);

		VerificationEvent v5 = prmVrf.vrfParamLen();
		gm.sendVrfMsg(v5);

		//cache the results
		partialResults.add(v1);
		partialResults.add(v2);
		partialResults.add(v3);
		partialResults.add(v4);
		partialResults.add(v5);

		return Collections.unmodifiableList(partialResults);
	}
}
