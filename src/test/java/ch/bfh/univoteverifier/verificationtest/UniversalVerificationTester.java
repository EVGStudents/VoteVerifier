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
package ch.bfh.univoteverifier.verificationtest;

import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.verification.UniversalVerification;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of a UniversalVerification
 *
 * @author snake
 */
public class UniversalVerificationTester {

	Verification v;
	List<VerificationResult> mockList;
	List<VerificationResult> realList;
	String eID = "sub-2013";
	final int RES_COUNT = 5;

	public UniversalVerificationTester() {
		v = new UniversalVerification(new Messenger(), eID);
		realList = v.runVerification();

		mockList = new ArrayList<>();
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_P, true, eID, RunnerName.UNSET));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_Q, true, eID, RunnerName.UNSET));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_G, true, eID, RunnerName.UNSET));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_P_SAFE_PRIME, true, eID, RunnerName.UNSET));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_PARAM_LEN, true, eID, RunnerName.UNSET));
		//ToDO add all the results when they are available
	}

	/**
	 * Test if the list contains all the results
	 */
	@Test
	public void testListSize() {
		assertEquals(RES_COUNT, realList.size());
	}

	/**
	 * Test if the election contains the correct election ID
	 */
	@Test
	public void testgetelectionID() {
		assertEquals(eID, v.geteID());
	}

	/**
	 * Test if the final result list is correct
	 */
	@Test
	public void testFinalResultList() {
		int i;

		for (i = 0; i < mockList.size(); i++) {
			if (i <= 5) {
				assertEquals(realList.get(i).getVerificationEnum(), mockList.get(i).getVerificationEnum());
				assertEquals(realList.get(i).getResult(), mockList.get(i).getResult());
				assertTrue(realList.get(i).isImplemented());
			}
		}
	}
}
