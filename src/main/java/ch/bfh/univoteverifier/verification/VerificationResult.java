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

/*
 *This class is a VerificationResult and it represent the result of a single
 * check (for example an RSA Signature). Here we can find some information like
 * the result or if the check is implemented or not.
 * @author prinstin
 */
public class VerificationResult {

	private final VerificationType v;
	private RunnerName rn;
	private String msg;
	private boolean impl;
	private final boolean result;
	private FailureCode fc;
	private String entityName;
	private final String eID;

	/**
	 * Create a new VerificationResult.
	 *
	 * @param v The type of verification that has succeeded.
	 * @param res The result.
	 * @param eID The election ID who need this VerificationResult.
	 * @param rn The name of the runner who wants to have this
	 * VerificationResult.
	 */
	public VerificationResult(VerificationType v, boolean res, String eID, RunnerName rn) {
		this.v = v;
		this.result = res;
		this.impl = true;
		this.fc = null;
		this.eID = eID;
		this.rn = rn;
	}

	/**
	 * The name of the section that has performed this test.
	 *
	 * @return the RunnerName of this VerificationResult.
	 */
	public RunnerName getRunnerName() {
		return rn;
	}

	/**
	 * Get the verification type for this VerificationResult Identifies that
	 * type of message/information that this event contains.
	 *
	 * @return the VerificationType.
	 */
	public VerificationType getVerificationType() {
		return v;
	}

	/**
	 * Get the result for this VerificationResult.
	 *
	 * @return the result as a boolean.
	 */
	public boolean getResult() {
		return result;
	}

	/**
	 * To know if the verification associated with this result is
	 * implemented.
	 *
	 * @return a boolean.
	 */
	public boolean isImplemented() {
		return impl;
	}

	/**
	 * Set the implementation flag for this VerificationResult.
	 *
	 * @param impl the boolean value representing the implementation for
	 * this event.
	 */
	public void setImplemented(boolean impl) {
		this.impl = impl;
	}

	/**
	 * Get the failure code for this VerificationResult.
	 *
	 * @return the FailureCode.
	 */
	public FailureCode getFailureCode() {
		return this.fc;
	}

	/**
	 * Set the failure code for this VerificationResult.
	 *
	 * @param fc the FailureCode.
	 */
	public void setFailureCode(FailureCode fc) {
		this.fc = fc;
	}

	/**
	 * Set the entity name for this VerificationResult if any. It can be use
	 * to print the name for example of a tallier.
	 *
	 * @param entityName the name for example of a tallier.
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Get the entity name.
	 *
	 * @return a String containing the name.
	 */
	public String getEntityName() {
		return this.entityName;
	}

	/**
	 * Get the election ID.
	 *
	 * @return a String with election ID.
	 */
	public String getElectionID() {
		return this.eID;
	}

	//ToDo check if is useful
	public String toString() {
		String s = "\n\tVerification Event"
			+ "" + getRunnerName()
			+ "" + getResult();

		return s;
	}
}
