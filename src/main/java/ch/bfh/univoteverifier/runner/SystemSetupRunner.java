/**
*
*  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
*   Bern University of Applied Sciences, Engineering and Information Technology,
*   Research Institute for Security in the Information Society, E-Voting Group,
*   Biel, Switzerland.
*
*   Project independent UniVoteVerifier.
*
*/
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.*;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.GUIMessenger;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author snake
 */
public class SystemSetupRunner extends Runner{
	
	private static final Logger LOGGER = Logger.getLogger(SystemSetupRunner.class.getName());
	private final ParametersImplementer prmVrf;
	private final GUIMessenger gm;
	
	public SystemSetupRunner(ElectionBoardProxy ebp, GUIMessenger gm) {
		super(ebp, SectionNameEnum.SYSTEM_SETUP);
		
		//create the verification classes we want
		prmVrf = new ParametersImplementer(ebp);
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
