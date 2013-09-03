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
package ch.bfh.univoteverifier.filehandling;

import ch.bfh.univoteverifier.common.Messenger;
import java.util.Locale;
import java.util.ResourceBundle;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author Justin Springer
 */
public class ResourceBundleTest {

    /**
     * Tests that the resource bundle is found and returns the value of a given
     * key
     */
    @Test
    public void getCorrectKeyValue() {
        Locale loc = new Locale("en");
        ResourceBundle rb = ResourceBundle.getBundle("error", loc);
        String resultStr = rb.getString("filenotfound");
        assertNotNull(resultStr);
        assertFalse(resultStr.isEmpty());
    }

    /**
     * Tests that class GUI Messenger returns appropriate value for a given key
     */
    @Test
    public void GUIMessengerGetsKeyValue() {
        Messenger msrg = new Messenger();
        String resultStr = msrg.getMessageForKey("filenotfound");
        assertNotNull(resultStr);
        assertFalse(resultStr.isEmpty());
    }

    /**
     * Tests that class GUI Messenger returns appropriate value for a given key
     * even if the locale is changed to DE
     */
    @Test
    public void GUIMessengerGetsKeyValueIfLocaleChangedToDe() {
        Messenger msrg = new Messenger();
        msrg.changeLocale("de");
        String resultStr = msrg.getMessageForKey("filenotfound");
        assertNotNull(resultStr);
        assertFalse(resultStr.isEmpty());
    }

    /**
     * Tests that class GUI Messenger returns appropriate value for a given key
     * even if the locale is changed tor FR
     */
    @Test
    public void GUIMessengerGetsKeyValueIfLocaleChangedToFr() {
        Messenger msrg = new Messenger();
        msrg.changeLocale("fr");
        String resultStr = msrg.getMessageForKey("filenotfound");
        assertNotNull(resultStr);
        assertFalse(resultStr.isEmpty());
    }

    /**
     * Tests that class GUI Messenger returns appropriate value for a given key
     * even if the locale is changed tor EN
     */
    @Test
    public void GUIMessengerGetsKeyValueIfLocaleChangedToEn() {
        Messenger msrg = new Messenger();
        msrg.changeLocale("en");
        String resultStr = msrg.getMessageForKey("filenotfound");
        assertNotNull(resultStr);
        assertFalse(resultStr.isEmpty());
    }
}