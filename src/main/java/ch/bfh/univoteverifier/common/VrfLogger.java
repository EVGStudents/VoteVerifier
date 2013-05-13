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

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Creates a logger that can use a custom formatter in order to present
 * verification output in a desirable format in the console.
 *
 * @author prinstin
 */
public class VrfLogger {

    static private FileHandler fileTxt;
    static private LogFormatter formatter;

    /**
     * Setup the logger with the custom formatter.
     *
     * @throws IOException
     */
    static public void setup() throws IOException {

        // Get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler("Logging.txt");

        formatter = new LogFormatter();
        fileTxt.setFormatter(formatter);
        logger.addHandler(fileTxt);
    }
}
