/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 * 
* Project independent UniVoteVerifier.
 * 
*/
package ch.bfh.univoteverifier.verification;

/**
 * Verification Event is a helper class that contains information that shall be
 * sent to the GUI this class is part of the observer pattern
 *
 * @author prinstin
 */
public class VerificationEvent {

    private final VerificationEnum v;
    private SectionNameEnum section;
    private String msg;
    private boolean impl;
    private boolean result;
    
        /**
     * create a new verification event with an error message for the GUI
     * @param v verification type for this event
     * @param msg the message text that is being sent in this verification event
     */
    public VerificationEvent(VerificationEnum v, String msg) {
        this.v = v;
        this.msg = msg;
    }



    
    
    /**
     * Create a new verification results
     *
     * @param v Verification The type of verification
     * @param result boolean
     */
    public VerificationEvent(VerificationEnum v, boolean result) {
        this.v = v;
        this.result = result;
        this.impl = true;
    }

    /**
     * Create a new verification results
     *
     * @param v Verification The type of verification
     * @param result boolean
     * @þaram impl if the test implied in this result is implemented or not
     */
    public VerificationEvent(VerificationEnum v, boolean result, boolean impl) {
        this.v = v;
        this.result = result;
        this.impl = impl;
    }

    public SectionNameEnum getSection() {
        return section;
    }

    public void setSection(SectionNameEnum section) {
        this.section = section;
    }

    /**
     * Get the verification type for this VerificationResult
     * Identifies that type of message/information that this event contains
     *
     * @return the verification type
     */
    public VerificationEnum getVerificationEnum() {
        return v;
    }

    /**
     * Get the result for this VerificationResult
     *
     * @return
     */
    public boolean getResult() {
        return result;
    }

    /**
     * To know if the verification associated with this result is implemented
     *
     * @return
     */
    public boolean isImplemented() {
        return impl;
    }

    /**
     * get the String of the message this StatusEvent is delivering
     *
     * @return String the message to deliver to the GUI
     */
    public String getMessage() {
        return msg;
    }
}
