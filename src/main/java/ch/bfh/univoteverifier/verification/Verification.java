/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.listener.VerificationSubject;
import ch.bfh.univoteverifier.runner.Runner;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.runner.ResultsRunner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This abstract class represent a verification.
 *
 * @author Justin Springer
 */
public abstract class Verification {

	private static final Logger LOGGER = Logger.getLogger(Verification.class.getName());
	private final String eID;
	protected List<Runner> runners;
	protected ElectionBoardProxy ebproxy;
	protected Messenger msgr;
	//used to store the results of a verification
	protected List<VerificationResult> res;

	/**
	 * Construct a new abstract verification with a given election ID.
	 *
	 * @param msgr the Messenger used to manage the output
	 * @param eID the ID of an election.
	 *
	 */
	public Verification(Messenger msgr, String eID) {
		this.eID = eID;

		this.ebproxy = new ElectionBoardProxy(eID);

		runners = new ArrayList<>();
		res = new ArrayList<>();
		this.msgr = msgr;
	}

	/**
	 * Construct a new abstract verification with a given election ID.
	 *
	 * @param msgr the Messenger used to manage the output
	 * @param eID the ID of an election.
	 * @param test if true use locally saved data, is false do nothing.
	 *
	 */
	public Verification(Messenger msgr, String eID, boolean test) {
		this.eID = eID;

		if (test) {
			try {
				this.ebproxy = new ElectionBoardProxy(eID, true);
			} catch (FileNotFoundException ex) {
				Logger.getLogger(Verification.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		runners = new ArrayList<>();
		res = new ArrayList<>();
		this.msgr = msgr;
	}

	/**
	 * Get the status subject of the Messenger stored in this class.
	 *
	 * @return the status subject.
	 */
	public VerificationSubject getStatusSubject() {
		return this.msgr.getStatusSubject();
	}

	/**
	 * Get the election id for this verification.
	 *
	 * @return the eID.
	 */
	public String geteID() {
		return eID;
	}

	/**
	 * Run a verification.
	 */
	public List<VerificationResult> runVerification() {

		try {
			//crate the runners
			createRunners();

			if (runners.isEmpty()) {
				LOGGER.log(Level.INFO, "There aren't runners. The verification will not run.");
			}

			//run the runners  and get the results
			for (Runner r : runners) {
				//the result runner doesn't support run() but runResult().
				if (r instanceof ResultsRunner) {
					ResultsRunner rr = (ResultsRunner) r;
					rr.runResults();
				} else {
					List<VerificationResult> l = r.run();

					//check that a list isn't empty
					if (l != null) {
						res.addAll(l);
					} else {
						LOGGER.log(Level.INFO, "The runner {0} does not contain any verification.", r.getRunnerName());
					}
				}


			}

			msgr.sendVerificationFinished(eID);
		} catch (InterruptedException ex) {
			//stop the thread and end verification.
			Thread.currentThread().interrupt();
			return null;
		}

		return Collections.unmodifiableList(res);
	}

	/**
	 * Create the necessaries runners.
	 */
	protected abstract void createRunners();
}
