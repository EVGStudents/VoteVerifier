/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.common.FailureCode;

/**
 * Verification Event is a helper class that contains information that shall be
 * sent to the GUI this class is part of the observer pattern
 *
 * @author prinstin
 */
public class VerificationEvent {

	private final VerificationType v;
	private RunnerName section;
	private String msg;
	private boolean impl;
	private boolean result;
	private FailureCode fc;

	/**
	 * create a new verification event with an error message for the GUI
	 *
	 * @param v verification type for this event
	 * @param msg the message text that is being sent in this verification
	 * event
	 */
	public VerificationEvent(VerificationType v, String msg) {
		this.v = v;
		this.msg = msg;
		this.fc = null;
	}

	/**
	 * Create a new verification event
	 *
	 * @param v The type of verification that has succeeded
	 */
	public VerificationEvent(VerificationType v, boolean res) {
		this.v = v;
		this.result = res;
		this.impl = true;
		this.fc = null;
	}

	public RunnerName getSection() {
		return section;
	}

	public void setSection(RunnerName section) {
		this.section = section;
	}

	/**
	 * Get the verification type for this VerificationResult Identifies that
	 * type of message/information that this event contains
	 *
	 * @return the verification type
	 */
	public VerificationType getVerificationEnum() {
		return v;
	}

	/**
	 * Get the result for this VerificationEvent
	 *
	 * @return
	 */
	public boolean getResult() {
		return result;
	}

	/**
	 * To know if the verification associated with this result is
	 * implemented
	 *
	 * @return
	 */
	public boolean isImplemented() {
		return impl;
	}

	/**
	 * Set the implementation flag for this verification event
	 *
	 * @param impl the boolean value representing the implementation for
	 * this event
	 */
	public void setImplemented(boolean impl) {
		this.impl = impl;
	}

	/**
	 * get the String of the message this StatusEvent is delivering
	 *
	 * @return the message to deliver to the GUI
	 */
	public String getMessage() {
		return msg;
	}

	/**
	 * Get the failure code for this verification event
	 *
	 * @return
	 */
	public FailureCode getFailureCode() {
		return this.fc;
	}

	/**
	 * Set the failure code for this verification event
	 *
	 * @param fc the failure code
	 */
	public void setFailureCode(FailureCode fc) {
		this.fc = fc;
	}
}
