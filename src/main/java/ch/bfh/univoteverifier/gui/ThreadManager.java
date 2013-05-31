/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.verification.VerificationThread;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Organizes the various threads that have been started which are responsible
 * for different elections being verified.
 *
 * @author prinstin
 */
public class ThreadManager {

    private List<VerificationThread> threads;
    private static final Logger LOGGER = Logger.getLogger(ThreadManager.class.toString());

    /**
     * Create a new instance of this class.
     */
    public ThreadManager() {
        threads = new ArrayList<>();
    }

    /**
     * End a thread, for example if a tab is closed, the verification process is
     * no longer needed.
     *
     * @param eID The election ID for which to end a thread.
     */
    public void killThread(String eID) {
        VerificationThread vtFound = null;
        LOGGER.log(Level.OFF, "Looking for thread with name {0}", eID);
        for (VerificationThread vtI : threads) {
            LOGGER.log(Level.OFF, "Iterated name {0}", vtI.getName());
            if (0 == vtI.getName().compareTo(eID)) {
                vtFound = vtI;
            }
        }
        if (vtFound != null) {
            threads.remove(vtFound);
            vtFound.interrupt();
            LOGGER.log(Level.OFF, "INTERRUPTED THREAD {0}", vtFound.getName());
        }
    }

    /**
     * A new verification process has begun and its thread should be registered
     * with this class.
     *
     * @param vt VerificationThread to register.
     */
    public void addThread(VerificationThread vt) {
        LOGGER.log(Level.OFF, "THREAD REGISTERED!  Name: " + vt.getName());
        threads.add(vt);
    }

    /**
     * Kill all the threads running. Used when the language is changed.
     */
    public void killAllThreads() {
        if (threads.isEmpty()) {
            LOGGER.log(Level.OFF, "NO THREADS FOUND");
            LOGGER.log(Level.OFF, "NO THREADS FOUND");

        }
        for (VerificationThread vtI : threads) {
            LOGGER.log(Level.OFF, "INTERRUPTED THREAD {0}", vtI.getName());
            vtI.interrupt();
        }
        threads = new ArrayList<>();
    }
}
