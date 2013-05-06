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

import ch.bfh.univoteverifier.common.VerificationOrder;
import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.gui.VerificationSubject;
import ch.bfh.univoteverifier.runner.Runner;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This abstract class represent a verification.
 *
 * @author prinstin
 */
public abstract class Verification {

	private static final Logger LOGGER = Logger.getLogger(Verification.class.getName());
	private final String eID;
	protected List<Runner> runners;
	protected ElectionBoardProxy ebproxy;
	protected VerificationOrder displayType = VerificationOrder.BY_SPEC;
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
	 * Set the view type for the verification.
	 *
	 * @param t the verification view.
	 */
	public void setViewType(VerificationOrder t) {
		displayType = t;
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

		if (runners.isEmpty()) {
			LOGGER.log(Level.INFO, "There aren't runners. The verification will not run.");
		}

		//run the runners  and get the results
		for (Runner r : runners) {
			List<VerificationResult> l = r.run();

			//check that a list isn't empty
			if (l != null) {
				res.addAll(l);
			} else {
				LOGGER.log(Level.INFO, "The runner {0} does not contain any verification.", r.getRunnerName());
			}
		}

		return Collections.unmodifiableList(res);
	}
}
