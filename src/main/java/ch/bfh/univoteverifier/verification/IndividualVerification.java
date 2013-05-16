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
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;

/**
 * This class is used to perform an individual verification.
 *
 * @author snake
 */
public class IndividualVerification extends Verification {

	private final ElectionReceipt er;

	/**
	 * Construct an IndividualVerification with a given Messenger and
	 * ElectionReceipt.
	 *
	 * @param msgr the Messenger used to send messages.
	 * @param er the ElectionReceipt that contains the information about the
	 * ballot.
	 *
	 */
	public IndividualVerification(Messenger msgr, ElectionReceipt er) {
		super(msgr, er.getElectionID());
		this.er = er;
	}

	@Override
	public void createRunners() {
		try {
			IndividualRunner ir = new IndividualRunner(ebproxy, msgr, er);

			runners.add(ir);
		} catch (CertificateException | ElectionBoardServiceFault | InvalidNameException ex) {
			msgr.sendElectionSpecError(geteID(), ex);
		}
	}
}
