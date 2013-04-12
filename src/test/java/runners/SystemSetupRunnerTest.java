/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runners;

import ch.bfh.univoteverifier.runner.Runner;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.utils.CryptoUtils;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author snake
 */
public class SystemSetupRunnerTest {
	
	Runner r;
	
	public SystemSetupRunnerTest() {
		String eID = "vshbfh-2013";
		this.r = new SystemSetupRunner();
		this.r.seteID(eID);
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	// TODO add test methods here.
	// The methods must be annotated with annotation @Test. For example:
	//
	// @Test
	// public void hello() {}
	
	@Test
	public void SystemSetupRunnerVerCount(){
		List<VerificationResult> vr = r.run();
		
		assertEquals(vr.size(), 6);
	}

	@Test
	public void SystemSetupRunnerResult(){
		List<VerificationResult> res = new ArrayList<>();
		
		res.add(CryptoUtils.getVrfRes(Verification.SETUP_P_IS_PRIME, true));
		res.add(CryptoUtils.getVrfRes(Verification.SETUP_Q_IS_PRIME, true));
		res.add(CryptoUtils.getVrfRes(Verification.SETUP_G_IS_GENERATOR, true));
		res.add(CryptoUtils.getVrfRes(Verification.SETUP_P_IS_SAFE_PRIME, true));
		res.add(CryptoUtils.getVrfRes(Verification.SETUP_PARAM_LEN, true));
		res.add(CryptoUtils.getVrfRes(Verification.SETUP_EM_CERT, true));
	
		List<VerificationResult> trueRes = r.run();
		
		for(int i = 0 ; i < res.size() ; i++){
			assertEquals(res.get(i).getVerification(),trueRes.get(i).getVerification());
			assertEquals(res.get(i).getResult(), trueRes.get(i).getResult());
		}
		
	}
	
	
}