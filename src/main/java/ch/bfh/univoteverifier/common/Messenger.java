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

import ch.bfh.univote.common.Choice;
import ch.bfh.univoteverifier.listener.VerificationEvent;
import ch.bfh.univoteverifier.listener.VerificationListener;
import ch.bfh.univoteverifier.listener.VerificationMessage;
import ch.bfh.univoteverifier.listener.VerificationSubject;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GUIMessenger acts a the conduit for all information that is sent to the GUI
 * many classes have a reference to this class, but there will be only one
 * instance of it
 *
 * @author prinstin
 */
public class Messenger {

    private ResourceBundle rb;
    private VerificationSubject ss;
    private static final Logger LOGGER = Logger.getLogger(Messenger.class.toString());
    private String processID;

    public Messenger() {
        this("default");
    }

    /**
     * instantiate a GUIMessenger that is used to relay messages to the GUI
     */
    public Messenger(String processID) {
        this.processID = processID;
        ss = new ConcreteSubject();
        instantiateRB("en");
    }

    /**
     * send an error message to the GUI to be used in the case of an exception
     * encountered by the program which the GUI will most likely diplay in the
     * "console" or as a pop-up message
     *
     * @param str the message to send to the GUI to be displayed as an error in
     * the console
     */
    public void sendSetupError(String str) {
        VerificationEvent ve = new VerificationEvent(VerificationMessage.SETUP_ERROR, str);
        ss.notifyListeners(ve);
    }

    /**
     * Alert the GUI that a file has been selected.
     *
     * @param fileName The path to the file.
     */
    public void sendFileSelected(String fileName) {
        VerificationEvent ve = new VerificationEvent(VerificationMessage.FILE_SELECTED, fileName);
        ss.notifyListeners(ve);
    }

    /**
     * Alert the GUI that the visibility of the console should be changed.
     *
     * @param selected
     */
    public void sendShowConsole(Boolean selected) {
        VerificationEvent ve = new VerificationEvent(VerificationMessage.SHOW_CONSOLE, selected);
        ss.notifyListeners(ve);
    }

    /**
     * Send a fatal error message to the GUI to be used in the case of an
     * exception which prevents the program from continuing the desired course
     * of action. Text will be displayed the Errors and Exceptions text area in
     * a results panel.
     *
     * @param String the message to send
     */
    public void sendElectionSpecError(Exception ex) {
        LOGGER.log(Level.OFF, "EXCEPTION NAME: {0}", ex.toString());
        String exNameLong = ex.getClass().getName();
        LOGGER.log(Level.OFF, "EXCEPTION NAME: {0}", exNameLong);

        Pattern pattern = Pattern.compile("[^/.]+$");
        Matcher match = pattern.matcher(exNameLong);
        String exName;
        String message = "An error has occured.";
        if (match.find()) {
            exName = match.group();
            message = getMessageForKey(exName);
        }

        VerificationEvent ve = new VerificationEvent(VerificationMessage.ELECTION_SPECIFIC_ERROR, message, processID);
        ss.notifyListeners(ve);
    }

    /**
     * Send election results to the GUI
     *
     * @param Map<Choice,Integer> The election results.
     */
    public void sendElectionResults(String eID, Map<Choice, Integer> electionResults) {
        VerificationEvent ve = new VerificationEvent(VerificationMessage.ELECTION_RESULTS, eID, electionResults, processID);
        ss.notifyListeners(ve);
    }

    /**
     * send the results of a verification to the GUI the message is packaged
     * into a StatusEvent
     *
     * @param vr the helper class in which the results are packaged
     *
     */
    public void sendVrfMsg(VerificationResult vr) {
        LOGGER.log(Level.OFF, "SendVrfMessage, send with Messenger ID is : " + processID);
        VerificationEvent ve = new VerificationEvent(vr, processID);
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
     * Send a message that the verificaiton process has finished for a given
     * election.
     */
    public void sendVerificationFinished(String eID) {
        VerificationEvent ve = new VerificationEvent(VerificationMessage.VRF_FINISHED, processID);
        ss.notifyListeners(ve);
    }

    /**
     * Get the ID for this messenger. This is needed to distinguish an
     * individual verification from other verification tabs.
     *
     * @return the ID for this messenger.
     */
    public String getProcessID() {
        return processID;
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
            listeners.add(sl);
        }

        @Override
        public void removeListener(VerificationListener sl) {
            listeners.remove(sl);
        }

        @Override
        public void notifyListeners(VerificationEvent ve) {
            for (VerificationListener pl : listeners) {
                pl.updateStatus(ve);
            }
        }
    }
}
