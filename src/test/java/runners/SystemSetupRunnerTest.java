/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runners;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.GUIMessenger;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.verification.SectionNameEnum;
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
public class SystemSetupRunnerTest {

	SystemSetupRunner ssr;
	List<VerificationResult> mockList;
	List<VerificationResult> realList;
	GUIMessenger gm ;
	
	public SystemSetupRunnerTest() {
		gm = new GUIMessenger();
		ElectionBoardProxy ebp = new ElectionBoardProxy("sub-2013");
		ssr = new SystemSetupRunner(ebp, "system setup runner",gm);
		realList = ssr.run();
		mockList = new ArrayList<>();
		
		mockList.add(new VerificationResult(VerificationEnum.SETUP_P_IS_PRIME, true));
		mockList.add(new VerificationResult(VerificationEnum.SETUP_Q_IS_PRIME, true));
		mockList.add(new VerificationResult(VerificationEnum.SETUP_G_IS_GENERATOR, true));
		mockList.add(new VerificationResult(VerificationEnum.SETUP_P_IS_SAFE_PRIME, true));
		mockList.add(new VerificationResult(VerificationEnum.SETUP_PARAM_LEN, true));

	}
	
	@Test
	public void testSizeOfResults(){
		assertEquals(mockList.size(), realList.size());
	}
	
	@Test
	public void testResultList(){
		int i;

		for(i = 0 ; i < mockList.size() ; i++){
			assertEquals(realList.get(i).getVerification(),mockList.get(i).getVerification());
			assertEquals(realList.get(i).getResult(), mockList.get(i).getResult());
			assertTrue(realList.get(i).isImplemented());
		}
	}

}