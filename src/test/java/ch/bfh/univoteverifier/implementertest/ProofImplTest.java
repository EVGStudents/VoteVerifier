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
package ch.bfh.univoteverifier.implementertest;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.implementer.ProofImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of the ProofImplementer.
 *
 *
 * @author Scalzi Giuseppe
 */
public class ProofImplTest {

	ProofImplementer pi;
	ElectionBoardProxy ebp;

	public ProofImplTest() throws FileNotFoundException {
		ebp = new ElectionBoardProxy("risis-2013-1", true);
		pi = new ProofImplementer(ebp, RunnerName.UNSET);
	}

	/**
	 * Test the result of vrfDistributedKeyByProog().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testDistributedKeyGenBy() throws ElectionBoardServiceFault {
		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = pi.vrfDistributedKeyByProof(tName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfElectionGeneratorByProof().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testBlindedGenerator() throws ElectionBoardServiceFault {
		String previousName = "schnorr_generator";

		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = pi.vrfElectionGeneratorByProof(mName, previousName);
			previousName = mName;
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result and the implementation state of
	 * vrfLatelyVerificationKeysProof().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testLatelyRegistrationKeys() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = pi.vrfLatelyVerificationKeysByProof(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfBallotProof() by using values from a ballot.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testBallotProofFromBallot() throws ElectionBoardServiceFault {
		Ballot b = ebp.getBallots().getBallot().get(0);
		VerificationResult v = pi.vrfBallotProof(b, null);

		assertTrue(v.getResult());
	}

	/**
	 * Test the result of vrfBallotProof() by using values from the QR-Code.
	 */
	@Test
	public void testBallotProofFromQRcode() {
		File qrCodeFile = new File(this.getClass().getResource("/qrcodeGiu").getPath());
		QRCode qrCode = new QRCode(new Messenger());
		ElectionReceipt er = qrCode.decodeReceipt(qrCodeFile);

		VerificationResult v = pi.vrfBallotProof(null, er);

		assertTrue(v.getResult());
	}

	/**
	 * Test the result of vrfDecryptedVotesByProof().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testDecryptedVotes() throws ElectionBoardServiceFault {
		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = pi.vrfDecryptedVotesByProof(tName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the mixed verification keys by a given mixer.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testMixedVerificationKeysBy() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult v = pi.vrfVerificationKeysMixedByProof(mName);
			assertTrue(v.getResult());
		}
	}

	/**
	 * Test the result of vrfEncryptedVotesByProof().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testEncryptedVotesBy() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult v = pi.vrfEncryptedVotesByProof(mName);
			assertTrue(v.getResult());
		}
	}

	/**
	 * Test the result of vrfLateRenewalOfRegistrationProofBy().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testLateRenewalOfRegistrationProofBy() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult v = pi.vrfLateRenewalOfRegistrationProofBy(mName);
			assertTrue(v.getResult());
		}
	}
}
