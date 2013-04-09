/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.gui.StatusEvent;
import ch.bfh.univoteverifier.gui.StatusListener;
import ch.bfh.univoteverifier.gui.StatusSubject;
import java.util.ArrayList;

/**
 *
 * @author prinstin
 */
public abstract class AbstractVerification {

    StatusSubject ss;

    public AbstractVerification() {
        ss = new ConcreteSubject();
    }

    public StatusSubject getStatusSubject() {
        return this.ss;
    }

    /**
     * a subject that is used in an observer pattern with the GUI information
     * used to display messages in the status console of the GUI as well as
     * relay information regarding the status of verification process
     */
    private class ConcreteSubject implements StatusSubject {

        public ConcreteSubject() {
        }
        ArrayList<StatusListener> listeners = new ArrayList();

        @Override
        public void addListener(StatusListener sl) {
            listeners.add(sl);
        }

        @Override
        public void removeListener(StatusListener sl) {
            listeners.remove(sl);
        }

        @Override
        public void notifyListeners(StatusEvent se) {

            for (StatusListener pl : listeners) {
                pl.updateStatus(se);
            }
        }
    }
}
