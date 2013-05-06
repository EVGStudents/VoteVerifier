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

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.math.BigInteger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of the ParametersImplementer.
 *
 * @author snake
 */
public class ParamImplTest {

	ParametersImplementer pi;
	BigInteger p, q, g;

	public ParamImplTest() {
		pi = new ParametersImplementer();

		//change the value of p,q and g - all the test must fail
		p = Config.p.multiply(new BigInteger("2"));
		q = Config.q.multiply(new BigInteger("2"));
		g = Config.g.multiply(new BigInteger("2"));
	}

	/**
	 * Test that the parameters are not long as expected.
	 */
	@Test
	public void testParamNotLen() {
		VerificationResult v = pi.vrfSchnorrParamLen(p, q, g);
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.FALSE_PARAMETERS_LENGTH);
	}

	/**
	 * Test that P is not a safe prime.
	 */
	@Test
	public void testPisNotSafePrime() {
		VerificationResult v = pi.vrfSafePrime(p, q, VerificationType.SETUP_SCHNORR_P_SAFE_PRIME);
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.NOT_SAFE_PRIME);

	}

	/**
	 * Test that P is not prime.
	 */
	@Test
	public void testPisNotPrime() {
		VerificationResult v = pi.vrfPrime(p, VerificationType.SETUP_SCHNORR_P);
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.COMPOSITE_PRIME_NUMBER);
	}

	/**
	 * Test that Q is not prime.
	 */
	@Test
	public void testQisNotPrime() {
		VerificationResult v = pi.vrfPrime(q, VerificationType.SETUP_SCHNORR_Q);
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.COMPOSITE_PRIME_NUMBER);
	}

	/**
	 * Test that G is not a generator.
	 */
	@Test
	public void testGisNotGenerator() {
		VerificationResult v = pi.vrfGenerator(p, q, g, VerificationType.SETUP_SCHNORR_G);
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.NOT_A_GENERATOR);
	}
}
