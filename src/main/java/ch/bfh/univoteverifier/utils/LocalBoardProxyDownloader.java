/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.utils;

import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import com.thoughtworks.xstream.XStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class stores the data from the election board of univote into XML file that can
 * be later converted into real java objects. These objects can be used as a source data
 * for the univote verifier
 * @author snake
 */
public class LocalBoardProxyDownloader {
	XStream xstream;
	ElectionBoardProxy ebp;
	String destPath = "src/test/java/ch/bfh/univoteverifier/testresources";
	
	public static void main(String[] args) throws ElectionBoardServiceFault, FileNotFoundException {
		LocalBoardProxyDownloader lbpd = new LocalBoardProxyDownloader();
		lbpd.writeElectionDataToFile();
		
	}
	
	public LocalBoardProxyDownloader() throws ElectionBoardServiceFault {
		xstream = new XStream();
		ebp = new ElectionBoardProxy("sub-2013");
	}

	/**
	 * Write an object to a file as XML
	 * @param o the object to be writeen
	 * @param suffix the name of file
	 */
	private void realWrite(Object o, String suffix){
		try {
			xstream.toXML(o, new FileOutputStream(destPath+"/"+suffix+"Obj.xml"));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(LocalBoardProxyDownloader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Write the election data to a file
	 * @throws FileNotFoundException
	 * @throws ElectionBoardServiceFault 
	 */
	public void writeElectionDataToFile() throws FileNotFoundException, ElectionBoardServiceFault{
		realWrite(ebp.getElectionData(), "ElectionData");
	}

}
