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

/**
 * This class is used to perform an individual verification
 *
 * @author snake
 */
public class IndividualVerification extends Verification {

	/**
	 * Construct an IndividualVerification with a given election ID
	 *
	 * @param eID the election ID
	 */
	public IndividualVerification(String eID) {
		super(eID);
	}

	/**
	 * Create the necessaries runners used to print the results ordered by
	 * the specification
	 */
	private void createRunnerBySpec() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	/**
	 * Create the necessaries runners used to print the results ordered by
	 * the entities
	 */
	private void createRunnerByEntities() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
