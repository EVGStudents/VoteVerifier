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
 * This class represent a MixerTallierRunner.
 *
 * @author snake
 */
public class MixerTallierRunner extends Runner {

	private final Messenger gm;

	/**
	 * Construct an MixerTallierRunner with a given ElectionBoardProxy.
	 *
	 * @param ebp the ElectionBoardProxy from where get the data.
	 */
	public MixerTallierRunner(ElectionBoardProxy ebp, Messenger gm) {
		super(ebp, RunnerName.MIXING_TALLING);
		this.gm = gm;
	}

	@Override
	public List<VerificationEvent> run() {
		return null;
	}
}
