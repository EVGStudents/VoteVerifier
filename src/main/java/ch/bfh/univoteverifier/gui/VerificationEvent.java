/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.verification.VerificationResult;

/**
 *
 * Verification Event is a helper class that contains information that shall be
 * sent to the GUI this class is part of the observer pattern
 *
 *
 * @author snake
 */
public class VerificationEvent {

	private String msg;
	private VerificationMessage vm;
	private final VerificationResult vr;

	public VerificationEvent(VerificationMessage vm, String msg) {
		this.msg = msg;
		this.vm = vm;
		this.vr = null;
	}

	public VerificationEvent(VerificationResult vr) {
		this.vr = vr;
		this.vm = VerificationMessage.RESULT;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
