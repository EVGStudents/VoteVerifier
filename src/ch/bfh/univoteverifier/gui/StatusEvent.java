/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.List;


/**
 *
 * @author prinstin
 */
public class StatusEvent {
    
    String message;
    StatusMessage statusMessage;
    List vr;
    
    public StatusEvent(StatusMessage sm, String message ){
      this.message = message;
      this.statusMessage =sm;
     
    }
//    
//    public StatusEvent(StatusMessage sm, List l){
//        this.statusMessage = sm;
//        this.l = l;     
//    }
    
   public StatusEvent(StatusMessage sm, List vr){
        this.statusMessage = sm;
        this.vr = vr;     
    }
        
    
    public StatusMessage getStatusMessage(){
        return statusMessage;
    }
    public String getMessage(){
    return message;
    }

    public List getVerificationResult() {
        return vr;
    }
    
    
}


