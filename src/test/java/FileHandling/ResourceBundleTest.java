/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FileHandling;

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
}