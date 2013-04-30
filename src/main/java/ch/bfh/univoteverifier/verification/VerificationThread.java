/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.common.Messenger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prinstin
 */
public class VerificationThread extends Thread {

    String eID="";
    Verification v;
    Messenger msgr;
    
    public VerificationThread(Messenger msgr, String eID){
        this.eID = eID;
        this.v = new UniversalVerification(msgr, eID);
        this.msgr = msgr;
        
    }
    
    @Override
    public void run() {
        msgr.sendErrorMsg("BEFORE RUN VERIFICATION");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "BEFORE RUN VERIFICATION");
        v.runVerification();
        msgr.sendErrorMsg("AFTER RUN VERIFICATION");
         Logger.getLogger(this.getClass().getName()).log(Level.INFO, "AFTER RUN VERIFICATION");
    }
    
}
