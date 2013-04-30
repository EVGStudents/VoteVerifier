/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.utils;

import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import com.thoughtworks.xstream.XStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	final String DES_PATH = "src/test/java/ch/bfh/univoteverifier/testresources";
	final String ELECTION_ID = "vsbfh-2013";
	final List<String> mixerIdentifier;
	final List<String> tallierIdentifier;
	final String EXT = ".xml";

	public static void main(String[] args) throws ElectionBoardServiceFault, FileNotFoundException {
		LocalBoardProxyDownloader lbpd = new LocalBoardProxyDownloader();

		//Write all the objects to files
		//lbpd.writeBallot();
		lbpd.writeBallots();
		lbpd.writeBlindedGenerator();
		lbpd.writeDecodedVotes();
		lbpd.writeDecryptedVotes();
		lbpd.writeElectionData();
		lbpd.writeElectionDefinition();
		lbpd.writeElectionGenerator();
		lbpd.writeElectionOptions();
		lbpd.writeElectionSystemInfo();
		lbpd.writeElectoralRoll();
		lbpd.writeEncryptionKey();
		lbpd.writeEncryptionKeyShare();
		lbpd.writeEncryptionParameters();
		lbpd.writeLatelyMixedVerificationKeys();
		lbpd.writeLatelyMixedVerificationKeysBy();
		lbpd.writeLatelyRegisteredVoterCerts();
		lbpd.writeMixedEncryptedVotes();
		lbpd.writeMixedEncryptedVotesBy();
		lbpd.writeMixedVerificationKeys();
		lbpd.writeMixedVerificationKeysBy();
		lbpd.writePartiallyDecrpytedVotes();
		lbpd.writeRootCertificate();
		lbpd.writeSignatureParameters();
		lbpd.writeVoterCerts();
	}

	/**
	 * Construct a new local board proxy downloaded
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public LocalBoardProxyDownloader() throws ElectionBoardServiceFault, FileNotFoundException {
		xstream = new XStream();
		ebp = new ElectionBoardProxy(ELECTION_ID);
		mixerIdentifier = ebp.getElectionDefinition().getMixerId();
		tallierIdentifier = ebp.getElectionDefinition().getTallierId();
	}

	/**
	 * Write an object to a file as XML
	 *
	 * @param o the object to be written
	 * @param suffix the name of file
	 */
	private void realWrite(Object o, String suffix) {
		try {
			xstream.toXML(o, new FileOutputStream(DES_PATH + "/" + suffix + ELECTION_ID + EXT));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(LocalBoardProxyDownloader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Write a signale ballot
	 */
	public void writeBallot() throws ElectionBoardServiceFault {
		//ToDo - Change it, read the real verification key from a qr-code
		BigInteger verificationKey = new BigInteger("1");

		realWrite(ebp.getBallot(verificationKey), "SingleBallot");
	}

	/**
	 * Write the ballots
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeBallots() throws ElectionBoardServiceFault {
		realWrite(ebp.getBallots(), "Ballots");
	}

	/**
	 * Write the blinded generator of all mixers
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeBlindedGenerator() throws ElectionBoardServiceFault {
		Map<String, BlindedGenerator> blindGen = new HashMap<>();

		for (String mixerID : mixerIdentifier) {
			blindGen.put(mixerID, ebp.getBlindedGenerator(mixerID));
		}

		realWrite(blindGen, "BlindedGenerator");
	}

	/**
	 * Write the decoded votes
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeDecodedVotes() throws ElectionBoardServiceFault {
		realWrite(ebp.getDecodedVotes(), "DecodedVotes");
	}

	/**
	 * Write the decrypted votes
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeDecryptedVotes() throws ElectionBoardServiceFault {
		realWrite(ebp.getDecryptedVotes(), "DecryptedVotes");
	}

	/**
	 * Write the election data
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeElectionData() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionData(), "ElectionData");
	}

	/**
	 * Write the election definition
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeElectionDefinition() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionDefinition(), "ElectionDefinition");
	}

	/**
	 * Write the election generator
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeElectionGenerator() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionGenerator(), "ElectionGenerator");
	}

	/**
	 * Write the election options
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeElectionOptions() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionOptions(), "ElectionOptions");
	}

	/**
	 * Write the election system info
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeElectionSystemInfo() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionSystemInfo(), "ElectionSystemInfo");
	}

	/**
	 * Write the electoral roll
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeElectoralRoll() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectoralRoll(), "ElectoralRoll");
	}

	/**
	 * Write the encryption key
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeEncryptionKey() throws ElectionBoardServiceFault {
		realWrite(ebp.getEncryptionKey(), "EncryptionKey");
	}

	/**
	 * Write the encryption key share for every tallier
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeEncryptionKeyShare() throws ElectionBoardServiceFault {
		Map<String, EncryptionKeyShare> encKeyShareBy = new HashMap<>();

		for (String tallierID : tallierIdentifier) {
			encKeyShareBy.put(tallierID, ebp.getEncryptionKeyShare(tallierID));
		}

		realWrite(encKeyShareBy, "EncryptionKeyShare");
	}

	/**
	 * Write the encryption parameters
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeEncryptionParameters() throws ElectionBoardServiceFault {
		realWrite(ebp.getEncryptionParameters(), "EncryptionParameters");
	}

	/**
	 * Write the lately mixed verification keys
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeLatelyMixedVerificationKeys() throws ElectionBoardServiceFault {
		realWrite(ebp.getLateyMixedVerificationKeys(), "LatelyMixedVerificationKeys");
	}

	/**
	 * Write the lately mixed verification keys for each mixer
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeLatelyMixedVerificationKeysBy() throws ElectionBoardServiceFault {
		Map<String, List<MixedVerificationKey>> latelyMixedVerKeyBy = new HashMap<>();

		for (String mixerID : mixerIdentifier) {
			latelyMixedVerKeyBy.put(mixerID, ebp.getLatelyMixedVerificationKeysBy(mixerID));
		}

		realWrite(latelyMixedVerKeyBy, "LatelyMixedVerificationKeysBy");
	}

	/**
	 * Write the lately registered voter certs
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeLatelyRegisteredVoterCerts() throws ElectionBoardServiceFault {
		realWrite(ebp.getLatelyRegisteredVoterCerts(), "LatelyRegisteredVoterCerts");
	}

	/**
	 * Write the mixed encrypted votes
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeMixedEncryptedVotes() throws ElectionBoardServiceFault {
		realWrite(ebp.getMixedEncryptedVotes(), "MixedEncryptedVotes");
	}

	/**
	 * Write the mixed encrypted votes for each mixer
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeMixedEncryptedVotesBy() throws ElectionBoardServiceFault {
		Map<String, MixedEncryptedVotes> mixEncVotesBy = new HashMap<>();

		for (String mixerID : mixerIdentifier) {
			mixEncVotesBy.put(mixerID, ebp.getMixedEncryptedVotesBy(mixerID));
		}

		realWrite(mixEncVotesBy, "MixedEncryptedVotesBy");
	}

	/**
	 * Write the mixed verification keys
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeMixedVerificationKeys() throws ElectionBoardServiceFault {
		realWrite(ebp.getMixedVerificationKeys(), "MixedVerificationKeys");
	}

	/**
	 * Write the mixed verification keys for each mixer
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeMixedVerificationKeysBy() throws ElectionBoardServiceFault {
		Map<String, MixedVerificationKeys> mixVerKeysBy = new HashMap<>();

		for (String mixerID : mixerIdentifier) {
			mixVerKeysBy.put(mixerID, ebp.getMixedVerificationKeysBy(mixerID));
		}

		realWrite(mixVerKeysBy, "MixedVerificationKeysBy");
	}

	/**
	 * Write the partially decrypted votes for each tallier
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writePartiallyDecrpytedVotes() throws ElectionBoardServiceFault {
		Map<String, PartiallyDecryptedVotes> parDecVotes = new HashMap<>();

		for (String tallierID : tallierIdentifier) {
			parDecVotes.put(tallierID, ebp.getPartiallyDecryptedVotes(tallierID));
		}

		realWrite(parDecVotes, "PartiallyDecryptedVotes");
	}

	/**
	 * Write the root certificate
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeRootCertificate() throws ElectionBoardServiceFault {
		realWrite(ebp.getRootCertificate(), "RootCertificate");
	}

	/**
	 * Write the signature parameters
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeSignatureParameters() throws ElectionBoardServiceFault {
		realWrite(ebp.getSignatureParameters(), "SignatureParameters");
	}

	/**
	 * Write the voter certificates
	 *
	 * @throws ElectionBoardServiceFault
	 */
	public void writeVoterCerts() throws ElectionBoardServiceFault {
		realWrite(ebp.getVoterCerts(), "VoterCerts");
	}
}
