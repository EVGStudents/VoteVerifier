/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.verification.PrimitivesVerifier;

/**
 * This class represent a Runner. A runner is used to perform the verifications
 * and there we can find the real implementation.
 * @author snake
 */
public abstract class Runner {
	
	/**
	 * The election board proxy
	 */
	protected ElectionBoardProxy ebproxy;

	/**
	 * The primitive verifier
	 */
	protected PrimitivesVerifier prmVrf;
	
	/**
	 * Start this runner, it will perform the verification 
	 * implemented
	 * @return the list of results
	 */
	public abstract void run();

	/**
	 * Set the election board proxy for this runner
	 * @param ebproxy the election board proxy
	 */
	public void setElectionBoardProxy(ElectionBoardProxy ebproxy){
		this.ebproxy = ebproxy;
	}

	/**
	 * Set the primitives verifier for this runner
	 * @param prmVrf the primitives verifier
	 */
	public void setPrimitivesVerifier(PrimitivesVerifier prmVrf){
		this.prmVrf = prmVrf;
	}
}
