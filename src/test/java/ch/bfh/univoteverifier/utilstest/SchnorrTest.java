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
package ch.bfh.univoteverifier.utilstest;

import ch.bfh.univoteverifier.utils.OldPrimitives;
import ch.bfh.univoteverifier.utils.SchnorrGenerator;
import ch.bfh.univoteverifier.utils.SchnorrSignature;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;

/**
 * Test the schnorr signature verifier
 *
 * @author snake
 */
public class SchnorrTest {

	SchnorrGenerator sg;
	OldPrimitives op;

	public SchnorrTest() {
		this.sg = new SchnorrGenerator();
		this.op = new OldPrimitives();
	}

	/**
	 * Test a single schnorr's signature
	 *
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void singleSignatureVerification() throws NoSuchAlgorithmException {
		//BFH encoded as 66 | 70 | 72
		String bfh = "667072";
		BigInteger message = new BigInteger(bfh);

		//generate the signature
		SchnorrSignature ss = sg.signatureGeneration(message);

		//Verify signature
		assertTrue(op.vrfSchnorrSign(ss, message, SchnorrGenerator.publicKey));
	}

	/**
	 * Test multiples signatures, so we are sure that all should work fine
	 *
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void multipleSignatureVerification() throws NoSuchAlgorithmException {
		int numberOfVerifications = 100;

		for (int i = 0; i < numberOfVerifications; i++) {
			String bfh = "667072";
			BigInteger message = new BigInteger(bfh);

			//generate the signature
			SchnorrSignature ss = sg.signatureGeneration(message);

			assertTrue(op.vrfSchnorrSign(ss, message, SchnorrGenerator.publicKey));
		}

	}

	/**
	 * Test if an incorrect signature give false result
	 *
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void incorrectSignature() throws NoSuchAlgorithmException {
		//BFH encoded as 66 | 70 | 72
		String bfh = "667072";
		BigInteger message = new BigInteger(bfh);

		BigInteger falseMessage = new BigInteger("12345");

		//generate the signature
		SchnorrSignature ss = sg.signatureGeneration(message);

		//Verify signature
		assertFalse(op.vrfSchnorrSign(ss, falseMessage, SchnorrGenerator.publicKey));
	}
}
