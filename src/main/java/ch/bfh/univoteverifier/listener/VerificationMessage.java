/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.listener;

/**
 *
 * @author prinstin
 *
 * Enumerates the types of messages that the GUI receives
 */
public enum VerificationMessage {

    /**
     * the type of StatusMessages possible. Used by the GUI to identify incoming
     * messages and take the appropriate action
     */
    RESULT(10), //  for hte verification results
    SETUP_ERROR(20),
    ELECTION_SPECIFIC_ERROR(30),
    VRF_FINISHED(40),
    FILE_SELECTED(50),
    SHOW_CONSOLE(60);
//The code of the message
    private int code;

    /**
     * Construct a Message
     *
     * @param c the code for the message
     */
    private VerificationMessage(int c) {
        code = c;
    }

    /**
     * Get the code of the message
     *
     * @return an integer with the code of this message
     */
    public int getInt() {
        return code;
    }
}
