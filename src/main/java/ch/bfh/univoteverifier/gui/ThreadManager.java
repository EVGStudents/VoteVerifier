/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.verification.VerificationThread;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Organizes the various threads that have been started which are responsible
 * for different elections being verified.
 *
 * @author Justin Springer
 */
public class ThreadManager {

    private Map<String, VerificationThread> threadMap;
    private static final Logger LOGGER = Logger.getLogger(ThreadManager.class.toString());

    /**
     * Create a new instance of this class.
     */
    public ThreadManager() {
        threadMap = new HashMap<String, VerificationThread>();
    }

    /**
     * Check if a thread with a given processID exists.
     *
     * @param processID The processID for which to check if a thread exists.
     * @return true if there is a thread with the processID in the map.
     */
    public boolean hasThreadWithProcessID(String processID) {
        return threadMap.containsKey(processID);
    }

    /**
     * End a thread, for example if a tab is closed, the verification process is
     * no longer needed.
     *
     * @param eID The election ID for which to end a thread.
     */
    public void killThread(String processID) {
        if (threadMap.containsKey(processID)) {
            VerificationThread vt = threadMap.remove(processID);
            vt.interrupt();
        } else {
            LOGGER.log(Level.SEVERE, "No thread found to kill with the processID: {0}", processID);
        }
    }

    /**
     * A new verification process has begun and its thread should be registered
     * with this class.
     *
     * @param vt VerificationThread to register.
     */
    public void addThread(VerificationThread vt) {
        String processID = vt.getProcessID();
        threadMap.put(processID, vt);
    }

    /**
     * Kill all the threads running. Used when the language is changed.
     */
    public void killAllThreads() {
        // Eric Dubuis: Simplified method.
        for (VerificationThread vt : threadMap.values()) {
            vt.interrupt();
        }
    }
}
