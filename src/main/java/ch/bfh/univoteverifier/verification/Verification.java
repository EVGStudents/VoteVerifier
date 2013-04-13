package ch.bfh.univoteverifier.verification;

/**
 * This enumeration is used to define the type of verifications
 * to perform.
 * @author snake
 */
public enum Verification {
	/**
	 *
	 */
	SETUP_P_IS_PRIME(100),
	/**
	 *
	 */
	SETUP_Q_IS_PRIME(110),
	/**
	 *
	 */
	SETUP_G_IS_GENERATOR(120),
	/**
	 *
	 */
	SETUP_PARAM_LEN(130),
	/**
	 *
	 */
	SETUP_P_IS_SAFE_PRIME(140),
	/**
	 *
	 */
	SETUP_EM_CERT(150);
	
	
	//the ID of the verification
	private final int code;
	
	/**
	 * Construct a Verification object
	 * @param code int the ID of the verification
	 */
	private Verification(int code){
		this.code = code;
	}
	
	
	/**
	 * Get the ID of the verification
	 * @return int the ID of the verification
	 */
	public int getID(){
		return code;
	}
	
}
