package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.gui.StatusSubject;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.verification.VerificationEnum;

/**
 * This class is the controller who is responsible for the communication between
 * the GUI and the internal infrastructure
 * @author snake
 */
public class MainController {
	
	private Verification v;

	/**
	 * Run an universal verification
	 * @param eID String the ID of a election
	 */
	public void universalVerification(String eID){
		this.v = new Verification(eID, VerificationEnum.UNIVERSAL);
		
		//add some runners
		v.addRunner(new SystemSetupRunner());
		
		
		v.runVerification();
	}
	
	
	/**
	 * Run an individual verification
	 * @param eID
	 */
	public void individualVerification(String eID){
		this.v = new Verification(eID, VerificationEnum.INDIVIDUAL);
	}
	
	/**
	 *
	 * @return
	 */
	public StatusSubject getUniversalStatusSubject() {
		return v.getStatusSubject();
	}
	
	/**
	 *
	 * @return
	 */
	public StatusSubject getIndividualStatusSubject() {
		return v.getStatusSubject();
	}
	
}
