/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.gui.StatusEvent;
import ch.bfh.univoteverifier.gui.StatusMessage;


/**
 *
 * @author snake and prinstin
 */
public class UniversalVerification extends AbstractVerification{
 
    public UniversalVerification(){

    }
    
       public void testObserverPattern(){
        StatusEvent se = new StatusEvent(StatusMessage.VRF_STATUS, "This is a message through the observer pattern");
        ss.notifyListeners(se);
    }
}
