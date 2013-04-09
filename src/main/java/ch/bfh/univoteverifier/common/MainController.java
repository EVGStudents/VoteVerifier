package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.verification.IndividualVerification;
import ch.bfh.univoteverifier.verification.UniversalVerification;
import ch.bfh.univoteverifier.gui.StatusSubject;

/**
 * This class is the controller who is responsible for the communication between
 * the GUI and the internal infrastructure
 * @author snake
 */
public class MainController {

    UniversalVerification uv;
    IndividualVerification iv;

    /**
     * Run an universal verification
     * @param eID String the ID of a election
     */
    public void universalVerification(String eID){
	    this.uv = new UniversalVerification(eID);
	    uv.runUniversal();
    }

    /**
     * Run an individual verification
     */
    public void individualVerification(){
	   this.iv = new IndividualVerification();
    }

    public StatusSubject getUniversalStatusSubject() {
        return uv.getStatusSubject();
    }

    public StatusSubject getIndividualStatusSubject() {
        return iv.getStatusSubject();
    }

    /**
     * ToDO -- Remove
     */ 
    public void testObserverPattern() {
        uv.testObserverPattern();
    }
}
