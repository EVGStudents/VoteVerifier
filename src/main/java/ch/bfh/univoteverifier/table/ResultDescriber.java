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
package ch.bfh.univoteverifier.table;

import ch.bfh.univoteverifier.common.Config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prinstin
 */
public class ResultDescriber {

    /**
     * Turns the vrfCode into a text string that is shown in the GUI.
     *
     * @param code The int value which corresponds to a verification type.
     * @return The user-friendly text that describes a verification step.
     */
    public String getTextFromVrfCode(int code) {
        //String fileName = "src/main/resources/messages.properties";
        String fileName = "messages.properties";
        return getTextFromFile(fileName, code);
    }

    public String getTextFromFailureCode(int code) {
        //String fileName = "src/main/resources/failurecodes.properties";
        String fileName = "failurecodes.properties";
        return getTextFromFile(fileName, code);
    }

    /**
     * Turns the provided vrfCode into a text string that is shown as tooltip
     * text in the results table.
     *
     * @param code The int value which corresponds to a verification type.
     * @return The user-friendly text that describes a verification.
     */
    public String getTextFromFile(String fileName, int code) {
        String text = "";

        try {
            Properties prop;
            prop = new Properties();
			InputStream is = Config.class.getClassLoader().getResourceAsStream(fileName);
			prop.load(is);
            text = prop.getProperty(String.valueOf(code));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }

        return text;
    }
}
