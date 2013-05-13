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
 *
 * @author prinstin
 */
public class ThreadManager {

    List<VerificationThread> threads;

    public ThreadManager() {
        threads = new ArrayList<>();
    }

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

    public void addThread(VerificationThread vt) {
        threads.add(vt);
    }
}
