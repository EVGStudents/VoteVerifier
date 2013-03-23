package utils;

public enum Verification {
	PRIMITIVE_P_IS_PRIME(100),
	PRIMITIVE_Q_IS_PRIME(110),
	PRIMITIVE_G_IS_GENERATOR(120),
	PRIMITIVE_LENGTH_OK(130),
	PRIMITIVE_P_IS_SAFE_PRIME(140);
	
	
	//the ID of the verification
	private int code;
	
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
