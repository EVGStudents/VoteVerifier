/**
*
*  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
*   Bern University of Applied Sciences, Engineering and Information Technology,
*   Research Institute for Security in the Information Society, E-Voting Group,
*   Biel, Switzerland.
*
*   Project independent UniVoteVerifier.
*
*/
package ch.bfh.univoteverifier.verification;

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
 * This abstract class represent a verification
 * @author prinstin
 */
public abstract class Verification {
	
	private static final Logger LOGGER = Logger.getLogger(Verification.class.getName());
	private final String eID;
	protected final List<Runner> runners;
	protected final ElectionBoardProxy ebproxy;
	protected VerificationEnum displayType = VerificationEnum.ORDER_BY_SPEC;
	protected GUIMessenger gm;
	
	//used to store the results of a verification
	//maybe add two list for the two types of order
	protected List<VerificationEvent> res;
	
	/**
	 * Construct a new abstract verification with a given election ID
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
	 * Set the view type for the verification
	 * @param t the verification view
	 */
	public void setViewType(VerificationEnum t){
		if(t != VerificationEnum.ORDER_BY_ENTITES || t != VerificationEnum.ORDER_BY_SPEC){
			LOGGER.log(Level.INFO, "View type not specified, falling back to specification view");
			displayType = VerificationEnum.ORDER_BY_SPEC;
		}
		else
			displayType = t;
	}
	
	/**
	 * Get the status subject of the GUIMessenger stored in this class
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
	 * Initialize the public key of the different entities as well as talliers and mixers count
	 */
	private void initializePublicKeys() {
		try {
			//initialize ca, em, ea
			Config.ca = CryptoFunc.getRSAPublicKeyFromCert(ebproxy.getElectionSystemInfo().getCertificateAuthority().getValue());
			Config.em = CryptoFunc.getRSAPublicKeyFromCert(ebproxy.getElectionSystemInfo().getElectionManager().getValue());
			Config.ea = CryptoFunc.getRSAPublicKeyFromCert(ebproxy.getElectionSystemInfo().getElectionAdministration().getValue());
			
			//initialize mixers
			for(Certificate mCert : ebproxy.getElectionSystemInfo().getMixer())
				Config.mixersPubKeys.put("mixer", CryptoFunc.getRSAPublicKeyFromCert(mCert.getValue()));
			
			//initialize talliers
			for(Certificate tCert : ebproxy.getElectionSystemInfo().getTallier())
				Config.mixersPubKeys.put("tallier", CryptoFunc.getRSAPublicKeyFromCert(tCert.getValue()));
			
			Config.mixerCount = ebproxy.getElectionSystemInfo().getMixer().size();
			
			Config.tallierCount = ebproxy.getElectionSystemInfo().getTallier().size();
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
//		initializePublicKeys();
		
		if(runners.isEmpty())
			LOGGER.log(Level.INFO, "There aren't runners. The verification will not run.");
		
		//run the runners  and get the results
		for(Runner r : runners){
			List<VerificationEvent> l = r.run();
			
			//check that a list isn't empty
			if(l != null){
				res.addAll(l);
			}
			else{
				LOGGER.log(Level.INFO, "The runner {0} does not contain any verification.", r.getRunnerName());
			}
		}
		
		return Collections.unmodifiableList(res);
	}
	
}
