/**
*
*  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
*   Bern University of Applied Sciences, Engineering and Information Technology,
*   Research Institute for Security in the Information Society, E-Voting Group,
*   Biel, Switzerland.
*
*   Project independent UniVoteVerifier.
*
*/
package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.gui.VerificationListener;
import ch.bfh.univoteverifier.gui.VerificationMessage;
import ch.bfh.univoteverifier.gui.VerificationSubject;
import ch.bfh.univoteverifier.verification.SectionNameEnum;
import ch.bfh.univoteverifier.verification.VerificationEnum;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prinstin GUIMessenger acts a the conduit for all information that is
 * sent to the GUI many classes have a reference to this class, but there will
 * be only one instance of it
 */
public class GUIMessenger {

    ResourceBundle rb;
    VerificationSubject ss;
    SectionNameEnum activeSection = SectionNameEnum.UNSET;

    /**
     * instantiate a GUIMessenger that is used to relay messages to the GUI
     */
    public GUIMessenger() {
        Logger.getLogger(QRCode.class.getName()).log(Level.INFO, "GUIMessenger instantiated");
        ss = new ConcreteSubject();
        instantiateRB("en");
    }
    
    /*
     * send an error message to the GUI
     * to be used in the case of an exception encountered by the program which 
     * the GUI will most likely diplay in the "console" or as a pop-up message
     * @param str the message to send to the GUI to be displayed as an error in the console
     */
    public void sendErrorMsg(String str) {
        VerificationEvent ve;
        ve = new VerificationEvent(VerificationEnum.ERROR, str);
        ss.notifyListeners(ve);
    }

    /**
     * send the results of a verification to the GUI the message is packaged
     * into a StatusEvent
     *
     * @param vr the helper class in which the results are packaged
     */
    public void sendVrfMsg(VerificationEvent ve) {
        ve.setSection(activeSection);
        ss.notifyListeners(ve);
    }

    /**
     * creates a resource bundle with for the default locale of en when the
     * class in instantiated
     *
     * @param str
     */
    public void instantiateRB(String str) {
        changeLocale(str);
    }

    /**
     * used for internationalization when the user changes the language
     *
     * @param str a string representation of the language selected
     */
    public void changeLocale(String str) {
        Locale loc = new Locale(str);
        rb = ResourceBundle.getBundle("error", loc);
    }

    /**
     * signal that a new section for the verification has been started sets the
     * value of a SectionNamEnum that is packed into vrfResult messages to the
     * GUI method is called by the classes involved in the verification process
     *
     * @param s SectionNameEnum which describes a verification category
     */
    public void changeSection(SectionNameEnum s) {
        this.activeSection = s;
    }

    /**
     * returns the appropriate, language-specific message for a given key value
     *
     * @param key the key value for which to get a message from the resource
     * bundle
     * @return the strings or the language-specific message
     */
    public String getMessageForKey(String key) {
        String resultStr = rb.getString(key);
        return resultStr;
    }

    /**
     * get the status subject for this class
     *
     * @return the status subject for this object which is part of the observer
     * pattern
     */
    public VerificationSubject getStatusSubject() {
        return this.ss;
    }

    /**
     * a subject that is used in an observer pattern with the GUI information
     * used to display messages in the status console of the GUI as well as
     * relay information regarding the status of verification process
     */
    private class ConcreteSubject implements VerificationSubject {

        public ConcreteSubject() {
        }
        ArrayList<VerificationListener> listeners = new ArrayList();

        @Override
        public void addListener(VerificationListener sl) {
            Logger.getLogger(QRCode.class.getName()).log(Level.INFO, "listener registered {0}", sl.getClass().getEnclosingClass());
            listeners.add(sl);
        }

        @Override
        public void removeListener(VerificationListener sl) {
            listeners.remove(sl);
        }

        @Override
        public void notifyListeners(VerificationEvent ve) {
            Logger.getLogger(QRCode.class.getName()).log(Level.INFO, "notifyListeners called");

            for (VerificationListener pl : listeners) {
                Logger.getLogger(QRCode.class.getName()).log(Level.INFO, "listener notified: {0}", pl.getClass().getEnclosingClass());
                pl.updateStatus(ve);
            }
        }
    }
}
