package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.verification.IndividualVerification;
import ch.bfh.univoteverifier.verification.UniversalVerification;
import ch.bfh.univoteverifier.gui.StatusSubject;
import ch.bfh.univoteverifier.runner.SystemSetupRunner;
import ch.bfh.univoteverifier.verification.AbstractVerification;

/**
 * This class is the controller who is responsible for the communication between
 * the GUI and the internal infrastructure
 * @author snake
 */
public class MainController {

   private AbstractVerification av;
    
    /**
     * Run an universal verification
     * @param eID String the ID of a election
     */
    public void universalVerification(String eID){
	    this.av = new UniversalVerification(eID);

	    //add some runners
	    av.addRunner(new SystemSetupRunner());


	    av.runVerification();
    }


    /**
     * Run an individual verification
     * @param eID 
     */
    public void individualVerification(String eID){
	   this.av = new IndividualVerification(eID);
    }

    	/**
	 *
	 * @return
	 */
	public StatusSubject getUniversalStatusSubject() {
        return av.getStatusSubject();
    }

    	/**
	 *
	 * @return
	 */
	public StatusSubject getIndividualStatusSubject() {
        return av.getStatusSubject();
    }

}
