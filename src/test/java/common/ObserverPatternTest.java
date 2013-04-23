/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import fileHandling.QRCodeTest;
import ch.bfh.univoteverifier.common.GUIMessenger;
import ch.bfh.univoteverifier.common.MainController;
import ch.bfh.univoteverifier.gui.StatusEvent;
import ch.bfh.univoteverifier.gui.StatusListener;
import static ch.bfh.univoteverifier.gui.StatusMessage.VRF_RESULT;
import static ch.bfh.univoteverifier.gui.StatusMessage.VRF_STATUS;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ObserverPatternTest {
    
    public ObserverPatternTest() {
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
    
    @Test
    public void messageIsReceived(){
        StatusListener sl = new StatusUpdate();
        MainController mc = new MainController();
        mc.getStatusSubject().addListener(sl);
        GUIMessenger msgr = new GUIMessenger();
        msgr.sendErrorMsg("12345");
        
    }
    
    class StatusUpdate implements StatusListener {

        @Override
        public void updateStatus(StatusEvent se) {

             Logger.getLogger(QRCodeTest.class.getName()).log(Level.SEVERE, "MSG RECEIVED: {0}", se.getMessage());
    
        }
       
    }
}