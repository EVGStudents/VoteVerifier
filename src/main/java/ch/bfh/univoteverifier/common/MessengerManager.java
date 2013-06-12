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

import ch.bfh.univoteverifier.listener.VerificationListener;
import java.util.logging.Logger;

/**
 *
 * @author prinstin
 */
public class MessengerManager {

    private static final Logger LOGGER = Logger.getLogger(MessengerManager.class.toString());
    private Messenger defaultMessenger;
    VerificationListener vl;

    /**
     * Create a new instance of this class.
     */
    public MessengerManager(VerificationListener vl) {
        this.vl = vl;
        defaultMessenger = new Messenger("default");
        defaultMessenger.getStatusSubject().addListener(vl);
    }

    /**
     * Create new Messenger
     *
     * @param vt VerificationThread to register.
     * @return The ID of the Election Verification Process.
     */
    public Messenger getNewMessenger(String processID) {
        Messenger msgr = new Messenger(processID);
        msgr.getStatusSubject().addListener(vl);
        return msgr;
    }

    /**
     * Get the default messenger to send setup messages to the GUI.
     */
    public Messenger getDefaultMessenger() {
        return defaultMessenger;
    }
}
