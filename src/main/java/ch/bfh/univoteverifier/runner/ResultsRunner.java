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
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.List;

/**
 * This class represent a ResultRunner that is responsible to compute the
 * results of an election
 *
 * @author snake
 */
public class ResultsRunner extends Runner {

	/**
	 * Construct a ResultRunner with a given ElectionBoardProxy and
	 * Messenger.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param msgr the Messenger used to send the results.
	 */
	public ResultsRunner(ElectionBoardProxy ebp, Messenger msgr) {
		super(RunnerName.RESULT, msgr);
	}

	@Override
	public List<VerificationResult> run() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
