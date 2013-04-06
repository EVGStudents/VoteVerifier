package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.primitives.VerifyManager;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;






public class MainTemp {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		//System.out.println(Config.p.toString());
		//System.out.println(Config.q.toString());
		//System.out.println(Config.g.toString());
	
		
		VerifyManager vm = new VerifyManager(0);
		
		try {		
			vm.setupVerifier();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(MainTemp.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		
		
	}

}
