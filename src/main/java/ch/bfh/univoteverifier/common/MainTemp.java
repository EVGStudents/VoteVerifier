package ch.bfh.univoteverifier.common;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.utils.ElectionBoardProxy;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainTemp {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException{
		
		ElectionBoardProxy ep = new ElectionBoardProxy("vsbfh-2013");
		try {
			ep.getSignatureParameters();
		} catch (ElectionBoardServiceFault ex ) {
			Logger.getLogger(MainTemp.class.getName()).log(Level.SEVERE, null, ex);
		}

		System.out.println("test");
	}	

}
