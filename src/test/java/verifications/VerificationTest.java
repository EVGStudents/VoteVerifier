/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package verifications;

import ch.bfh.univoteverifier.runner.Runner;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.verification.VerificationEnum;
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
public class VerificationTest {
	
	Verification vU;
	Verification vI;
	String eID = "test-2013";
	
	public VerificationTest() {
		v = new Verification(eID,VerificationEnum.UNIVERSAL);
		vI = new Verification(eID, VerificationEnum.INDIVIDUAL);
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
	public void testElectionID(){
		assertEquals(eID, vU.geteID());
	}
	
	@Test
	public void testRunnerAddition(){
		Runner r = new SystemSetupRunner();
		
		assertTrue(vU.addRunner(r));
	}

	@Test
	public void testElectionBoardProxy(){
		assertNotNull(vU.getEbproxy());
	}

	@Test
	public void testVerificationTypeIndividual(){
		assertEquals(vI.getVerificationType(), VerificationEnum.INDIVIDUAL),
	}
	
	@Test
	public void testVerificationTypeUniversal(){
		assertEquals(vU.getVerificationType(), VerificationEnum.UNIVERSAL),
	}
	
}