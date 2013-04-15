/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.gui.StatusEvent;
import ch.bfh.univoteverifier.gui.StatusListener;
import ch.bfh.univoteverifier.gui.StatusSubject;
import ch.bfh.univoteverifier.runner.Runner;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prinstin
 */
public class Verification {
	
	
	private static final Logger LOGGER = Logger.getLogger(VerificationEnum.class.getName());
	private final StatusSubject ss;
	private final String eID;
	private final VerificationEnum verificationType;
	private final List<Runner> runners;
	private final ElectionBoardProxy ebproxy;
	private final PrimitivesVerifier prmVrf;
	
	/**
	 * Construct a new abstract verification with a given election ID
	 * @param eID the ID of an election
	 */
	public Verification(String eID, VerificationEnum verificationType) {
		this.eID = eID;
		this.ebproxy = new ElectionBoardProxy(eID);
		ss = new ConcreteSubject();
		runners = new ArrayList<>();
		prmVrf = new PrimitivesVerifier();
		this.verificationType = verificationType;
		

	}
	
	/**
	 *
	 * @return
	 */
	public StatusSubject getStatusSubject() {
		return this.ss;
	}
	
	/**
	 * @return the ebproxy
	 */
	public ElectionBoardProxy getEbproxy() {
		return ebproxy;
	}
	
	/**
	 * @return the eID
	 */
	public String geteID() {
		return eID;
	}
	
	/**
	 * @return the Verification type
	 */
	public VerificationEnum getVerificationType(){
		return verificationType;
	}
	
	/**
	 * a subject that is used in an observer pattern with the GUI information
	 * used to display messages in the status console of the GUI as well as
	 * relay information regarding the status of verification process
	 */
	private class ConcreteSubject implements StatusSubject {
		
		public ConcreteSubject() {
		}
		ArrayList<StatusListener> listeners = new ArrayList();
		
		@Override
		public void addListener(StatusListener sl) {
			listeners.add(sl);
		}
		
		@Override
		public void removeListener(StatusListener sl) {
			listeners.remove(sl);
		}
		
		@Override
		public void notifyListeners(StatusEvent se) {
			
			for (StatusListener pl : listeners) {
				pl.updateStatus(se);
			}
		}
	}
	
	/**
	 * Run a verification
	 */
	public void runVerification(){
		LOGGER.log(Level.INFO, "Running verificaton.....");
		
		if(runners.isEmpty())
			LOGGER.log(Level.INFO, "There aren't runners. The verification will not run.");
		
		//run the runners and get results
		for(Runner r : runners){
			r.run();
		}
	}
	
	/**
	 * Add a runner to this verification
	 * @param r a runner
	 * @return true if the runner has been successfully added to the list
	 */
	public boolean addRunner(Runner r){
		r.setElectionBoardProxy(ebproxy);
		r.setPrimitivesVerifier(prmVrf);
		
		return this.runners.add(r);
	}
}
