/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.runnertest;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test the runner of the system setup.
 *
 * @author Scalzi Giuseppe
 */
public class SystemSetupRunnerTest {

	private final SystemSetupRunner ssr;
	private final List<VerificationResult> mockList;
	private final List<VerificationResult> realList;
	private final ElectionBoardProxy ebp;
	private final String eID;
	private final RunnerName rn;

	public SystemSetupRunnerTest() throws FileNotFoundException, InterruptedException {
		eID = "risis-2013-1";
		ebp = new ElectionBoardProxy(eID, true);
		ssr = new SystemSetupRunner(ebp, new Messenger());
		realList = ssr.run();
		mockList = new ArrayList<VerificationResult>();
		rn = RunnerName.SYSTEM_SETUP;

		buildMockList();
	}

	/**
	 * Build the mock list.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	private void buildMockList() {
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_P, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_Q, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_G, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_P_SAFE_PRIME, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_PARAM_LEN, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SETUP_CA_CERT, true, eID, rn, ImplementerType.CERTIFICATE, EntityType.CA));
		mockList.add(new VerificationResult(VerificationType.SETUP_EM_CERT, true, eID, rn, ImplementerType.CERTIFICATE, EntityType.EM));
	}

	/**
	 * Test if the size of the result list correspond.
	 */
	@Test
	public void testSizeOfResults() {
		assertEquals(mockList.size(), realList.size());
	}

	/**
	 * Test if the runner name correspond.
	 */
	@Test
	public void testRunnerType() {
		assertEquals(ssr.getRunnerName(), rn);
	}

	/**
	 * Test if the result list correspond with the one we have built.
	 */
	@Test
	public void testResultList() {
		int i;

		for (i = 0; i < mockList.size(); i++) {
			assertEquals(realList.get(i).getVerificationType(), mockList.get(i).getVerificationType());
			assertEquals(realList.get(i).getResult(), mockList.get(i).getResult());
			assertTrue(realList.get(i).isImplemented());
			assertNull(realList.get(i).getReport());
		}
	}
}
