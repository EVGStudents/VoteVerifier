/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.common;

/**
 * This class represent the failure codes for a VerificationEvent
 *
 * @author snake
 */
public enum FailureCode {

	NOT_YET_IMPLEMENTED(0),
	COMPOSITE_PRIME_NUMBER(10),
	FALSE_PARAMETERS_LENGTH(20),
	NOT_SAFE_PRIME(30),
	NOT_A_GENERATOR(40);
	private final int id;

	/**
	 * Construct a new failure code with a given ID
	 *
	 * @param ID the ID for this failure
	 */
	private FailureCode(int id) {
		this.id = id;
	}

	/**
	 * Get the ID for this failure
	 *
	 * @return the ID of the failure
	 */
	public int getID() {
		return this.id;
	}
}
