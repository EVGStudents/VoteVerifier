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

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a Runner. A runner is used to perform the verifications
 * with a precise order.
 *
 * @author snake
 */
public abstract class Runner {

	protected List<VerificationEvent> partialResults;
	protected ElectionBoardProxy ebp;
	private final RunnerName runnerName;

	/**
	 * Construct a runner with an ElectionBoardProxy and a relative name.
	 *
	 * @param ebp
	 * @param runnerName
	 */
	public Runner(ElectionBoardProxy ebp, RunnerName runnerName) {
		this.ebp = ebp;
		this.runnerName = runnerName;
		this.partialResults = new ArrayList<>();
	}

	/**
	 * Get the name of this runner.
	 *
	 * @return the name of this runner
	 */
	public RunnerName getRunnerName() {
		return runnerName;
	}

	/**
	 * Start this runner, it will perform the verification implemented.
	 *
	 * @return the list of results
	 */
	public abstract List<VerificationEvent> run();
}
