package ch.bfh.univoteverifier.verification;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import ch.bfh.univoteverifier.gui.StatusEvent;
import ch.bfh.univoteverifier.gui.StatusMessage;
import ch.bfh.univoteverifier.runner.Runner;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * This class is used to represent an UniversalVerification
 * @author snake and prinstin
 */
public class UniversalVerification extends AbstractVerification{
	
	private static final Logger LOGGER = Logger.getLogger(UniversalVerification.class.getName());
	
	/**
	 * Construct a new UniversalVerification with a given election ID
	 * @param eID String the ID of the election
	 */
	public UniversalVerification(String eID){
		super(eID);
		
		//TODO - CHeck that the electionID is correct and does not contain strange things.
		
	}
	
	//ToDO - Remove
	/**
	 *
	 */
	public void testObserverPattern(){
		StatusEvent se = new StatusEvent(StatusMessage.VRF_STATUS, "This is a message through the observer pattern");
		ss.notifyListeners(se);
	}

	@Override
	public void runVerification() {
		//run the runners and get results
		for(Runner r : runners){
			List<VerificationResult> res = r.run();
			StatusEvent se = new StatusEvent(StatusMessage.VRF_RESULT, res);
			ss.notifyListeners(se);
		}
	}

	@Override
	public boolean addRunner(Runner r) {
		r.seteID(super.geteID());
		r.setElectionBoardProxy(super.getEbproxy());
		
		return this.runners.add(r);
	}
	
}
