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
package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.gui.StatusSubject;
import ch.bfh.univoteverifier.verification.IndividualVerification;
import ch.bfh.univoteverifier.verification.UniversalVerification;
import ch.bfh.univoteverifier.verification.Verification;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the controller who is responsible for the communication between
 * the GUI and the internal infrastructure
 * @author snake
 */
public class MainController {
	
	private Verification v;

/**
 * create a instance of the main controller.
 * Only 1 is needed and is created by the GUI
 */
	public MainController(){
		//initialize the root logger - maybe this should be placed into the main method
		Handler h;
		
//		try {
//			h = new FileHandler("verification.log");
//			Logger.getLogger("").addHandler(h);
//			
//		} catch (	IOException | SecurityException ex) {
//			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
//		}
		
	}
	
        
	/**
	 * Run an universal verification
	 * @param eID String the ID of a election
	 */
	public void universalVerification(String eID){
		this.v = new UniversalVerification(eID);
	}

	public void runVerifcation(){
		v.runVerification();
	}
	
	
	/**
	 * Run an individual verification
	 * @param eID
	 */
	public void individualVerification(String eID){
		this.v = new IndividualVerification(eID);
	}
	
	/**
	 *
	 * @return
	 */
	public StatusSubject getStatusSubject() {
		return v.getStatusSubject();
		
	}
	
	
	public String decodeQRCode(File filepath){
		QRCode qr = new QRCode();
		//            String decodedStr = qr.decode(filepath);
		return "";
	}

        
}
