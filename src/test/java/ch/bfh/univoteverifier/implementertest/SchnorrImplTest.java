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
package ch.bfh.univoteverifier.implementertest;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.implementer.SchnorrImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of the SchnorrImplementer.
 *
 * @author Scalzi Giuseppe
 */
public class SchnorrImplTest {

	private final ElectionBoardProxy ebp;
	private final SchnorrImplementer si;
	private final ElectionReceipt er;

	public SchnorrImplTest() throws FileNotFoundException {
		ebp = new ElectionBoardProxy();
		si = new SchnorrImplementer(ebp, RunnerName.UNSET);

		File qrCodeFile = new File(this.getClass().getResource("/qrcodeGiu").getPath());
		QRCode qrCode = new QRCode(new Messenger());
		er = qrCode.decodeReceipt(qrCodeFile);
	}

	/**
	 * Test the result of vrfBallotSignature().
	 *
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testSignatureVerification() throws NoSuchAlgorithmException, UnsupportedEncodingException, ElectionBoardServiceFault {
		Ballot b = ebp.getBallots().getBallot().get(0);
		VerificationResult vr = si.vrfBallotSignature(b, null);
		assertTrue(vr.getResult());
	}
}
