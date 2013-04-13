/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.gui.StatusEvent;
import ch.bfh.univoteverifier.gui.StatusListener;
import ch.bfh.univoteverifier.gui.StatusSubject;
import ch.bfh.univoteverifier.runner.Runner;
import ch.bfh.univoteverifier.utils.ElectionBoardProxy;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prinstin
 */
public abstract class AbstractVerification {
	
	/**
	 *
	 */
	protected StatusSubject ss;
	private final String eID;
	/**
	 *
	 */
	protected List<Runner> runners;
	private final ElectionBoardProxy ebproxy;
	
	/**
	 *
	 * @param eID
	 */
	public AbstractVerification(String eID) {
		this.eID = eID;
		this.ebproxy = new ElectionBoardProxy(eID);
		ss = new ConcreteSubject();
		runners = new ArrayList<>();
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
	public abstract void runVerification();

	/**
	 * Add a runner to this verification
	 * @param r
	 * @return  
	 */
	public abstract boolean addRunner(Runner r);
}
