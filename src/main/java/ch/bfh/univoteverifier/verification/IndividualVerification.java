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

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.runner.IndividualRunner;
import ch.bfh.univoteverifier.runner.ResultsRunner;
import java.security.cert.CertificateException;
import javax.naming.InvalidNameException;

/**
 * This class is used to perform an individual verification.
 *
 * @author Scalzi Giuseppe
 */
public class IndividualVerification extends Verification {

	private final ElectionReceipt er;

	/**
	 * Construct an IndividualVerification with a given Messenger and
	 * ElectionReceipt.
	 *
	 * @param msgr the Messenger used to send messages.
	 * @param eID the election ID
	 * @param er the ElectionReceipt that contains the information about the
	 * ballot.
	 *
	 */
	public IndividualVerification(Messenger msgr, String eID, ElectionReceipt er) {
		super(msgr, eID);
		this.er = er;
	}

	@Override
	protected void createRunners() {
		IndividualRunner ir = new IndividualRunner(ebproxy, msgr, er);
		ResultsRunner rr = new ResultsRunner(ebproxy, msgr);

		runners.add(ir);
		runners.add(rr);
	}
}
