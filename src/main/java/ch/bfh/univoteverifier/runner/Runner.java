/**
*
*  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
*   Bern University of Applied Sciences, Engineering and Information Technology,
*   Research Institute for Security in the Information Society, E-Voting Group,
*   Biel, Switzerland.
*
*   Project independent UniVoteVerifier.
*
*/
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.verification.SectionNameEnum;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a Runner. A runner is used to perform the verifications
 * and there we can find the real implementation.
 * @author snake
 */
public abstract class Runner {
	

	protected List<VerificationEvent> partialResults;
	protected ElectionBoardProxy ebp;
	private final SectionNameEnum runnerName;

	public Runner(ElectionBoardProxy ebp, SectionNameEnum runnerName){
		this.ebp = ebp;
		this.runnerName = runnerName;
		this.partialResults = new ArrayList<>();
	}

	public SectionNameEnum getRunnerName(){
		return runnerName;
	}
	
	/**
	 * Start this runner, it will perform the verification 
	 * implemented
	 * @return the list of results
	 */
	public abstract List<VerificationEvent> run();

}
