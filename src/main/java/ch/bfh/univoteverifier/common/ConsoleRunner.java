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
import ch.bfh.univoteverifier.gui.MainGUI;
import ch.bfh.univoteverifier.verification.VerificationThread;
import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs the program without GUI components
 * @author prinstin
 */
public class ConsoleRunner {
   
    private static final Logger LOGGER = Logger.getLogger(ConsoleRunner.class.getName());
    private final ResourceBundle rb= ResourceBundle.getBundle("error", GUIconstants.getLocale());
    
    /**
     * Create an instance of this class which checks the parameters passed and starts the verification type the user entered.
     * @param msgr Messenger to show messages in the console.
     * @param args the arguments to pass to the program.
     */
    public ConsoleRunner(Messenger msgr, String[] args) {
        if (0 == args[0].compareTo("-u")) {
            runUniVrfConsole(msgr, args[1]);
        } else if (0 == args[0].compareTo("-i")) {
            runIndVrfConsole(msgr, args[1]);
        } else {
            LOGGER.log(Level.SEVERE, GUIconstants.USAGE);
            System.exit(0);
        }
    }
    
    
    public void runUniVrfConsole(Messenger msgr, String eID){
            String msg = rb.getString("beginningVrfFor") + rb.getString("forElectionId") + eID;
            msgr.sendErrorMsg(msg);
            VerificationThread vt = new VerificationThread(msgr, eID);
            vt.start();
    }
    
    public void runIndVrfConsole(Messenger msgr, String path){
            File file = new File(path);
            QRCode qr= new QRCode(msgr);
            ElectionReceipt er =qr.decodeReceipt(file);
            if (er==null)
                System.exit(0);
            String msg = rb.getString("beginningVrfFor") + rb.getString("ballotProvided");
            msgr.sendErrorMsg(msg);
            VerificationThread vt = new VerificationThread(msgr, er);
            vt.start();
    }
    
}
