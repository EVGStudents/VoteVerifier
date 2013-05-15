/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verificationtest;

import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.verification.UniversalVerification;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of an IndividualVerification.
 *
 * @author snake
 */
public class IndividualVerificationTester {

	Verification v;
	List<VerificationResult> mockList;
	List<VerificationResult> realList;
	String eID = "sub-2013";

	public IndividualVerificationTester() {
		v = new UniversalVerification(new Messenger(), eID);
		realList = v.runVerification();

		mockList = new ArrayList<>();
	}

	/**
	 * Test if the list contains all the results
	 */
	@Test
	public void testListSize() {
		assertEquals(mockList.size(), realList.size());
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
//		int i;
//
//		for (i = 0; i < mockList.size(); i++) {
//			if (i <= 5) {
//				assertEquals(realList.get(i).getVerificationType(), mockList.get(i).getVerificationType());
//				assertEquals(realList.get(i).getResult(), mockList.get(i).getResult());
//				assertTrue(realList.get(i).isImplemented());
//			}
//		}
	}
}
