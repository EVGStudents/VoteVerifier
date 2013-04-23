/**
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

import ch.bfh.univoteverifier.verification.VerificationResult;


/**
 * Status Event is a helper class that contains information that shall be sent to the GUI
 * this class is part of the observer pattern
 * @author prinstin
 */
public class StatusEvent {
    
    String message;
    StatusMessage statusMessage;
    VerificationResult vr;
    
    /**
     * instantiate a StatusEvent 
     * @param sm the type of message that this StatusEvent contains defined by the enum class StatusMessage
     * @param message the message in user-friendly form
     */
    public StatusEvent(StatusMessage sm, String message ){
      this.message = message;
      this.statusMessage =sm;
     
    }

    
    /**
     * create an instance of StatusEvent used to relay results from a verification
     * @param sm the type of message that this StatusEvent contains defined by the enum class StatusMessage
     * @param vr the helper class which contains results from a verification
     */
    public StatusEvent(StatusMessage sm, VerificationResult vr){
        this.statusMessage = sm;
        this.vr = vr;     
    }
   
        
    /**
     * get the type of StatusMessage that this Event is delivering
     * @return the StatusMessage type
     */
    public StatusMessage getStatusMessage(){
        return statusMessage;
    }
    /**
     * get the String of the message this StatusEvent is delivering
     * @return String the message to deliver to the GUI
     */
    public String getMessage(){
    return message;
    }

    /**
     * get the VerificationResult helper class this StatusEvent is delivering
     * @return VerificationResult the verification results packed into a helped class
     */
    public VerificationResult getVerificationResult() {
        return vr;
    }
    
    
}


