/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.utils;
//
//import ch.bfh.univote.election.ElectionBoardService;
//import ch.bfh.univote.election.ElectionBoardServiceFault;

/**
 *
 * @author snake
 */
public class ElectionBoardProxy {
//
private final String eID;
//private ch.bfh.univote.common.ElectionData ed;
//private ch.bfh.univote.election.ElectionBoard eb;
//	
	public ElectionBoardProxy(String eID){
		this.eID = eID;	
	}
//
//	private void getElectionBoard(){
//		ElectionBoardService ebs = new ElectionBoardService();
//		eb = ebs.getElectionBoardPort();
//	}
//	
//	public ch.bfh.univote.common.ElectionData getElectionData() throws ElectionBoardServiceFault{
//		long currentTimeMillis = System.currentTimeMillis();
//	
//		if(ed == null)
//			ed = eb.getElectionData(eID);
//		
//		if(Config.DEBUG_MODE){
//		long finalTime = System.currentTimeMillis() - currentTimeMillis;
//			System.out.println("Time of execution of web service call: " + finalTime);
//		}
//		
//		return ed;
//		
//	}
}
