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

/**
 * Organizes the various threads that have been started which are responsible
 * for different elections being verified.
 *
 * @author prinstin
 */
public class ThreadManager {

    List<VerificationThread> threads;

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
        for (VerificationThread vtI : threads) {
            if (0 == vtI.getName().compareTo(eID)) {
                vtFound = vtI;
            }
        }
        if (vtFound != null) {
            threads.remove(vtFound);
            vtFound.interrupt();
        }
    }

    /**
     * A new verification process has begun and its thread should be registered
     * with this class.
     *
     * @param vt VerificationThread to register.
     */
    public void addThread(VerificationThread vt) {
        threads.add(vt);
    }
}
