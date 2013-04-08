/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

import java.awt.List;

/**
 *
 * @author prinstin
 */
public class StatusEvent {
    
    String message;
    StatusMessage statusMessage;
    List l;
    
    public StatusEvent(StatusMessage sm, String message ){
      this.message = message;
      this.statusMessage =sm;
     
    }
    
    public StatusEvent(StatusMessage sm, List l){
        this.statusMessage = sm;
        this.l = l;
            
    }
    
    public StatusMessage getStatusMessage(){
        return statusMessage;
    }
    public String getMessage(){
    return message;
    }
}
