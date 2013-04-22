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
	private final boolean impl;
	
	/**
	 * Create a new verification results
	 * @param v Verification The type of verification
	 * @param result boolean
	 */
	public VerificationResult(VerificationEnum v, boolean result) {
		this.v=v;
		this.result=result;
		this.impl = true;
	}

	/**
	 * Create a new verification results
	 * @param v Verification The type of verification
	 * @param result boolean
	 * @þaram impl if the test implied in this result is implemented or not
	 */
	public VerificationResult(VerificationEnum v, boolean result, boolean impl){
		this.v=v;
		this.result=result;
		this.impl = impl;
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
	public boolean getResult() {
		return result;
	}

	/**
	 * To know if the verification associated with this
	 * result is implemented
	 * @return 
	 */
	public boolean isImplemented(){
		return impl;
	}

	
}
