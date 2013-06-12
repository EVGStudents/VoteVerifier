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
 * This class represent the failure codes for a VerificationResult.
 *
 * @author Scalzi Giuseppe
 */
public enum FailureCode {

	NOT_YET_IMPLEMENTED(0),
	COMPOSITE_PRIME_NUMBER(10),
	FALSE_PARAMETERS_LENGTH(20),
	NOT_SAFE_PRIME(30),
	NOT_A_GENERATOR(40),
	INVALID_CERTIFICATE(50),
	INVALID_RSA_SIGNATURE(60),
	INVALID_NIZKP(70),
	ENC_KEY_SHARE_NOT_EQUALS(80),
	ELECTION_GEN_NOT_EQUALS(90),
	SET_VERIFICATION_KEYS_NOT_EQUALS(100),
	NEW_SET_VERIFICATION_KEYS_NOT_EQUALS(110),
	VK_PLAUSIBILITY_CHECK_FAILED(120),
	ENCRYPTED_VOTES_PLAUSIBILITY_CHECK_FAILED(130),
	ENCRYPTED_VOTES_NOT_EQUALS(140),
	BALLOT_NOT_IN_SET(150),
	INVALID_BALLOT_VK(160),
	INVALID_SCHNORR_SIGN(170),
	INVALID_BALLOT(180),
	LATELY_KEY_PLAUSIBILITY_CHECK(190),
	INVALID_VOTES(200),
	INVALID_ALL_BALLOTS(210);
	private final int id;

	/**
	 * Construct a new failure code with a given ID.
	 *
	 * @param id the id for this failure.
	 */
	private FailureCode(int id) {
		this.id = id;
	}

	/**
	 * Get the ID for this failure.
	 *
	 * @return the ID of the failure.
	 */
	public int getID() {
		return this.id;
	}
}
