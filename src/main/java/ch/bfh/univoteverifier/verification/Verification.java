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
import ch.bfh.univoteverifier.common.GUIMessenger;
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
	private final StatusSubject ss;
	private final String eID;
	protected final List<Runner> runners;
	protected final ElectionBoardProxy ebproxy;
	protected VerificationEnum displayType = VerificationEnum.ORDER_BY_SPEC;
	protected GUIMessenger gm;

	//used to store the results of a verification
	protected List<VerificationResult> res;
	
	/**
	 * Construct a new abstract verification with a given election ID
	 * @param eID the ID of an election
	 */
	public Verification(String eID, GUIMessenger gm) {
		this.eID = eID;
		this.ebproxy = new ElectionBoardProxy(eID);
		ss = new ConcreteSubject();
		runners = new ArrayList<>();
		res = new ArrayList<>();
		this.gm = gm;

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
	 *
	 * @return
	 */
	public StatusSubject getStatusSubject() {
		return this.ss;
	}
	
	/**
	 * @return the eID
	 */
	public String geteID() {
		return eID;
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
	public List<VerificationResult> runVerification(){
		if(runners.isEmpty())
			LOGGER.log(Level.INFO, "There aren't runners. The verification will not run.");
		
		//run the runners  and get the results
		for(Runner r : runners){
			List<VerificationResult> l = r.run();

			//check that a list isn't empty
			if(l != null){
				res.addAll(l);
			}
			else{
				LOGGER.log(Level.INFO, "The runner " + r.getRunnerName() + "does not contain and verification.");
			}
		}

		return Collections.unmodifiableList(res);
	}

}
