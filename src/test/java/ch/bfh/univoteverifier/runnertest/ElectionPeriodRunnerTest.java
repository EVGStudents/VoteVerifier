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

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.runner.ElectionPeriodRunner;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InvalidNameException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test the runner of the election period.
 *
 * @author snake
 */
public class ElectionPeriodRunnerTest {

	private final ElectionPeriodRunner epr;
	private final List<VerificationResult> mockList;
	private final List<VerificationResult> realList;
	private final ElectionBoardProxy ebp;
	private final String eID;
	private final RunnerName rn;

	public ElectionPeriodRunnerTest() throws FileNotFoundException, CertificateException, ElectionBoardServiceFault, InvalidNameException {
		eID = "vsbfh-2013";
		ebp = new ElectionBoardProxy();
		epr = new ElectionPeriodRunner(ebp, new Messenger());
		realList = epr.run();
		mockList = new ArrayList<>();
		rn = RunnerName.ELECTION_PERIOD;

		buildMockList();
	}

	/**
	 * Build the mock list.
	 *
	 * @throws ElectionBoardServiceFault
	 */
	private void buildMockList() throws ElectionBoardServiceFault {
		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_LATE_NEW_VOTER_CERT, true, eID, rn, ImplementerType.CERTIFICATE, EntityType.CA));
		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_LATE_NEW_VOTER_CERT_SIGN, true, eID, rn, ImplementerType.RSA, EntityType.EM));

		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult v = new VerificationResult(VerificationType.EL_PERIOD_M_NIZKP_EQUALITY_NEW_VRF, false, ebp.getElectionID(), rn, ImplementerType.NIZKP, EntityType.PARAMETER);
			v.setEntityName(mName);
			v.setImplemented(false);

			mockList.add(v);

			VerificationResult vSign = new VerificationResult(VerificationType.EL_PERIOD_M_NIZKP_EQUALITY_NEW_VRF_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.MIXER);
			vSign.setEntityName(mName);
			mockList.add(vSign);
		}

		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_NEW_VER_KEY, true, ebp.getElectionID(), rn, ImplementerType.PARAMETER, EntityType.EM));

		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_NEW_VER_KEY_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM));

		//ToDo - Check M7,M8, EM16, EM17

		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_BALLOT, true, ebp.getElectionID(), rn, ImplementerType.PARAMETER, EntityType.VOTERS));

		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_BALLOT_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM));
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
		assertEquals(epr.getRunnerName(), rn);
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
			assertNull(realList.get(i).getFailureCode());
		}
	}
}
