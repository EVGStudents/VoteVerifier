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
import ch.bfh.univoteverifier.utils.ProofDiscreteLog;
import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.*;
import org.junit.Test;

public class NIZKPTest {

	OldPrimitives op;

	public NIZKPTest() {
		op = new OldPrimitives();
	}

	/**
	 * Tests valid proof with small values
	 */
	@Test
	public void validProofSmallValues() throws NoSuchAlgorithmException {
		ProofDiscreteLog prfDL = new ProofDiscreteLog();
		ProofDiscreteLog prf = prfDL.getProofSmall();
		assertTrue(op.vrfNIZKP(prf));
	}

	/**
	 * Tests valid proof with large values
	 */
	@Test
	public void validProofLargeValues() throws NoSuchAlgorithmException {
		ProofDiscreteLog prfDL = new ProofDiscreteLog();
		ProofDiscreteLog prf = prfDL.getProofLarge();
		assertTrue(op.vrfNIZKP(prf));
	}
}
