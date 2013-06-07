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
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.runner.IndividualRunner;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InvalidNameException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the runner for the individual verification.
 *
 * @author Scalzi Giuseppe
 */
public class IndividualRunnerTest {

	private final IndividualRunner ir;
	private final List<VerificationResult> mockList;
	private final List<VerificationResult> realList;
	private final ElectionBoardProxy ebp;
	private final RunnerName rn;

	public IndividualRunnerTest() throws FileNotFoundException, CertificateException, ElectionBoardServiceFault, InvalidNameException, InterruptedException {
		File qrCodeFile = new File(this.getClass().getResource("/qrcodeGiu").getPath());
		QRCode qrCode = new QRCode(new Messenger());
		ElectionReceipt er = qrCode.decodeReceipt(qrCodeFile);

		ebp = new ElectionBoardProxy("risis-2013-1", true);
		ir = new IndividualRunner(ebp, new Messenger(), er);
		realList = ir.run();
		mockList = new ArrayList<>();
		rn = RunnerName.INDIVIDUAL;

		buildMockList();
	}

	/**
	 * Build the mock list.
	 *
	 */
	private void buildMockList() {
		mockList.add(new VerificationResult(VerificationType.SETUP_EM_CERT, true, ebp.getElectionID(), rn, ImplementerType.CERTIFICATE, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.SINGLE_BALLOT_RSA_SIGN, true, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.SINGLE_BALLOT_IN_BALLOTS, true, ebp.getElectionID(), rn, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SINGLE_BALLOT_SCHNORR_SIGN, true, ebp.getElectionID(), rn, ImplementerType.SCHNORR, EntityType.VOTERS));
		mockList.add(new VerificationResult(VerificationType.SINGLE_BALLOT_PROOF, true, ebp.getElectionID(), rn, ImplementerType.NIZKP, EntityType.VOTERS));
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
		assertEquals(ir.getRunnerName(), rn);
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
