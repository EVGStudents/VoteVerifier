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
package ch.bfh.univoteverifier.utilstest;

import ch.bfh.univoteverifier.utils.OldPrimitives;
import ch.bfh.univoteverifier.utils.RSASignature;
import static org.junit.Assert.*;
import java.math.BigInteger;
import java.util.Random;
import org.junit.Test;

public class RSATest {

	OldPrimitives op;

	public RSATest() {
		op = new OldPrimitives();
	}

	/**
	 * Tests valid signature with large values and large random message
	 */
	@Test
	public void validSigLargValsRandomMsg() {
		RSASignature s = new RSASignature();
		BigInteger m = new BigInteger("123456789012345678901234567890123456789012345678901234567890");
		s.setVarsLarge(m);
		s.sign(m);
		assertTrue(op.vrfRSASign(s, m));
	}

	/**
	 * Tests valid signature with small values and static message
	 */
	@Test
	public void validSigSmallStaticVals() {
		RSASignature s = new RSASignature();
		BigInteger m = new BigInteger("4");
		s.setStaticVars();
		s.sign(m);
		assertTrue(op.vrfRSASign(s, m));
	}

	/**
	 * Tests valid signature with small values and random message and random
	 * key values
	 */
	@Test
	public void validSigSmallValues() {
		Random rand = new Random();
		int r = rand.nextInt(33);
		int p = 3;
		int q = 11;
		RSASignature s = new RSASignature();
		BigInteger m = BigInteger.valueOf(r);
		s.setVarsSmall(m, p, q);
		s.sign(m);
		assertTrue(op.vrfRSASign(s, m));
	}
}
