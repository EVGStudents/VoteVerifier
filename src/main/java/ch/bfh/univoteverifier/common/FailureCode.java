/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.common;

/**
 * This class represent the failure codes for a VerificationEvent
 * @author snake
 */
public enum FailureCode {
	CLEAN(-1),
	NOT_YET_IMPLEMENTED(0),
	COMPOSITE_PRIME_NUMBER(10),
	FALSE_PARAMETERS_LENGTH(20),
	NOT_SAFE_PRIME(30),
	NOT_A_GENERATOR(40);
	
	private final int code;

	private FailureCode(int code){
		this.code = code;
	}

	public int getFailureCode(){
		return this.code;
	}
	

}
