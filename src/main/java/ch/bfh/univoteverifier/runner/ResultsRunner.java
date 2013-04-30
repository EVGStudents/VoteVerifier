/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.runner;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.util.List;

/**
 * This class represent a ResultRunner that is responsible to compute the
 * results of an election
 *
 * @author snake
 */
public class ResultsRunner extends Runner {

	/**
	 * Construct a ResultRunner with a given ElectionBoardProxy
	 *
	 * @param ebp the ElectionBoardProxy from where get the data
	 */
	public ResultsRunner(ElectionBoardProxy ebp) {
		super(ebp, RunnerName.RESULT);
	}

	@Override
	public List<VerificationEvent> run() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
