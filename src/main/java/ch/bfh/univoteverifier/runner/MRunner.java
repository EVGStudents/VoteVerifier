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
 * This class represent a MRunner that is responsible to run the verification
 * for the mixer entity.
 *
 * @author snake
 */
public class MRunner extends Runner {

	/**
	 * Construct a MRunner with a given ElectionBoardProxy.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 */
	public MRunner(ElectionBoardProxy ebp, Messenger msgr) {
		super(RunnerName.MIXER, msgr);
	}

	@Override
	public List<VerificationResult> run() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
