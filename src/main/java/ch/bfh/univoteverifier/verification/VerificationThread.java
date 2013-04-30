/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.common.Messenger;

/**
 * This class represent a thread that is responsible to start a verification so
 * that the GUI won't be blocked by background operations
 *
 * @author prinstin
 */
public class VerificationThread extends Thread {

	private final String eID;
	private final Verification v;
	private final Messenger msgr;

	/**
	 * Construct a verification thread with a given Messenger and election
	 * ID
	 *
	 * @param msgr the Messenger to where send the output
	 * @param eID the election ID
	 */
	public VerificationThread(Messenger msgr, String eID) {
		this.eID = eID;
		this.v = new UniversalVerification(msgr, eID);
		this.msgr = msgr;

	}

	@Override
	public void run() {
		v.runVerification();
	}
}
