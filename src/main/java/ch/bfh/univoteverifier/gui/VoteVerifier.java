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

import ch.bfh.univoteverifier.common.ConsoleRunner;
import ch.bfh.univoteverifier.common.LogFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class simply provides a point of departure when the program is started.
 *
 * @author prinstin
 */
public class VoteVerifier {

    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());
    LogFormatter logf = new LogFormatter();
    ResourceBundle rb;

    /**
     * @param args To tell the command line verifier what to do and where to get
     * data. Usage: uvv [OPTION] [FILE] uvv -i /home/username/qrcode \t# Verify
     * a single election receipt from a QR code. uvv -u vsbfh-2013 \t\t# Verify
     * the results from the election with ID vsbfh-2013
     *
     * Commands:\n"
     *
     * -i, --individual [file] \tVerify an election receipt. -u, --universal
     * \"election id\"\tVerify an an entire election.
     */
    public static void main(String[] args) {
        VoteVerifier runner = new VoteVerifier(args);
    }

    /**
     * Begin a new verification.
     *
     * @param args
     */
    public VoteVerifier(String[] args) {
        if (args.length > 0) {
            ConsoleRunner cr = new ConsoleRunner();
            cr.begin(args);

        } else {
            MainGUI gui = new MainGUI();
        }
    }
}
