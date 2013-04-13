/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.utils.ElectionBoardProxy;
import ch.bfh.univoteverifier.verification.PrimitivesVerifier;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a Runner. A runner is used to perform the verifications
 * and there we can find the real implementation.
 * @author snake
 */
public abstract class Runner {
	/**
	 * Election ID
	 */
	protected String eID;
	/**
	 * Primitive Verifier 
	 */
	protected PrimitivesVerifier prmVrf;
	/**
	 * The list with all the results for this runner
	 */
	protected List<VerificationResult> result;
	/**
	 * The election board proxy
	 */
	protected ElectionBoardProxy ebproxy;

	/**
	 * Construct a runner
	 */
	public Runner() {
		this.result = new ArrayList<>();
		this.prmVrf = new PrimitivesVerifier();
	}

	/**
	 * Start this runner, it will perform the verification 
	 * implemented
	 * @return the list of results
	 */
	public abstract List<VerificationResult> run();

	/**
	 * Set the election ID for this runner
	 * @param eID the election ID
	 */
	public void seteID(String eID) {
		this.eID = eID;
	}

	/**
	 * Set the election board proxy for this runner
	 * @param ebproxy the election board proxy
	 */
	public void setElectionBoardProxy(ElectionBoardProxy ebproxy){
		this.ebproxy = ebproxy;
	}
}
