/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package verifications;

import ch.bfh.univoteverifier.runner.Runner;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.utils.ElectionBoardProxy;
import ch.bfh.univoteverifier.verification.AbstractVerification;
import ch.bfh.univoteverifier.verification.UniversalVerification;
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
public class UniversalVerificationTest {
	
	AbstractVerification av;
	String eID = "test-2013";
	
	public UniversalVerificationTest() {
		av = new UniversalVerification(eID);
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
		assertEquals(eID, av.geteID());
	}
	
	@Test
	public void testRunnerAddition(){
		Runner r = new SystemSetupRunner();
		
		assertTrue(av.addRunner(r));
	}

	@Test
	public void testElectionBoardProxy(){
		assertNotNull(av.getEbproxy());
	}
	
}