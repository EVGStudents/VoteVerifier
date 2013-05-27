/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.verificationtest;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.verification.IndividualVerification;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of an IndividualVerification.
 *
 * @author Scalzi Giuseppe
 */
public class IndividualVerificationTester {

	private final Verification v;
	private final List<VerificationResult> mockList;
	private final List<VerificationResult> realList;
	private final String eID;

	public IndividualVerificationTester() {
		File qrCodeFile = new File(this.getClass().getResource("/qrcodeGiu").getPath());
		QRCode qrCode = new QRCode(new Messenger());
		ElectionReceipt er = qrCode.decodeReceipt(qrCodeFile);
		eID = "vsbfh-2013";
		v = new IndividualVerification(new Messenger(), "", er);
		realList = v.runVerification();
		mockList = new ArrayList<>();
		buildMockList();
	}

	/**
	 * Build the mock list.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	private void buildMockList() {
		mockList.add(new VerificationResult(VerificationType.SETUP_EM_CERT, true, eID, RunnerName.INDIVIDUAL, ImplementerType.CERTIFICATE, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.SINGLE_BALLOT_RSA_SIGN, true, eID, RunnerName.INDIVIDUAL, ImplementerType.RSA, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.SINGLE_BALLOT_IN_BALLOTS, true, eID, RunnerName.INDIVIDUAL, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SINGLE_BALLOT_SCHNORR_SIGN, true, eID, RunnerName.INDIVIDUAL, ImplementerType.SCHNORR, EntityType.VOTERS));
		mockList.add(new VerificationResult(VerificationType.SINGLE_BALLOT_PROOF, true, eID, RunnerName.INDIVIDUAL, ImplementerType.NIZKP, EntityType.VOTERS));
	}

	/**
	 * Test if the list contains all the results
	 */
	@Test
	public void testListSize() {
		assertEquals(mockList.size(), realList.size());
	}

	/**
	 * Test if the election contains the correct election ID
	 */
	@Test
	public void testgetelectionID() {
		assertEquals(eID, v.geteID());
	}

	/**
	 * Test if the final result list is correct
	 */
	@Test
	public void testFinalResultList() {
		int i;

		for (i = 0; i < mockList.size(); i++) {
			assertEquals(realList.get(i).getVerificationType(), mockList.get(i).getVerificationType());
			assertEquals(realList.get(i).getResult(), mockList.get(i).getResult());
		}
	}
}
