/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package verifications;

import ch.bfh.univoteverifier.verification.SectionNameEnum;
import ch.bfh.univoteverifier.verification.UniversalVerification;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.verification.VerificationEnum;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author snake
 */
public class UniversalVerificationTester {
	
	
	Verification v;
	List<VerificationResult> mockList;
	List<VerificationResult> realList;
	String eID = "sub-2013";
	
	public UniversalVerificationTester() {
		v = new UniversalVerification(eID);
		realList = v.runVerification();
		
	
		mockList = new ArrayList<>();
		mockList.add(new VerificationResult(VerificationEnum.SETUP_P_IS_PRIME, true));
		mockList.add(new VerificationResult(VerificationEnum.SETUP_Q_IS_PRIME, true));
		mockList.add(new VerificationResult(VerificationEnum.SETUP_G_IS_GENERATOR, true));
		mockList.add(new VerificationResult(VerificationEnum.SETUP_P_IS_SAFE_PRIME, true));
		mockList.add(new VerificationResult(VerificationEnum.SETUP_PARAM_LEN, true));
		//ToDO add all the results
	}
	
	@Test
	public void testEmptyList(){
		assertEquals(5, realList.size());
	}
	
	@Test
	public void testgetelectionID(){
		assertEquals(eID, v.geteID());
	}

	@Test
	public void testFinalResultList(){
		int i;
		
		System.out.println(mockList.size());
		
		for(i = 0 ; i < mockList.size() ; i++){
			if(i <= 5 ){
				assertEquals(realList.get(i).getVerification(),mockList.get(i).getVerification());
				assertEquals(realList.get(i).getResult(), mockList.get(i).getResult());
				assertEquals(realList.get(i).getSectionName(), SectionNameEnum.SYSTEM_SETUP);
				assertTrue(realList.get(i).isImplemented());
			}
		}
	}
}