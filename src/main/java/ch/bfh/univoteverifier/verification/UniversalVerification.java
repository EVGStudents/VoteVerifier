package ch.bfh.univoteverifier.verification;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import ch.bfh.univoteverifier.gui.StatusEvent;
import ch.bfh.univoteverifier.gui.StatusMessage;
import java.util.logging.Logger;


/**
 *
 * @author snake and prinstin
 */
public class UniversalVerification extends AbstractVerification{
	
	SystemSetupVerifier ssv = new SystemSetupVerifier();
	private static final Logger LOGGER = Logger.getLogger(UniversalVerification.class.getName());
	
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
