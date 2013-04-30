/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.VerificationType;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author prinstin
 */
public class VerificationSwingWorker extends SwingWorker<VerificationEvent,Void> {

    String eID="";
    Verification v;
    Messenger msgr;
    
    public VerificationSwingWorker(Messenger msgr, String eID){
        this.eID = eID;
        this.v = new UniversalVerification(msgr, eID);
        this.msgr = msgr;
        
    }

    @Override
    protected VerificationEvent doInBackground() throws Exception {
              msgr.sendErrorMsg("BEFORE RUN VERIFICATION");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "BEFORE RUN VERIFICATION");
        v.runVerification();
        msgr.sendErrorMsg("AFTER RUN VERIFICATION");
         Logger.getLogger(this.getClass().getName()).log(Level.INFO, "AFTER RUN VERIFICATION");
         return new VerificationEvent(VerificationType.EL_PERIOD_BALLOT, true);
    }
    
        @Override
    protected void done(){
            String str ="error";
        try {
             VerificationEvent ve= get();
             msgr.sendVrfMsg(ve);
        } catch (InterruptedException ex) {
            Logger.getLogger(VerificationSwingWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(VerificationSwingWorker.class.getName()).log(Level.SEVERE, null, ex);
        }

        }
}
