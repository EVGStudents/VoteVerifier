/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a Runner. A runner is used to perform the verifications
 * with a precise order.
 *
 * @author Scalzi Giuseppe
 */
public abstract class Runner {

	protected List<VerificationResult> partialResults;
	protected final Messenger msgr;
	protected final RunnerName runnerName;

	/**
	 * Construct a runner with an ElectionBoardProxy and a relative name.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param runnerName the name of this runner.
	 */
	public Runner(RunnerName runnerName, Messenger msgr) {
		this.runnerName = runnerName;
		this.partialResults = new ArrayList<>();
		this.msgr = msgr;
	}

	/**
	 * Get the name of this runner.
	 *
	 * @return the name of this runner.
	 */
	public RunnerName getRunnerName() {
		return runnerName;
	}

	/**
	 * Start this runner, it will perform the specified verifications.
	 *
	 * @return the list of results.
	 */
	public abstract List<VerificationResult> run() throws InterruptedException;
}
