package ch.bfh.univoteverifier.verification;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import ch.bfh.univoteverifier.gui.StatusEvent;
import ch.bfh.univoteverifier.gui.StatusMessage;
import java.util.logging.Logger;


/**
 * This class is used to represent an UniversalVerification
 * @author snake and prinstin
 */
public class UniversalVerification extends AbstractVerification{
	
	private SystemSetupRunner ssv = new SystemSetupRunner();
	private final String eID; 
	private static final Logger LOGGER = Logger.getLogger(UniversalVerification.class.getName());

	/**
	 * Construct a new UniversalVerification with a given election ID
	 * @param eID String the ID of the election
	 */
	public UniversalVerification(String eID){
		this.eID = eID;	
	}
	
	//ToDO - Remove
	public void testObserverPattern(){
		StatusEvent se = new StatusEvent(StatusMessage.VRF_STATUS, "This is a message through the observer pattern");
		ss.notifyListeners(se);
	}
	
	public void runUniversal(){
		startSystemSetup();
	}
	
	private void startSystemSetup(){
		StatusEvent se = new StatusEvent(StatusMessage.VRF_RESULT, ssv.vrfSignParam());
		ss.notifyListeners(se);
	}
}
