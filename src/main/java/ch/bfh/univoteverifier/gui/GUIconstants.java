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

import java.awt.Color;
import java.util.Locale;

/**
 * Create a static utility class which contains widely used variables.
 *
 * @author prinstin
 */
public class GUIconstants {

    public final static Color GREY = new Color(190, 190, 190);
    public final static Color DARK_GREY = new Color(160, 160, 160);
    public final static Color BLUE = new Color(110, 110, 254);
    public static Locale loc = new Locale("EN");
    public final static String ABOUT_TEXT = "UniVerifier\nProject independent verifier for UniVote."
            + "\nCopyright (c) 2013 Berner Fachhochschule, Switzerland."
            + "\nBern University of Applied Sciences, Engineering and Information Technology,"
            + " \nResearch Institute for Security in the Information Society, E-Voting Group,"
            + " \nBiel, Switzerland.";
    public final static String USAGE = ABOUT_TEXT
            + "\n\nUsage: uvv [OPTION] [FILE]\n"
            + "uvv verifies the results from an election held with the UniVote Wahlsystem.\n"
            + "\n"
            + "Examples:\n"
            + "  uvv -i /home/username/qrcode \t# Verify a single election receipt from a QR code.\n"
            + "  uvv -u vsbfh-2013   \t\t# Verify the results from the election with ID vsbfh-2013\n"
            + "\n"
            + "Commands:\n"
            + "\n"
            + "  -i, --individual [file]   \tVerify an election receipt.\n"
            + "  -u, --universal \"election id\"\tVerify an an entire election.\n";

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
}
