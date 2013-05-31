/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.runner.ElectionPeriodRunner;
import ch.bfh.univoteverifier.runner.ElectionPreparationRunner;
import ch.bfh.univoteverifier.runner.ElectionSetupRunner;
import ch.bfh.univoteverifier.runner.MixerTallierRunner;
import ch.bfh.univoteverifier.runner.ResultsRunner;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;

/**
 * This class is used to perform an universal verification.
 *
 * @author Scalzi Giuseppe
 */
public class UniversalVerification extends Verification {

	/**
	 * Construct a universal verification with an election id.
	 *
	 * @param eID the ID of the election.
	 * @param msgr the entity used to exchange messages with the GUI.
	 */
	public UniversalVerification(Messenger msgr, String eID) {
		super(msgr, eID);
	}

	@Override
	protected void createRunners() {
		SystemSetupRunner ssr = new SystemSetupRunner(ebproxy, msgr);
		ElectionSetupRunner esr = new ElectionSetupRunner(ebproxy, msgr);
		ElectionPreparationRunner epr = new ElectionPreparationRunner(ebproxy, msgr);
		ElectionPeriodRunner eperiodr = new ElectionPeriodRunner(ebproxy, msgr);
		MixerTallierRunner mtr = new MixerTallierRunner(ebproxy, msgr);
		ResultsRunner rr = new ResultsRunner(ebproxy, msgr);

		runners.add(rr);
		runners.add(ssr);
		runners.add(esr);
		runners.add(epr);
		runners.add(eperiodr);
		runners.add(mtr);

	}
}
