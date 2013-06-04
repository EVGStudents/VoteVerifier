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

import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.listener.VerificationEvent;
import ch.bfh.univoteverifier.listener.VerificationListener;
import ch.bfh.univoteverifier.listener.VerificationMessage;
import ch.bfh.univoteverifier.table.ResultDescriber;
import ch.bfh.univoteverifier.verification.VerificationResult;
import ch.bfh.univoteverifier.verification.VerificationThread;
import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs the program without in the terminal as the text only version in which
 * only one verification can run at a time.
 *
 * @author prinstin
 */
public class ConsoleRunner {

    private static final Logger LOGGER = Logger.getLogger(ConsoleRunner.class.getName());
    private final ResourceBundle rb;
    private Messenger msgr;

    /**
     * Create an instance of this class which checks the parameters passed and
     * starts the verification type the user entered.
     *
     * @param msgr Messenger to show messages in the console.
     * @param args the arguments to pass to the program.
     */
    public ConsoleRunner() {
        VerificationListener vl = new StatusUpdate();
        msgr = new Messenger("default");
        msgr.getStatusSubject().addListener(vl);
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
    }

    /**
     * Begin the program with the provided parameters.
     *
     * @param args The parameters passed to the program from the terminal.
     */
    public void begin(String[] args) {
        if (args[0].equals("-u")) {
            runUniVrfConsole(msgr, args[1]);
        } else if (args[0].equals("-i")) {
            runIndVrfConsole(msgr, args[1]);
        } else {
            LOGGER.log(Level.SEVERE, GUIconstants.USAGE);
            System.exit(0);
        }
    }

    /**
     * Show the verification information in the terminal. This method is called
     * if the program is being run from the terminal.
     *
     * @param ve VerificationResult helper class containing verification
     * information.
     */
    public void showResultInTerminal(VerificationEvent ve) {
        VerificationResult vr = ve.getVr();
        int code = vr.getVerificationType().getID();
        ResultDescriber rd = new ResultDescriber();
        String vrfType = rd.getTextFromVrfCode(code);
        String result = getResultName(vr);
        String eID = vr.getElectionID();
        String outputText = "\n" + eID + " : " + vrfType + " ............. " + result;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, outputText);
    }

    public String getResultName(VerificationResult vr) {
        String str = "";
        if (vr.isImplemented()) {
            if (vr.getResult()) {
                str = rb.getString("pass");
            } else {
                str = rb.getString("fail");
            }
        } else {
            str = rb.getString("noImpl");
        }
        return str;
    }

    public void runUniVrfConsole(Messenger msgr, String eID) {
        String msg = rb.getString("beginningVrfFor") + rb.getString("forElectionId") + eID;
        msgr.sendSetupError(msg);
        VerificationThread vt = new VerificationThread(msgr, eID);
        vt.start();
    }

    public void runIndVrfConsole(Messenger msgr, String path) {
        File file = new File(path);
        QRCode qr = new QRCode(msgr);
        ElectionReceipt er = qr.decodeReceipt(file);
        if (er == null) {
            System.exit(0);
        }
        String msg = rb.getString("beginningVrfFor") + rb.getString("ballotProvided");
        msgr.sendSetupError(msg);
        VerificationThread vt = new VerificationThread(msgr, er);
        vt.start();
    }

    /**
     * This inner class represents the implementation of the observer pattern
     * for the status messages for the console and verification parts of the
     * GUI.
     *
     * @author prinstin
     */
    class StatusUpdate implements VerificationListener {

        @Override
        public void updateStatus(VerificationEvent ve) {

            if (ve.getVm() == VerificationMessage.ELECTION_SPECIFIC_ERROR) {
                LOGGER.log(Level.SEVERE, ve.getMsg());
            } else if (ve.getVm() == VerificationMessage.SETUP_ERROR) {
                String text = "\n" + ve.getMsg();
                LOGGER.log(Level.SEVERE, text);
            } else {
                showResultInTerminal(ve);
            }
        }
    }
}
