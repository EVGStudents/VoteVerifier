/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a Runner. A runner is used to perform the verifications
 * and there we can find the real implementation.
 * @author snake
 */
public abstract class Runner {
	

	protected List<VerificationResult> partialResults;
	protected ElectionBoardProxy ebp;

	public Runner(ElectionBoardProxy ebp){
		this.ebp = ebp;
		this.partialResults = new ArrayList<>();
	}
	
	/**
	 * Start this runner, it will perform the verification 
	 * implemented
	 * @return the list of results
	 */
	public abstract List<VerificationResult> run();

}
