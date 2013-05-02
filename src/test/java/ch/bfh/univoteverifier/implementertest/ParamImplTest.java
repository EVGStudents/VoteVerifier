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
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.math.BigInteger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of the ParametersImplementer
 *
 * @author snake
 */
public class ParamImplTest {

	ParametersImplementer pi;

	public ParamImplTest() {
		pi = new ParametersImplementer();

		//change the value of p,q and g - all the test must fail
		pi.setP(Config.p.multiply(new BigInteger("2")));
		pi.setQ(Config.q.multiply(new BigInteger("2")));
		pi.setG(Config.g.multiply(new BigInteger("2")));
	}

	/**
	 * Test that the parameters are not long as expected
	 */
	@Test
	public void testParamNotLen() {
		VerificationEvent v = pi.vrfParamLen();
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.FALSE_PARAMETERS_LENGTH);
	}

	/**
	 * Test that P is not a safe prime
	 */
	@Test
	public void testPisNotSafePrime() {
		VerificationEvent v = pi.vrfSafePrime();
		assertFalse(pi.vrfSafePrime().getResult());
		assertEquals(v.getFailureCode(), FailureCode.NOT_SAFE_PRIME);

	}

	/**
	 * Test that P is not prime
	 */
	@Test
	public void testPisNotPrime() {
		VerificationEvent v = pi.vrfPrimeP();
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.COMPOSITE_PRIME_NUMBER);
	}

	/**
	 * Test that Q is not prime
	 */
	@Test
	public void testQisNotPrime() {
		VerificationEvent v = pi.vrfPrimeQ();
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.COMPOSITE_PRIME_NUMBER);
	}

	/**
	 * Test that G is not a generator
	 */
	@Test
	public void testGisNotGenerator() {
		VerificationEvent v = pi.vrfGenerator();
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.NOT_A_GENERATOR);
	}
}
