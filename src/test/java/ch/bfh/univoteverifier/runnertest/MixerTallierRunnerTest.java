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
import ch.bfh.univoteverifier.runner.MixerTallierRunner;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InvalidNameException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the runner of mixer and tallier.
 *
 * @author Scalzi Giuseppe
 */
public class MixerTallierRunnerTest {

	private final MixerTallierRunner epr;
	private final List<VerificationResult> mockList;
	private final List<VerificationResult> realList;
	private final ElectionBoardProxy ebp;
	private final String eID;
	private final RunnerName rn;

	public MixerTallierRunnerTest() throws FileNotFoundException, CertificateException, ElectionBoardServiceFault, InvalidNameException, InterruptedException {
		eID = "vsbfh-2013";
		ebp = new ElectionBoardProxy();
		epr = new MixerTallierRunner(ebp, new Messenger());
		realList = epr.run();
		mockList = new ArrayList<>();
		rn = RunnerName.MIXING_TALLING;
		buildMockList();
	}

	/**
	 * Build the mock list.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	private void buildMockList() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult v1 = new VerificationResult(VerificationType.MT_M_ENC_VOTES_SET, true, ebp.getElectionID(), rn, ImplementerType.NIZKP, EntityType.PARAMETER);
			v1.setEntityName(mName);
			mockList.add(v1);

			VerificationResult v2 = new VerificationResult(VerificationType.MT_M_ENC_VOTES_SET_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.MIXER);
			v2.setEntityName(mName);
			mockList.add(v2);
		}

		mockList.add(new VerificationResult(VerificationType.MT_ENC_VOTES_SET, true, ebp.getElectionID(), rn, ImplementerType.PARAMETER, EntityType.EA));

		mockList.add(new VerificationResult(VerificationType.MT_ENC_VOTES_ID_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EA));

		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = new VerificationResult(VerificationType.MT_T_NIZKP_OF_X, true, ebp.getElectionID(), rn, ImplementerType.NIZKP, EntityType.TALLIER);
			vr.setEntityName(tName);
			mockList.add(vr);

			VerificationResult vr2 = new VerificationResult(VerificationType.MT_T_NIZKP_OF_X_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.TALLIER);
			vr2.setEntityName(tName);
			mockList.add(vr2);
		}

		mockList.add(new VerificationResult(VerificationType.MT_VALID_PLAINTEXT_VOTES, true, ebp.getElectionID(), rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.MT_VALID_PLAINTEXT_VOTES_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM));
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
