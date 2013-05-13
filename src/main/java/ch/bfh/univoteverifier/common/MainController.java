/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.listener.VerificationSubject;
import ch.bfh.univoteverifier.verification.Verification;

/**
 * This class is the controller who is responsible for the communication between
 * the GUI and the internal infrastructure
 *
 * @author snake
 */
public class MainController {

    private Verification v;

    /**
     * Create an universal verification
     *
     * @param eID String the ID of a election
     */
    public void universalVerification(String eID) {
//		this.v = new UniversalVerification(eID);
    }

    /**
     * Run a verification
     */
    public void runVerifcation() {
        v.runVerification();
    }

    /**
     * Create an individual verification
     *
     * @param eID String the ID of a election
     */
    public void individualVerification(String eID) {
//		this.v = new IndividualVerification(eID);
    }

    /**
     * Get the status subject on which to register a listener.
     *
     * @return The inner class which implements VerificationSubject
     */
    public VerificationSubject getStatusSubject() {
        return v.getStatusSubject();
    }
}
