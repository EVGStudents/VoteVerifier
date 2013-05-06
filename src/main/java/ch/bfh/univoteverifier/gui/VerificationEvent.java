/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.verification.VerificationResult;

/**
 *
 * Verification Event is a helper class that contains information that shall be
 * sent to the GUI this class is part of the observer pattern
 *
 *
 * @author snake
 */
public class VerificationEvent {

    private VerificationMessage vm;
    private String msg;
    private String eID;
    private final VerificationResult vr;

    /**
     * Create an instance of this helper class to send a message that is specific to an given election ID.
     * @param vm The type of message that this VerificationEvent contains
     * @param msg The message to be shown in the GUI or console
     * @param eID the election ID for which this message pertains.
     */
    public VerificationEvent(VerificationMessage vm, String msg, String eID) {
        this.vm = vm;
        this.msg = msg;
        this.eID = eID;
        this.vr = null;
    }
    
        /**
     * Create an instance of this helper class to send a message about a problem with the setup of the verification to be performed.
     * A message of this type will be displayed below the buttons for Universal or Individual verification.
     * @param vm The type of message that this VerificationEvent contains
     * @param msg The message to be shown in the GUI or console
     */
    public VerificationEvent(VerificationMessage vm, String msg) {
        this.vm = vm;
        this.msg = msg;
        this.vr = null;
    }

        /**
     * Create an instance of this helper class to send verification results to output
     * @param vr The helper class which contains the verification results.
     */
    public VerificationEvent(VerificationResult vr) {
        this.vr = vr;
        this.vm = VerificationMessage.RESULT;
    }

    /**
     * Get the message from this VerificationEvent.
     * @return String the message.
     */
    public String getMsg() {
        return msg;
    }

 /**
  * Get the VerificationMessage, which designates the type of message in this VerificationEvent.
  * @return The VerificationMessage Enum used to designate the type of message in an VerificationEvent. 
  */
    public VerificationMessage getVm() {
        return vm;
    }

    /**
     * Get the verification results.
     * @return VerificationResult : the helper class, which organizes the results from verification tests.
     */
    public VerificationResult getVr() {
        return vr;
    }
    
    

}
