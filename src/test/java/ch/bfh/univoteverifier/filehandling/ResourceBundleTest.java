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
package ch.bfh.univoteverifier.filehandling;

import ch.bfh.univoteverifier.common.GUIMessenger;
import java.util.Locale;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prinstin
 */
public class ResourceBundleTest {

    public ResourceBundleTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Tests that the resource bundle is found and returns the value of a given
     * key
     */
    @Test
    public void getCorrectKeyValue() {
        Locale loc = new Locale("en");
        ResourceBundle rb = ResourceBundle.getBundle("error", loc);
        String resultStr = rb.getString("filenotfound");
//      Logger.getLogger(QRCodeTest.class.getName()).log(Level.INFO, "OUTPUT: {0} ", resultStr);
        assertTrue(0 == "The file was not found.".compareTo(resultStr));
    }
    
        /**
     * Tests that class GUI Messenger returns appropriate value for a given key
     */
    @Test
    public void GUIMessengerGetsKeyValue() {
        GUIMessenger msrg = new GUIMessenger();
        String resultStr = msrg.getMessageForKey("filenotfound");
        assertTrue(0 == "The file was not found.".compareTo(resultStr));
    }
    
           /**
     * Tests that class GUI Messenger returns appropriate value for a given key
     * even if the locale is changed
     */
    @Test
    public void GUIMessengerGetsKeyValueIfLocaleChanged() {
        GUIMessenger msrg = new GUIMessenger();
        msrg.changeLocale("de");
        String resultStr = msrg.getMessageForKey("filenotfound");
        assertTrue(0 == "Der Datei wurde nicht gefunden.".compareTo(resultStr));
    }
}