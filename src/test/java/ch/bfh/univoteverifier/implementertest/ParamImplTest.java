/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author snake
 */
public class ParamImplTest {

	ParametersImplementer pi;

	public ParamImplTest() {
		pi = new ParametersImplementer();

		//change the value of p - all the test must fail
		pi.setP(Config.p.subtract(BigInteger.ONE));
		pi.setQ(Config.q.subtract(BigInteger.ONE));
		pi.setG(new BigInteger("65537"));
	}

	@Test
	public void testParamNotLen() {
		VerificationEvent v = pi.vrfParamLen();
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.FALSE_PARAMETERS_LENGTH);
	}

	@Test
	public void testPisNotSafePrime() {
		VerificationEvent v = pi.vrfSafePrime();
		assertFalse(pi.vrfSafePrime().getResult());
		assertEquals(v.getFailureCode(), FailureCode.NOT_SAFE_PRIME);

	}

	@Test
	public void testPisNotPrime() {
		VerificationEvent v = pi.vrfPrimeP();
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.COMPOSITE_PRIME_NUMBER);
	}

	@Test
	public void testQisNotPrime() {
		VerificationEvent v = pi.vrfPrimeQ();
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.COMPOSITE_PRIME_NUMBER);
	}

	@Test
	public void testGisNotGenerator() {
		VerificationEvent v = pi.vrfGenerator();
		assertFalse(v.getResult());
		assertEquals(v.getFailureCode(), FailureCode.NOT_A_GENERATOR);
	}
}
