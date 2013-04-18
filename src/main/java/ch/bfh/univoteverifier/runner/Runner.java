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
	private final String runnerName;

	public Runner(ElectionBoardProxy ebp, String runnerName){
		this.ebp = ebp;
		this.runnerName = runnerName;
		this.partialResults = new ArrayList<>();
	}

	public String getRunnerName(){
		return runnerName;
	}
	
	/**
	 * Start this runner, it will perform the verification 
	 * implemented
	 * @return the list of results
	 */
	public abstract List<VerificationResult> run();

}
