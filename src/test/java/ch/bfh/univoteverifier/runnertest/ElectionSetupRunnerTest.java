/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.runnertest;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.runner.ElectionSetupRunner;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author snake
 */
public class ElectionSetupRunnerTest {

	ElectionSetupRunner esr;
	List<VerificationResult> mockList;
	List<VerificationResult> realList;
	Messenger gm;
	ElectionBoardProxy ebp;
	String eID;

	public ElectionSetupRunnerTest() throws FileNotFoundException, CertificateException, ElectionBoardServiceFault {
		gm = new Messenger();
		eID = "vsbfh-2013";
		ebp = new ElectionBoardProxy();
		esr = new ElectionSetupRunner(ebp, gm);
		realList = esr.run();
		mockList = new ArrayList<>();

		mockList.add(new VerificationResult(VerificationType.EL_SETUP_EA_CERT, true, eID, RunnerName.ELECTION_SETUP));
//		mockList.add(new VerificationResult(VerificationType.EL_SETUP_EA_CERT_ID_SIGN, true, eID, RunnerName.ELECTION_SETUP));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_BASICS_PARAMS_SIGN, true, eID, RunnerName.ELECTION_SETUP));

		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_TALLIERS_CERT, true, eID, RunnerName.ELECTION_SETUP);
			vr.setEntityName(tName);
			mockList.add(vr);
		}

		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_MIXERS_CERT, true, eID, RunnerName.ELECTION_SETUP);
			mockList.add(vr);
		}
//		mockList.add(new VerificationResult(VerificationType.EL_SETUP_T_CERT_M_CERT_ID_SIGN, true, eID, RunnerName.ELECTION_SETUP));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_P, true, eID, RunnerName.ELECTION_SETUP));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_Q, true, eID, RunnerName.ELECTION_SETUP));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_G, true, eID, RunnerName.ELECTION_SETUP));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_SAFE_PRIME, true, eID, RunnerName.ELECTION_SETUP));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAM_LEN, true, eID, RunnerName.ELECTION_SETUP));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAMS_SIGN, true, eID, RunnerName.ELECTION_SETUP));
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
		assertEquals(esr.getRunnerName(), RunnerName.ELECTION_SETUP);
	}

	/**
	 * Test if the result list correspond with the one we have built
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
