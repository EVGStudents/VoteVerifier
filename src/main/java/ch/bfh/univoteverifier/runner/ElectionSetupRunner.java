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
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.util.List;

/**
 * This class represent an ElectionSetupRunner
 *
 * @author snake
 */
public class ElectionSetupRunner extends Runner {

	private final Messenger gm;

	/**
	 * Construct an ElectionSetupRunner with a given ElectionBoardProxy
	 *
	 * @param ebp the ElectionBoardProxy from where get the data
	 */
	public ElectionSetupRunner(ElectionBoardProxy ebp, Messenger gm) {
		super(ebp, RunnerName.ELECTION_SETUP);
		this.gm = gm;
	}

	@Override
	public List<VerificationEvent> run() {
		return null;
	}
}
