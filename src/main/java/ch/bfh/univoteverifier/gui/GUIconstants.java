/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.common.Config;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create a static utility class which contains widely used variables.
 *
 * @author prinstin
 */
public class GUIconstants {

    public final static Color GREY = new Color(190, 190, 190);
    public final static Color DARK_GREY = new Color(140, 140, 140);
    public final static Color BLUE = new Color(110, 110, 254);
    public static Locale loc = new Locale("EN");
    public final static String ABOUT_TEXT = "   Project independent UniVoteVerifier."
            + "\nCopyright (c) 2013 Berner Fachhochschule, Switzerland."
            + "\nBern University of Applied Sciences, Engineering and Information Technology,"
            + " \nResearch Institute for Security in the Information Society, E-Voting Group,"
            + " \nBiel, Switzerland.";

    /**
     * Set the locale being used for the program to a new value.
     *
     * @param str the String representation of the newly desired locale. I.e.
     * "EN" or "FR".
     */
    public static void setLocale(String str) {
        loc = new Locale(str);
    }

    /**
     * Get the locale that is currently being used for the program.
     *
     * @return Locale the locale that the program is currently set to use.
     */
    public static Locale getLocale() {
        return loc;
    }
    
     /**
     * Turns the vrfCode into a text string that is shown in the GUI.
     *
     * @param code The int value which corresponds to a verification type.
     * @return The user-friendly text that describes a verification step.
     */
    public static String getTextFromVrfCode(int code) {
        String text="";
        try {
            Properties prop;
            prop = new Properties();
            prop.load(new FileInputStream("src/main/java/ch/bfh/univoteverifier/resources/messages.properties"));
            text = (String) prop.getProperty(String.valueOf(code));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        return text;
    }
}
