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
 * This class represent a MixerTallierRunner.
 *
 * @author snake
 */
public class MixerTallierRunner extends Runner {

	/**
	 * Construct an MixerTallierRunner with a given ElectionBoardProxy.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 * @param msgr the Messenger used to send the results.
	 */
	public MixerTallierRunner(ElectionBoardProxy ebp, Messenger msgr) {
		super(RunnerName.MIXING_TALLING, msgr);
	}

	@Override
	public List<VerificationResult> run() {
		return null;
	}
}
