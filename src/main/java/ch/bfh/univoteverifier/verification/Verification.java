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
import ch.bfh.univoteverifier.common.GUIMessenger;
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
	protected final List<Runner> runners;
	protected final ElectionBoardProxy ebproxy;
	protected VerificationOrder displayType = VerificationOrder.BY_SPEC;
	protected GUIMessenger gm;
	//used to store the results of a verification
	//maybe add two list for the two types of order
	protected List<VerificationEvent> res;

	/**
	 * Construct a new abstract verification with a given election ID.
	 *
	 * @param eID the ID of an election
	 */
	public Verification(String eID) {
		this.eID = eID;
		this.ebproxy = new ElectionBoardProxy(eID);
		runners = new ArrayList<>();
		res = new ArrayList<>();
		this.gm = new GUIMessenger();

		//ToDo check if is correct
		LOGGER.setUseParentHandlers(true);
	}

	/**
	 * Set the view type for the verification.
	 *
	 * @param t the verification view
	 */
	public void setViewType(VerificationOrder t) {
		displayType = t;
	}

	/**
	 * Get the status subject of the GUIMessenger stored in this class.
	 *
	 * @return the status subject
	 */
	public VerificationSubject getStatusSubject() {
		return this.gm.getStatusSubject();
	}

	/**
	 * @return the eID
	 */
	public String geteID() {
		return eID;
	}

	/**
	 * Initialize the certificates of the different entities.
	 */
	private void initializeEntityCertificates() {
		try {
			//initialize ca, em, ea certificate
			Config.caCert = CryptoFunc.getX509Certificate(ebproxy.getElectionSystemInfo().getCertificateAuthority().getValue());
			Config.emCert = CryptoFunc.getX509Certificate(ebproxy.getElectionSystemInfo().getElectionManager().getValue());
			Config.eaCert = CryptoFunc.getX509Certificate(ebproxy.getElectionSystemInfo().getElectionAdministration().getValue());

			//initialize mixers certificates
			for (Certificate mCert : ebproxy.getElectionSystemInfo().getMixer()) {
				Config.mCerts.put("mixer", CryptoFunc.getX509Certificate(mCert.getValue()));
			}

			//initialize talliers certificates
			for (Certificate tCert : ebproxy.getElectionSystemInfo().getTallier()) {
				Config.tCerts.put("tallier", CryptoFunc.getX509Certificate(tCert.getValue()));
			}

		} catch (CertificateException | ElectionBoardServiceFault ex) {
			LOGGER.log(Level.SEVERE, null, ex);
			gm.sendErrorMsg(ex.getMessage());
		}
	}

	/**
	 * Run a verification
	 */
	public List<VerificationEvent> runVerification() {

		//initialize the public keys - ToDO decomment this when the webservices will work
		//		initializeEntityCertificates();

		if (runners.isEmpty()) {
			LOGGER.log(Level.INFO, "There aren't runners. The verification will not run.");
		}

		//run the runners  and get the results
		for (Runner r : runners) {
			List<VerificationEvent> l = r.run();

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
