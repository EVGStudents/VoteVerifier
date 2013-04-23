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
package ch.bfh.univoteverifier.commontest;

import ch.bfh.univoteverifier.common.MainController;
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