/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runners;

import ch.bfh.univoteverifier.runner.Runner;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.utils.CryptoUtils;
import ch.bfh.univoteverifier.verification.PrimitivesVerifier;
import ch.bfh.univoteverifier.verification.VerificationEnum;
import ch.bfh.univoteverifier.verification.VerificationResult;
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
	
	SystemSetupRunner r;
	VerificationResult vr;
	
	public SystemSetupRunnerTest() {
		this.r = new SystemSetupRunner();
		this.r.setPrimitivesVerifier(new PrimitivesVerifier());
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
	public void testSignParamP(){
		vr = r.vrfSignParamP();

		assertEquals(VerificationEnum.SETUP_P_IS_PRIME, vr.getVerification());
		assertEquals(true, vr.getResult());
	}

	@Test
	public void testSignParamQ(){
		vr = r.vrfSignParamQ();
		
		assertEquals(VerificationEnum.SETUP_Q_IS_PRIME, vr.getVerification());
		assertEquals(true, vr.getResult());
	}
	
	@Test
	public void testSignParamG(){
		vr = r.vrfSignParamG();
		
		assertEquals(VerificationEnum.SETUP_G_IS_GENERATOR, vr.getVerification());
		assertEquals(true, vr.getResult());
	}
	
	@Test
	public void testSignParamLen(){
		vr = r.vrfSignParamLen();
		
		assertEquals(VerificationEnum.SETUP_PARAM_LEN, vr.getVerification());
		assertEquals(true, vr.getResult());
	}
	
	@Test
	public void testSignParamSafePrime(){
		vr = r.vrfSignParamSafePrime();
		
		assertEquals(VerificationEnum.SETUP_P_IS_SAFE_PRIME, vr.getVerification());
		assertEquals(true, vr.getResult());
	}
	
}