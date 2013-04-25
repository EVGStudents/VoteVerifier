/*
*
*  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
*   Bern University of Applied Sciences, Engineering and Information Technology,
*   Research Institute for Security in the Information Society, E-Voting Group,
*   Biel, Switzerland.
*
*   Project independent UniVoteVerifier.
*
*/
package ch.bfh.univoteverifier.runnertest;

import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.GUIMessenger;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.common.SectionNameEnum;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the runner of the system setup
 * @author snake
 */
public class SystemSetupRunnerTest {

	SystemSetupRunner ssr;
	List<VerificationEvent> mockList;
	List<VerificationEvent> realList;
	GUIMessenger gm ;
	
	public SystemSetupRunnerTest() {
		gm = new GUIMessenger();
		ssr = new SystemSetupRunner(gm);
		realList = ssr.run();
		mockList = new ArrayList<>();
		
		mockList.add(new VerificationEvent(VerificationType.SETUP_P_IS_PRIME, true));
		mockList.add(new VerificationEvent(VerificationType.SETUP_Q_IS_PRIME, true));
		mockList.add(new VerificationEvent(VerificationType.SETUP_G_IS_GENERATOR, true));
		mockList.add(new VerificationEvent(VerificationType.SETUP_P_IS_SAFE_PRIME, true));
		mockList.add(new VerificationEvent(VerificationType.SETUP_PARAM_LEN, true));

	}

	/**
	 * Test if the size of the result list correspond
	 */
	@Test
	public void testSizeOfResults(){
		assertEquals(mockList.size(), realList.size());
	}

	/**
	 * Test if the runner name correspond
	 */
	@Test
	public void testRunnerType(){
		assertEquals(ssr.getRunnerName(),SectionNameEnum.SYSTEM_SETUP);
	}

	/**
	 * Test if the result list correspond with the one we have built
	 */
	@Test
	public void testResultList(){
		int i;

		for(i = 0 ; i < mockList.size() ; i++){
			assertEquals(realList.get(i).getVerificationEnum(),mockList.get(i).getVerificationEnum());
			assertEquals(realList.get(i).getResult(), mockList.get(i).getResult());
			assertTrue(realList.get(i).isImplemented());
			assertEquals(realList.get(i).getFailureCode(), FailureCode.CLEAN);
		}
	}

}