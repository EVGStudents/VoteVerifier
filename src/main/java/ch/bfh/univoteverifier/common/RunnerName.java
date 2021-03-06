/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.common;

/**
 * This enumeration specify the name of the sections for the verification
 * results.
 *
 * @author Scalzi Giuseppe
 */
public enum RunnerName {

	UNSET(0),
	SYSTEM_SETUP(1),
	ELECTION_SETUP(2),
	ELECTION_PREPARATION(3),
	ELECTION_PERIOD(4),
	MIXING_TALLING(5),
	RESULT(6),
	INDIVIDUAL(7);
	private final int id;

	/**
	 * Construct a new section name with a given ID.
	 *
	 * @param id the ID for this section name.
	 */
	private RunnerName(int id) {
		this.id = id;
	}

	/**
	 * Get the ID of this RunnerName.
	 *
	 * @return the ID of the section name.
	 */
	public int getID() {
		return this.id;
	}
}
