/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

/**
 *
 * @author prinstin
 */
public class StatusEvent {
    
    String message;
    StatusMessage statusMessage;
    
    public StatusEvent(StatusMessage sm, String message ){
      this.message = message;
      this.statusMessage =sm;
     
    }
    
    public StatusMessage getStatusMessage(){
        return statusMessage;
    }
    public String getMessage(){
    return message;
    }
}
