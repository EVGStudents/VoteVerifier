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
import ch.bfh.univoteverifier.runner.ElectionPreparationRunner;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InvalidNameException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  * Test the runner of the election preparation.
 *
 * @author Scalzi Giuseppe
 */
public class ElectionPreparationRunnerTest {

	private final ElectionPreparationRunner epr;
	private final List<VerificationResult> mockList;
	private final List<VerificationResult> realList;
	private final ElectionBoardProxy ebp;
	private final String eID;
	private final RunnerName rn;

	public ElectionPreparationRunnerTest() throws FileNotFoundException, CertificateException, ElectionBoardServiceFault, InvalidNameException, InterruptedException {
		eID = "risis-2013-1";
		ebp = new ElectionBoardProxy(eID, true);
		epr = new ElectionPreparationRunner(ebp, new Messenger());
		realList = epr.run();
		mockList = new ArrayList<>();
		rn = RunnerName.ELECTION_PREPARATION;

		buildMockList();
	}

	/**
	 * Build the mock list.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	private void buildMockList() throws ElectionBoardServiceFault {
		mockList.add(new VerificationResult(VerificationType.EL_PREP_C_AND_R_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EA));
		mockList.add(new VerificationResult(VerificationType.EL_PREP_EDATA_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.EL_PREP_ELECTORAL_ROLL_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EA));
		mockList.add(new VerificationResult(VerificationType.EL_PREP_VOTERS_CERT, true, ebp.getElectionID(), rn, ImplementerType.CERTIFICATE, EntityType.CA));
		mockList.add(new VerificationResult(VerificationType.EL_PREP_VOTERS_CERT_SIGN, false, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.CA));

		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult v = new VerificationResult(VerificationType.EL_PREP_M_PUB_VER_KEYS, true, ebp.getElectionID(), rn, ImplementerType.PARAMETER, EntityType.PARAMETER);
			v.setEntityName(mName);
			mockList.add(v);

			VerificationResult vSign = new VerificationResult(VerificationType.EL_PREP_M_PUB_VER_KEYS_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.MIXER);
			vSign.setEntityName(mName);
			mockList.add(vSign);
		}

		mockList.add(new VerificationResult(VerificationType.EL_PREP_PUB_VER_KEYS, true, ebp.getElectionID(), rn, ImplementerType.PARAMETER, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.EL_PREP_PUB_VER_KEYS_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM));
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
			assertNull(realList.get(i).getReport());
		}
	}
}
