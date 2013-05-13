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

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This class allows custom formatter settings to be implemented.
 *
 * @author prinstin
 */
public final class LogFormatter extends Formatter {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /*
     * Formats the record.  Only the message is sent out.
     */
    @Override
    public String format(LogRecord record) {
        return record.getMessage();
    }
}
