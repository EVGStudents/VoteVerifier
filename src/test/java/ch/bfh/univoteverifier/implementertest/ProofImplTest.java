/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.implementertest;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.ProofImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of the ProofImplementer.
 *
 *
 * @author snake
 */
public class ProofImplTest {

	ProofImplementer pi;
	ElectionBoardProxy ebp;

	public ProofImplTest() throws FileNotFoundException {
		ebp = new ElectionBoardProxy();
		pi = new ProofImplementer(ebp, RunnerName.UNSET);
	}

	/**
	 * Test the result of vrfDistributedKeyByProog().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testDistributedKeyGenBy() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
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
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testBlindedGenerator() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
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
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testLatelyRegistrationKeys() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = pi.vrfLatelyVerificationKeysProof(mName);
			assertFalse(vr.getResult());
			assertFalse(vr.isImplemented());
		}
	}
}