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
	
	@Test
	public void runUniVrf(){
		mc.universalVerification(eID);
	}
}