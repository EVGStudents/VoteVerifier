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
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import com.thoughtworks.xstream.XStream;
import java.io.File;
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
 * as a source data for the univote verifier.
 *
 * @author Scalzi Giuseppe
 */
public class LocalBoardProxyDownloader {

	private final XStream xstream;
	private final ElectionBoardProxy ebp;
	private final String DES_PATH = "src/test/java/ch/bfh/univoteverifier/testresources";
	private final String ELECTION_ID = "vsbfh-2013";
	private final List<String> mixerIdentifier;
	private final List<String> tallierIdentifier;
	private final String EXT = ".xml";

	public static void main(String[] args) throws ElectionBoardServiceFault, FileNotFoundException {
		LocalBoardProxyDownloader lbpd = new LocalBoardProxyDownloader();

		//Write all the objects to files
//		lbpd.writeBallot();
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
		lbpd.writeEncryptedVotes();
		lbpd.writeMixedEncryptedVotesBy();
		lbpd.writeMixedVerificationKeys();
		lbpd.writeMixedVerificationKeysBy();
		lbpd.writePartiallyDecrpytedVotes();
//		lbpd.writeRootCertificate();
		lbpd.writeSignatureParameters();
		lbpd.writeVoterCerts();
	}

	/**
	 * Construct a new local board proxy downloaded.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public LocalBoardProxyDownloader() throws ElectionBoardServiceFault, FileNotFoundException {
		xstream = new XStream();
		ebp = new ElectionBoardProxy(ELECTION_ID);
		mixerIdentifier = ebp.getElectionDefinition().getMixerId();
		tallierIdentifier = ebp.getElectionDefinition().getTallierId();
	}

	/**
	 * Write an object to a file as XML.
	 *
	 * @param o the object to be written.
	 * @param suffix the name of file.
	 */
	private void realWrite(Object o, String suffix) {
		try {
			xstream.toXML(o, new FileOutputStream(DES_PATH + "/" + suffix + "-" + ELECTION_ID + EXT));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(LocalBoardProxyDownloader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Write a single ballot.
	 */
	public void writeBallot() throws ElectionBoardServiceFault {
		QRCode qr = new QRCode(null);
		File f = new File(this.getClass().getResource("/qrcodeGiu").getPath());
		ElectionReceipt er = qr.decodeReceipt(f);
		BigInteger verificationKey = er.getVerificationKey();

		realWrite(ebp.getBallot(verificationKey), "SingleBallot");


	}

	/**
	 * Write the ballots.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeBallots() throws ElectionBoardServiceFault {
		realWrite(ebp.getBallots(), "Ballots");
	}

	/**
	 * Write the blinded generator of all mixers.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeBlindedGenerator() throws ElectionBoardServiceFault {
		Map<String, BlindedGenerator> blindGen = new HashMap<>();

		for (String mixerID : mixerIdentifier) {
			blindGen.put(mixerID, ebp.getBlindedGenerator(mixerID));
		}

		realWrite(blindGen, "BlindedGenerator");
	}

	/**
	 * Write the decoded votes.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeDecodedVotes() throws ElectionBoardServiceFault {
		realWrite(ebp.getDecodedVotes(), "DecodedVotes");
	}

	/**
	 * Write the decrypted votes.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeDecryptedVotes() throws ElectionBoardServiceFault {
		realWrite(ebp.getDecryptedVotes(), "DecryptedVotes");
	}

	/**
	 * Write the election data.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeElectionData() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionData(), "ElectionData");
	}

	/**
	 * Write the election definition.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeElectionDefinition() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionDefinition(), "ElectionDefinition");
	}

	/**
	 * Write the election generator.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeElectionGenerator() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionGenerator(), "ElectionGenerator");
	}

	/**
	 * Write the election options.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeElectionOptions() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionOptions(), "ElectionOptions");
	}

	/**
	 * Write the election system info.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeElectionSystemInfo() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectionSystemInfo(), "ElectionSystemInfo");
	}

	/**
	 * Write the electoral roll.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeElectoralRoll() throws ElectionBoardServiceFault {
		realWrite(ebp.getElectoralRoll(), "ElectoralRoll");
	}

	/**
	 * Write the encryption key.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeEncryptionKey() throws ElectionBoardServiceFault {
		realWrite(ebp.getEncryptionKey(), "EncryptionKey");
	}

	/**
	 * Write the encryption key share for every tallier.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeEncryptionKeyShare() throws ElectionBoardServiceFault {
		Map<String, EncryptionKeyShare> encKeyShareBy = new HashMap<>();

		for (String tallierID : tallierIdentifier) {
			encKeyShareBy.put(tallierID, ebp.getEncryptionKeyShare(tallierID));
		}

		realWrite(encKeyShareBy, "EncryptionKeyShare");
	}

	/**
	 * Write the encryption parameters.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeEncryptionParameters() throws ElectionBoardServiceFault {
		realWrite(ebp.getEncryptionParameters(), "EncryptionParameters");
	}

	/**
	 * Write the lately mixed verification keys.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeLatelyMixedVerificationKeys() throws ElectionBoardServiceFault {
		realWrite(ebp.getLateyMixedVerificationKeys(), "LatelyMixedVerificationKeys");
	}

	/**
	 * Write the lately mixed verification keys for each mixer.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeLatelyMixedVerificationKeysBy() throws ElectionBoardServiceFault {
		Map<String, List<MixedVerificationKey>> latelyMixedVerKeyBy = new HashMap<>();

		for (String mixerID : mixerIdentifier) {
			latelyMixedVerKeyBy.put(mixerID, ebp.getLatelyMixedVerificationKeysBy(mixerID));
		}

		realWrite(latelyMixedVerKeyBy, "LatelyMixedVerificationKeysBy");
	}

	/**
	 * Write the lately registered voter certs.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeLatelyRegisteredVoterCerts() throws ElectionBoardServiceFault {
		realWrite(ebp.getLatelyRegisteredVoterCerts(), "LatelyRegisteredVoterCerts");
	}

	/**
	 * Write the mixed encrypted votes.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeEncryptedVotes() throws ElectionBoardServiceFault {
		realWrite(ebp.getEncryptedVotes(), "EncryptedVotes");
	}

	/**
	 * Write the mixed encrypted votes for each mixer.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeMixedEncryptedVotesBy() throws ElectionBoardServiceFault {
		Map<String, MixedEncryptedVotes> mixEncVotesBy = new HashMap<>();

		for (String mixerID : mixerIdentifier) {
			mixEncVotesBy.put(mixerID, ebp.getMixedEncryptedVotesBy(mixerID));
		}

		realWrite(mixEncVotesBy, "MixedEncryptedVotesBy");
	}

	/**
	 * Write the mixed verification keys.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeMixedVerificationKeys() throws ElectionBoardServiceFault {
		realWrite(ebp.getMixedVerificationKeys(), "MixedVerificationKeys");
	}

	/**
	 * Write the mixed verification keys for each mixer.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeMixedVerificationKeysBy() throws ElectionBoardServiceFault {
		Map<String, MixedVerificationKeys> mixVerKeysBy = new HashMap<>();

		for (String mixerID : mixerIdentifier) {
			mixVerKeysBy.put(mixerID, ebp.getMixedVerificationKeysBy(mixerID));
		}

		realWrite(mixVerKeysBy, "MixedVerificationKeysBy");
	}

	/**
	 * Write the partially decrypted votes for each tallier.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writePartiallyDecrpytedVotes() throws ElectionBoardServiceFault {
		Map<String, PartiallyDecryptedVotes> parDecVotes = new HashMap<>();

		for (String tallierID : tallierIdentifier) {
			parDecVotes.put(tallierID, ebp.getPartiallyDecryptedVotes(tallierID));
		}

		realWrite(parDecVotes, "PartiallyDecryptedVotes");
	}

	/**
	 * Write the root certificate.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeRootCertificate() throws ElectionBoardServiceFault {
		realWrite(ebp.getRootCertificate(), "RootCertificate");
	}

	/**
	 * Write the signature parameters.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeSignatureParameters() throws ElectionBoardServiceFault {
		realWrite(ebp.getSignatureParameters(), "SignatureParameters");
	}

	/**
	 * Write the voter certificates.
	 *
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public void writeVoterCerts() throws ElectionBoardServiceFault {
		realWrite(ebp.getVoterCerts(), "VoterCerts");
	}
}
