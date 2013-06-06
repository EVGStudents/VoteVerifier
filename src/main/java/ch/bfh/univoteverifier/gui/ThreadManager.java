/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.verification.VerificationThread;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private Map<String, VerificationThread> threadMap;
    private static final Logger LOGGER = Logger.getLogger(ThreadManager.class.toString());

    /**
     * Create a new instance of this class.
     */
    public ThreadManager() {
        threads = new ArrayList<>();
        threadMap = new HashMap<>();
    }

    public boolean hasThreadWithProcessID(String processID) {
        return threadMap.containsKey(processID);
    }

//    /**
//     * End a thread, for example if a tab is closed, the verification process is
//     * no longer needed.
//     *
//     * @param eID The election ID for which to end a thread.
//     */
//    public void killThread(String processID) {
//        VerificationThread vtFound = null;
//        LOGGER.log(Level.INFO, "Looking for thread with process ID {0}", processID);
//        for (VerificationThread vtI : threads) {
//            LOGGER.log(Level.INFO, "Iterated process ID {0}", vtI.getProcessID());
//            if (vtI.getProcessID().equals(processID)) {
//                vtFound = vtI;
//            }
//        }
//        if (vtFound != null) {
//            threads.remove(vtFound);
//            vtFound.interrupt();
//            LOGGER.log(Level.INFO, "INTERRUPTED THREAD {0}", vtFound.getProcessID());
//        }
//    }
    public void killThread(String processID) {
        if (threadMap.containsKey(processID)) {
            VerificationThread vt = threadMap.remove(processID);
            vt.interrupt();
        } else {
            LOGGER.log(Level.SEVERE, "No thread found to kill with the processID: {0}", processID);
        }
    }

    public void addThread(VerificationThread vt) {
        LOGGER.log(Level.INFO, "THREAD REGISTERED!  Process ID: " + vt.getProcessID());
        String processID = vt.getProcessID();
        threadMap.put(processID, vt);
    }

    public void killAllThreads() {
        Set keys = threadMap.keySet();
        String[] keyList = new String[keys.size()];
        keys.toArray(keyList);

        for (int i = 0; i < keyList.length; i++) {
            VerificationThread vt = threadMap.get(keyList[i]);
            LOGGER.log(Level.INFO, "INTERRUPTED THREAD {0}", vt.getProcessID());
            vt.interrupt();
        }

    }
    /**
     * A new verification process has begun and its thread should be registered
     * with this class.
     *
     * @param vt VerificationThread to register.
     */
//public void addThread(VerificationThread vt) {
//        LOGGER.log(Level.INFO, "THREAD REGISTERED!  Process ID: " + vt.getProcessID());
//        threads.add(vt);
//    }
    /**
     * Kill all the threads running. Used when the language is changed.
     */
//public void killAllThreads() {
//        if (threads.isEmpty()) {
//            LOGGER.log(Level.INFO, "NO THREADS FOUND");
//        }
//        for (VerificationThread vtI : threads) {
//            LOGGER.log(Level.INFO, "INTERRUPTED THREAD {0}", vtI.getProcessID());
//            vtI.interrupt();
//        }
//        threads = new ArrayList<>();
//    }
}
