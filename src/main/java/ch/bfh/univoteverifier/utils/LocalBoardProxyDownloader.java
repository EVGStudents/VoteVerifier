/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.utils;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import com.thoughtworks.xstream.XStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class stores the data from the election board of univote into XML file
 * that can be later converted into real java objects. These objects can be used
 * as a source data for the univote verifier
 *
 * @author snake
 */
public class LocalBoardProxyDownloader {

	XStream xstream;
	ElectionBoardProxy ebp;
	String destPath = "src/test/java/ch/bfh/univoteverifier/testresources";

	public static void main(String[] args) throws ElectionBoardServiceFault, FileNotFoundException {
		LocalBoardProxyDownloader lbpd = new LocalBoardProxyDownloader();
		lbpd.writeBallots();
		lbpd.writeDecodedVotes();
		lbpd.writeDecryptedVotes();
		lbpd.writeElectionData();
		lbpd.writeElectionDefinition();
		lbpd.writeElectionGenerator();
		lbpd.writeElectionOptions();
		lbpd.writeElectionSystemInfo();
		lbpd.writeElectoralRoll();
		lbpd.writeEncryptionKey();
		lbpd.writeEncryptionParameters();
		lbpd.writeLatelyMixedVerificationKeys();
		lbpd.writeLatelyRegisteredVoterCerts();
		lbpd.writeMixedEncryptedVotes();
		lbpd.writeMixedVerificationKeys();
		lbpd.writeRootCertificate();
		lbpd.writeSignatureParameters();
		lbpd.writeVoterCerts();
	}

	public LocalBoardProxyDownloader() throws ElectionBoardServiceFault {
		xstream = new XStream();
		ebp = new ElectionBoardProxy("vsbfh-2013");
	}

	/**
	 * Write an object to a file as XML
	 *
	 * @param o the object to be writeen
	 * @param suffix the name of file
	 */
	private void realWrite(Object o, String suffix) {
		try {
			xstream.toXML(o, new FileOutputStream(destPath + "/" + suffix + "Obj.xml"));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(LocalBoardProxyDownloader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void writeBallots() throws ElectionBoardServiceFault {
		realWrite(ebp.getBallots(), "Ballots");
	}

	public void writeDecodedVotes() throws ElectionBoardServiceFault {
		realWrite(ebp.getDecodedVotes(), "DecodedVotes");
	}

	public void writeDecryptedVotes() throws ElectionBoardServiceFault {
		realWrite(ebp.getDecryptedVotes(), "DecryptedVotes");
	}

	/**
	 * Write the election data to a file
	 *
	 * @throws FileNotFoundException
	 * @throws ElectionBoardServiceFault
	 */
	public void writeElectionData() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionData(), "ElectionData");
	}

	public void writeElectionDefinition() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionDefinition(), "ElectionDefinition");
	}

	public void writeElectionGenerator() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionGenerator(), "ElectionGenerator");
	}

	public void writeElectionOptions() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionOptions(), "ElectionOptions");
	}

	public void writeElectionSystemInfo() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionSystemInfo(), "ElectionSystemInfo");
	}

	public void writeElectoralRoll() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectoralRoll(), "ElectoralRoll");
	}

	public void writeEncryptionKey() throws ElectionBoardServiceFault {
		realWrite(ebp.getEncryptionKey(), "EncryptionKey");
	}

	public void writeEncryptionParameters() throws ElectionBoardServiceFault {
		realWrite(ebp.getEncryptionParameters(), "EncryptionParameters");
	}

	public void writeLatelyRegisteredVoterCerts() throws ElectionBoardServiceFault {
		realWrite(ebp.getLatelyRegisteredVoterCerts(), "LatelyRegisteredVoterCerts");
	}

	public void writeLatelyMixedVerificationKeys() throws ElectionBoardServiceFault {
		realWrite(ebp.getLateyMixedVerificationKeys(), "LatelyMixedVerificationKeys");
	}

	public void writeMixedEncryptedVotes() throws ElectionBoardServiceFault {
		realWrite(ebp.getMixedEncryptedVotes(), "MixedEncryptedVotes");
	}

	public void writeMixedVerificationKeys() throws ElectionBoardServiceFault {
		realWrite(ebp.getMixedVerificationKeys(), "MixedVerificationKeys");
	}

	public void writeRootCertificate() throws ElectionBoardServiceFault {
		realWrite(ebp.getRootCertificate(), "RootCertificate");
	}

	public void writeSignatureParameters() throws ElectionBoardServiceFault {
		realWrite(ebp.getSignatureParameters(), "SignatureParameters");
	}

	public void writeVoterCerts() throws ElectionBoardServiceFault {
		realWrite(ebp.getVoterCerts(), "VoterCerts");
	}
}
