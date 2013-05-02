/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.gui.VerificationSubject;
import ch.bfh.univoteverifier.verification.IndividualVerification;
import ch.bfh.univoteverifier.verification.UniversalVerification;
import ch.bfh.univoteverifier.verification.Verification;
import java.io.File;

/**
 * This class is the controller who is responsible for the communication between
 * the GUI and the internal infrastructure
 *
 * @author snake
 */
public class MainController {

	private Verification v;

	/**
	 * Create an universal verification
	 *
	 * @param eID String the ID of a election
	 */
	public void universalVerification(String eID) {
//		this.v = new UniversalVerification(eID);
	}

	/**
	 * Run a verification
	 */
	public void runVerifcation() {
		v.runVerification();
	}

	/**
	 * Create an individual verification
	 *
	 * @param eID String the ID of a election
	 */
	public void individualVerification(String eID) {
//		this.v = new IndividualVerification(eID);
	}

	/**
	 *
	 * @return
	 */
	public VerificationSubject getStatusSubject() {
		return v.getStatusSubject();
	}


}
