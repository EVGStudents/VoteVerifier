/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import ch.bfh.univoteverifier.common.MainController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class is used to test the root logger behavior
 * @author snake
 */
public class MainControllerTest {

	MainController mc;
	String eID = "sub-2013";
	
	public MainControllerTest() {
		mc = new MainController();
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
	public void runUniVrf(){
		mc.universalVerification(eID);
	}
}