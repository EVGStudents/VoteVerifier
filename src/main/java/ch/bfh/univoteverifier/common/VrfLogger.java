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

    public static Logger getLoggerForClass(String className) {
        Logger logger = Logger.getLogger(className);

        LogFormatter formatter;
        formatter = new LogFormatter();
        VrfConsoleHandler consoleHandler = new VrfConsoleHandler();

        consoleHandler.setFormatter(formatter);
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        return logger;
    }
}
