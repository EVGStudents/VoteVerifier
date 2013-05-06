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

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the runner of the system setup
 *
 * @author snake
 */
public class SystemSetupRunnerTest {

	SystemSetupRunner ssr;
	List<VerificationResult> mockList;
	List<VerificationResult> realList;
	Messenger gm;
	ElectionBoardProxy ebp;
	String eID;

	public SystemSetupRunnerTest() {
		gm = new Messenger();
		eID = "vsbfh-2013";
		ebp = new ElectionBoardProxy(eID);
		ssr = new SystemSetupRunner(ebp, gm);
		realList = ssr.run();
		mockList = new ArrayList<>();

		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_P, true, eID, RunnerName.SYSTEM_SETUP));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_Q, true, eID, RunnerName.SYSTEM_SETUP));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_G, true, eID, RunnerName.SYSTEM_SETUP));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_P_SAFE_PRIME, true, eID, RunnerName.SYSTEM_SETUP));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_PARAM_LEN, true, eID, RunnerName.SYSTEM_SETUP));

	}

	/**
	 * Test if the size of the result list correspond
	 */
	@Test
	public void testSizeOfResults() {
		assertEquals(mockList.size(), realList.size());
	}

	/**
	 * Test if the runner name correspond
	 */
	@Test
	public void testRunnerType() {
		assertEquals(ssr.getRunnerName(), RunnerName.SYSTEM_SETUP);
	}

	/**
	 * Test if the result list correspond with the one we have built
	 */
	@Test
	public void testResultList() {
		int i;

		for (i = 0; i < mockList.size(); i++) {
			assertEquals(realList.get(i).getVerificationEnum(), mockList.get(i).getVerificationEnum());
			assertEquals(realList.get(i).getResult(), mockList.get(i).getResult());
			assertTrue(realList.get(i).isImplemented());
			assertNull(realList.get(i).getFailureCode());
		}
	}
}
