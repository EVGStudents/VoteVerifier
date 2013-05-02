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
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to perform an individual verification
 *
 * @author snake
 */
public class IndividualVerification extends Verification {

	/**
	 * Construct an IndividualVerification with a given election ID
	 *
	 * @param File the file with a path to a QR Code
	 */
	public IndividualVerification(Messenger msgr, File qrCodeFile) {
		super(msgr, "electionID");
		ElectionReceipt er = getElectionReceipt(qrCodeFile);
	}

	public ElectionReceipt getElectionReceipt(File qrCodeFile) {
		QRCode qr = new QRCode(super.msgr);
		ElectionReceipt er = null;
		try {
			er = qr.decodeReceipt(qrCodeFile);
		} catch (IOException ex) {
			Logger.getLogger(IndividualVerification.class.getName()).log(Level.SEVERE, "An error occured while processing the file", ex);
		}
		return er;

	}

	/**
	 * Create the necessaries runners used to print the results ordered by
	 * the specification
	 */
	private void createRunnerBySpec() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	/**
	 * Create the necessaries runners used to print the results ordered by
	 * the entities
	 */
	private void createRunnerByEntities() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
