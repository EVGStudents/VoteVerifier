/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.common.ConsoleRunner;
import ch.bfh.univoteverifier.common.LogFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prinstin
 */
public class UniVoteVerifierRunner {

    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());
    LogFormatter logf = new LogFormatter();
    ResourceBundle rb;

    /**
     * @param args
     */
    public static void main(String[] args) {
        UniVoteVerifierRunner runner = new UniVoteVerifierRunner(args);
    }

    public UniVoteVerifierRunner(String[] args) {
        if (args.length > 0) {
            ConsoleRunner cr = new ConsoleRunner();
            cr.begin(args);

        } else {
            MainGUI gui = new MainGUI();
        }
    }
}
