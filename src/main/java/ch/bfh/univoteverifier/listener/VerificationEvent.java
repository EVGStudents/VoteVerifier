/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.listener;

import ch.bfh.univote.common.Choice;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.Map;

/**
 *
 * Verification Event is a helper class that contains information that shall be
 * sent to the GUI this class is part of the observer pattern
 *
 *
 * @author Scalzi Giuseppe
 */
public class VerificationEvent {

    private VerificationMessage vm;
    private String msg, processID;
    private String eID;
    private final VerificationResult vr;
    private Boolean consoleSelected;
    private Map<Choice, Integer> electionResult;

    /**
     * Create an instance of this helper class to send a message that is
     * specific to an given election ID.
     *
     * @param vm The type of message that this VerificationEvent contains
     * @param Map<Choice, Integer> The election results for candidates.
     */
    public VerificationEvent(VerificationMessage vm, String eID, Map<Choice, Integer> electionResult, String processID) {
        this.vm = vm;
        this.eID = eID;
        this.electionResult = electionResult;
        this.vr = null;
        this.processID = processID;
    }

    /**
     * Create an instance of this helper class to send a message that is
     * specific to an given election ID.
     *
     * @param vm The type of message that this VerificationEvent contains
     */
    public VerificationEvent(VerificationMessage vm, String msg, String processID) {
        this.vm = vm;
        this.msg = msg;
        this.processID = processID;
        this.vr = null;
    }

    /**
     * Create an instance of this helper class to send a message that is
     * specific to an given election ID.
     *
     * @param vm The type of message that this VerificationEvent contains
     */
    public VerificationEvent(VerificationMessage vm, String msg) {
        this.vm = vm;
        this.processID = msg;
        this.msg = msg;
        this.vr = null;
    }

    /**
     * Create an instance of this helper class to send a message that tells the
     * GUI to toggle visibility of the console.
     *
     * @param vm The type of message that this VerificationEvent contains
     * @param Boolean true if the console should be shown.
     */
    public VerificationEvent(VerificationMessage vm, Boolean selected) {
        this.vm = vm;
        this.consoleSelected = selected;
        this.vr = null;
    }

    /**
     * Create an instance of this helper class to send verification results to
     * output
     *
     * @param vr The helper class which contains the verification results.
     * @param vm The type of message that this VerificationEvent contains
     */
    public VerificationEvent(VerificationResult vr, String processID) {
        this.vr = vr;
        this.processID = processID;
        this.vm = VerificationMessage.RESULT;
        this.eID = vr.getElectionID();
    }

    /**
     * Get the message from this VerificationEvent.
     *
     * @return String the message.
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Get the ID of the tab to which this Event pertains.
     *
     * @return String the message.
     */
    public String getProcessID() {
        return processID;
    }

    /**
     * Get the election results. This is the amount of votes that the candidates
     * have received.
     *
     * @return A map with choice and integer containing the names of the
     * candidates and the number of votes they have received.
     */
    public Map<Choice, Integer> getElectionResults() {
        return electionResult;
    }

    /**
     * Get the VerificationMessage, which designates the type of message in this
     * VerificationEvent.
     *
     * @return The VerificationMessage Enum used to designate the type of
     * message in an VerificationEvent.
     */
    public VerificationMessage getVm() {
        return vm;
    }

    /**
     * Get the verification results.
     *
     * @return VerificationResult : the helper class, which organizes the
     * results from verification tests.
     */
    public VerificationResult getVr() {
        return vr;
    }

    /**
     * Get the status of the console visiblity.
     *
     * @return True if the console should be shown.
     */
    public boolean getConsoleSelected() {
        return consoleSelected;
    }

    /**
     * Get the eID for which this message is intended.
     *
     * @return String : the election ID
     */
    public String getEID() {
        return eID;
    }
}
