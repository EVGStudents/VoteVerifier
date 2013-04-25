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

/**
 * This enumeration contains the list of different verifications
 *
 * @author snake
 */
public enum VerificationType {

	SETUP_P_IS_PRIME(100),
	SETUP_Q_IS_PRIME(110),
	SETUP_G_IS_GENERATOR(120),
	SETUP_PARAM_LEN(130),
	SETUP_P_IS_SAFE_PRIME(140),
	SETUP_EM_CERT(150),
	SETUP_CA_CERT(160),
	SETUP_SIGN_PARAM_SIGN(170),
	EL_SETUP_EA_CERT(200),
	EL_SETUP_EA_CERT_SIGN(210),
	//ToDO check if it is useful
	ERROR(666);
	//the ID of the verification
	private final int code;

	/**
	 * Construct a Verification object
	 *
	 * @param code int the ID of the verification
	 */
	private VerificationType(int code) {
		this.code = code;
	}

	/**
	 * Get the ID of the verification
	 *
	 * @return int the ID of the verification
	 */
	public int getID() {
		return code;
	}
}
