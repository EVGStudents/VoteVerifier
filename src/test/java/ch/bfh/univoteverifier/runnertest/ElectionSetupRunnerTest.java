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
import ch.bfh.univoteverifier.runner.ElectionSetupRunner;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InvalidNameException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the runner of the election setup.
 *
 * @author Scalzi Giuseppe
 */
public class ElectionSetupRunnerTest {

	private final ElectionSetupRunner esr;
	private final List<VerificationResult> mockList;
	private final List<VerificationResult> realList;
	private final ElectionBoardProxy ebp;
	private final String eID;
	private final RunnerName rn;

	public ElectionSetupRunnerTest() throws FileNotFoundException, CertificateException, ElectionBoardServiceFault, InvalidNameException, InterruptedException {
		eID = "vsbfh-2013";
		ebp = new ElectionBoardProxy();
		esr = new ElectionSetupRunner(ebp, new Messenger());
		realList = esr.run();
		mockList = new ArrayList<>();
		rn = RunnerName.ELECTION_SETUP;

		buildMockList();
	}

	/**
	 * Build the mock list.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	private void buildMockList() throws ElectionBoardServiceFault {


		mockList.add(new VerificationResult(VerificationType.EL_SETUP_EA_CERT, true, eID, rn, ImplementerType.CERTIFICATE, EntityType.CA));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_EA_CERT_ID_SIGN, true, eID, rn, ImplementerType.RSA, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_BASICS_PARAMS_SIGN, true, eID, rn, ImplementerType.RSA, EntityType.EA));

		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_TALLIERS_CERT, true, eID, rn, ImplementerType.CERTIFICATE, EntityType.TALLIER);
			vr.setEntityName(tName);
			mockList.add(vr);
		}

		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_MIXERS_CERT, true, eID, rn, ImplementerType.CERTIFICATE, EntityType.MIXER);
			vr.setEntityName(mName);
			mockList.add(vr);
		}
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_T_CERT_M_CERT_ID_SIGN, true, eID, rn, ImplementerType.RSA, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_P, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_Q, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_G, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_SAFE_PRIME, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAM_LEN, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAMS_SIGN, true, eID, rn, ImplementerType.RSA, EntityType.EM));

		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_T_NIZKP_OF_X, true, eID, rn, ImplementerType.NIZKP, EntityType.PARAMETER);
			vr.setEntityName(tName);
			mockList.add(vr);

			VerificationResult vrSign = new VerificationResult(VerificationType.EL_SETUP_T_NIZKP_OF_X_SIGN, true, eID, rn, ImplementerType.RSA, EntityType.TALLIER);
			vrSign.setEntityName(tName);
			mockList.add(vrSign);
		}

		mockList.add(new VerificationResult(VerificationType.EL_SETUP_T_PUBLIC_KEY, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_T_PUBLIC_KEY_SIGN, true, eID, rn, ImplementerType.RSA, EntityType.EM));

		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_M_NIZKP_OF_ALPHA, true, eID, rn, ImplementerType.NIZKP, EntityType.PARAMETER);
			vr.setEntityName(mName);
			mockList.add(vr);

			VerificationResult vrSign = new VerificationResult(VerificationType.EL_SETUP_M_NIZKP_OF_ALPHA_SIGN, true, eID, rn, ImplementerType.RSA, EntityType.MIXER);
			vrSign.setEntityName(mName);
			mockList.add(vrSign);
		}

		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ANON_GEN, true, eID, rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ANON_GEN_SIGN, true, eID, rn, ImplementerType.RSA, EntityType.EM));
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
		assertEquals(esr.getRunnerName(), rn);
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
