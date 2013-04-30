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
 * This enumeration contains the list of the different verifications
 *
 * @author snake
 */
public enum VerificationType {

	SETUP_P_IS_PRIME(100),
	SETUP_Q_IS_PRIME(110),
	SETUP_P_IS_SAFE_PRIME(120),
	SETUP_G_IS_GENERATOR(130),
	SETUP_PARAM_LEN(140),
	SETUP_EM_CERT(150),
	EL_SETUP_EA_CERT(200),
	EL_SETUP_EA_CERT_ID_SIGN(205),
	EL_SETUP_BASICS_PARAMS_SIGN(210),
	EL_SETUP_TALLIERS_CERT(215),
	EL_SETUP_MIXERS_CERT(220),
	EL_SETUP_T_CERT_M_CERT_ID_SIGN(225),
	EL_SETUP_ELGAMAL_PARAMS(230),
	EL_SETUP_ELGAMAL_PARAMS_SIGN(235),
	EL_SETUP_T_NIZKP_OF_X(240),
	EL_SETUP_T_NIZKP_OF_X_SIGN(245),
	EL_SETUP_T_PUBLIC_KEY(250),
	EL_SETUP_T_PUBLIC_KEY_SIGN(255),
	EL_SETUP_M_NIZKP_OF_ALPHA(260),
	EL_SETUP_M_NIZKP_OF_ALPHA_SIGN(265),
	EL_SETUP_ANON_GEN(270),
	EL_SETUP_ANON_GEN_SIGN(275),
	EL_PREP_C_AND_R_SIGN(300),
	EL_PREP_EDATA_SIGN(310),
	EL_PREP_ELIG_VOTERS_SIGN(320),
	EL_PREP_VOTERS_CERT_SIGN(330),
	EL_PREP_ELIG_VOTERS_CERT(340),
	EL_PREP_M_PUB_VER_KEYS(350),
	EL_PREP_M_PUB_VER_KEYS_SIGN(360),
	EL_PREP_PUB_VER_KEYS(370),
	EL_PREP_PUB_VER_KEYS_SIGN(380),
	EL_PERIOD_LATE_NEW_VOTER_CERT(400),
	EL_PERIOD_LATE_NEW_VOTER_CERT_SIGN(405),
	EL_PERIOD_M_NIZKP_EQUALITY(410),
	EL_PERIOD_M_NIZKP_EQUALITY_SIGN(415),
	EL_PERIOD_NEW_VER_KEY(420),
	EL_PERIOD_NEW_VER_KEY_SIGN(425),
	EL_PERIOD_M_VER_KEY_NIZKP_OF_ALPHA(430),
	EL_PERIOD_M_VER_KEY_NIZKP_OF_ALPHA_SIGN(435),
	EL_PERIOD_LAST_M_VER_KEY(440),
	EL_PERIOD_LAST_M_VER_KEY_SIGN(445),
	EL_PERIOD_BALLOT(450),
	EL_PERIOD_BALLOT_SIGN(455),
	MT_M_ENC_VOTES_CHECK(500),
	MT_M_ENC_VOTES_CHECK_SIGN(510),
	MT_ENC_VOTES_ID_SIGN(520),
	MT_T_NIZKP_OF_X(530),
	MT_T_NIZKP_OF_X_SIGN(540),
	MT_VALID_PLAINTEXT_VOTES(550),
	MT_VALID_PLAINTEXT_VOTES_SIGN(560),
	//ToDO check if it is useful
	ERROR(666);
	//the ID of the verification
	private final int id;

	/**
	 * Construct a verification type with a given ID
	 *
	 * @param id the ID of the verification type
	 */
	private VerificationType(int id) {
		this.id = id;
	}

	/**
	 * Get the ID of the verification
	 *
	 * @return the ID of the verification
	 */
	public int getID() {
		return id;
	}
}
