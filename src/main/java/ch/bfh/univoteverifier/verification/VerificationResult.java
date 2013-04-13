/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

/**
 * This is an helper class used to store the result of a verification along
 * with its type
 * @author prinstin
 */
public class VerificationResult {
	
	private final VerificationEnum v;
	private final boolean result;
	
	
	/**
	 * Create a new verification results
	 * @param v Verification The type of verification
	 * @param result boolean
	 */
	public VerificationResult(VerificationEnum v, boolean result) {
		this.v=v;
		this.result=result;
	}

	/**
	 * Get the verification type for this VerificationResult
	 * @return the verification type
	 */
	public VerificationEnum getVerification() {
		return v;
	}

	/**
	 * Get the result for this VerificationResult
	 * @return 
	 */
	public Boolean getResult() {
		return result;
	}
	
}
