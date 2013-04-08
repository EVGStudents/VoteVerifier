package ch.bfh.univoteverifier.utils;

/*
 * // * To change this template, choose Tools | Templates
 * // * and open the template in the editor.
 * // */
//
import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;

/**
 * // *
 * // * @author snake
 * // */
public class ElectionProxy {
	
	private ElectionData ed = null;
	private String eID;
	
	public ElectionProxy(String eID){
		this.eID = eID;
	}
	
	
	public ElectionData getElectionData() throws ElectionBoardServiceFault{
		long currentTimeMillis = System.currentTimeMillis();
		
		if(ed == null)
			ed = getElectionData(eID);
		
		if(Config.DEBUG_MODE){
			long finalTime = System.currentTimeMillis() - currentTimeMillis;
			System.out.println("Time of execution of web service call: " + finalTime);
		}
		
		return ed;
		
	}
	private static ElectionData getElectionData(java.lang.String electionId) throws ElectionBoardServiceFault {
		ch.bfh.univote.election.ElectionBoardService service = new ch.bfh.univote.election.ElectionBoardService();
		ch.bfh.univote.election.ElectionBoard port = service.getElectionBoardPort();
		return port.getElectionData(electionId);
	}
	
}
