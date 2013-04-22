/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.gui.StatusEvent;
import ch.bfh.univoteverifier.gui.StatusListener;
import ch.bfh.univoteverifier.gui.StatusMessage;
import ch.bfh.univoteverifier.gui.StatusSubject;
import ch.bfh.univoteverifier.verification.SectionNameEnum;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prinstin
 */
public class GUIMessenger {
    
    ResourceBundle rb;
    StatusSubject ss;
    SectionNameEnum activeSection = SectionNameEnum.UNSET;

    public GUIMessenger(){
        Logger.getLogger(QRCode.class.getName()).log(Level.INFO, "GUIMessenger instantiated");
        ss = new ConcreteSubject();
        instantiateRB("en");
    }
    
    public void sendErrorMsg(String str){
        StatusEvent se = new StatusEvent(StatusMessage.ERROR, str);
        ss.notifyListeners(se);
    }
    
    
   public void sendVrfMsg(VerificationResult vr){
       vr.setSection(activeSection);
        StatusEvent se = new StatusEvent(StatusMessage.VRF_RESULT, vr);
        ss.notifyListeners(se);
    }
    
    
    public void  instantiateRB(String str){
      changeLocale(str);
    }
    
    public void changeLocale(String str){
       Locale loc = new Locale(str);
        rb = ResourceBundle.getBundle("error", loc);
    }
    
    public void changeSection(SectionNameEnum s){
        this.activeSection=s;
    }
    
    
    public String getMessageForKey(String key){
        String resultStr = rb.getString(key);
        return resultStr;
    }
    	/**
	 * get the status subject for this class
	 * @return the status subject for this object which is part of the observer pattern
	 */
	public StatusSubject getStatusSubject() {
		return this.ss;
	}
        
        
    /**
	 * a subject that is used in an observer pattern with the GUI information
	 * used to display messages in the status console of the GUI as well as
	 * relay information regarding the status of verification process
	 */
	private class ConcreteSubject implements StatusSubject {
		
		public ConcreteSubject() {
		}
		ArrayList<StatusListener> listeners = new ArrayList();
		
		@Override
		public void addListener(StatusListener sl) {
                    Logger.getLogger(QRCode.class.getName()).log(Level.INFO, "listener registered {0}",sl.getClass().getEnclosingClass());
			listeners.add(sl);
		}
		
		@Override
		public void removeListener(StatusListener sl) {
			listeners.remove(sl);
		}
		
        @Override
        public void notifyListeners(StatusEvent se) {
            Logger.getLogger(QRCode.class.getName()).log(Level.INFO, "notifyListeners called");
            
            for (StatusListener pl : listeners) {
                Logger.getLogger(QRCode.class.getName()).log(Level.INFO, "listener notified: {0}", pl.getClass().getEnclosingClass());
                pl.updateStatus(se);
            }
        }
    }
}
