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

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.SchnorrImplementer;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author snake
 */
public class SchnorrImplTest {

	ElectionBoardProxy ebp;
	SchnorrImplementer si;

	public SchnorrImplTest() {
		si = new SchnorrImplementer(ebp, RunnerName.UNSET);
	}

	/**
	 * Test the result of vrfBallotSignature().
	 *
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testSignatureVerification() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = si.vrfBallotSignature(null);
		assertTrue(vr.getResult());
	}
}
