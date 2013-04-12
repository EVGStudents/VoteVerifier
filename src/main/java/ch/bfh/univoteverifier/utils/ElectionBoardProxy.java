/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.utils;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.DecryptedVotes;
import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.ElectionGenerator;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ElectionSystemInfo;
import ch.bfh.univote.common.ElectoralRoll;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.SignatureParameters;
import ch.bfh.univote.common.VoterCertificate;
import ch.bfh.univote.common.VoterCertificates;
import ch.bfh.univote.election.ElectionBoard;
import ch.bfh.univote.election.ElectionBoardService;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is used as a proxy for the ElectionBoard of UniVote.
 * @author snake
 */
public class ElectionBoardProxy {
	
	private final String eID;
	private URL wsdlURL;
	private static final Logger LOGGER = Logger.getLogger(ElectionBoardProxy.class.getName());
	
	private static SignatureParameters signParam;
	private static Certificate rootCert;
	private static ElectionSystemInfo sysInfo;
	private static ElectionDefinition elDef;
	private static EncryptionParameters encParam;
	private static EncryptionKeyShare encKeyShare;
	private static EncryptionKey encKey;
	private static BlindedGenerator blindGen;
	private static ElectionGenerator elGen;
	private static ElectionOptions elOpt;
	private static ElectionData elData;
	private static ElectoralRoll elRoll;
	private static VoterCertificates voterCerts;
	private static MixedVerificationKeys mixVerKeyBy;
	private static MixedVerificationKeys mixVerKey;
	private static List<VoterCertificate> latelyRegVoteCerts;
	private static List<MixedVerificationKey> latelyMixVerKeyBy;
	private static List<MixedVerificationKey> latelyMixVerKey;
	private static Ballots ballots;
	private static MixedEncryptedVotes mixEncVotesBy;
	private static MixedEncryptedVotes mixEncVotes;
	private static PartiallyDecryptedVotes parDecVotes;
	private static DecryptedVotes decryptedVotes;
	private static DecodedVotes decodedVotes;
	
	private ElectionBoard eb;
	
	/**
	 * Construct an ElectionBoardProxy with a given election id
	 * @param eID the ID of the election
	 */
	public ElectionBoardProxy(String eID){
		this.eID = eID;
		
		try {
			this.wsdlURL = new URL("http://univote.ch:8080/ElectionBoardService/ElectionBoardServiceImpl?wsdl");
		} catch (MalformedURLException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}
		
		getElectionBoard();
	}
	
	/**
	 * Construct an ElectionBoardProxy with a given election id
	 * and a URL as a source of the web services.
	 * @param eID the ID of the election
	 * @param wsdlURL the URL of the WSDL file
	 */
	public ElectionBoardProxy(String eID, String wsdlURL){
		this.eID = eID;
		
		try {
			this.wsdlURL = new URL(wsdlURL);
		} catch (MalformedURLException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}
		
		getElectionBoard();
	}
	
	
	/**
	 * Used to instantiate the ElectionBoardPort from where we
	 * get the data of an election
	 */
	private void getElectionBoard(){
		ElectionBoardService ebs = new ElectionBoardService(wsdlURL);
		eb = ebs.getElectionBoardPort();
	}
	
	/**
	 * Get the signature parameters
	 * @return the signature parameters
	 */
	public SignatureParameters getSignatureParameters() throws ElectionBoardServiceFault {
		if(signParam == null)
			signParam = eb.getSignatureParameters();
		
		return signParam;
	}
	
	/**
	 * Get the root certificate
	 * @return the root certificate
	 */
	public Certificate getRootCertificate() throws ElectionBoardServiceFault {
		if(rootCert == null)
			rootCert = eb.getRootCertificate();
		
		return rootCert;
	}
	
	/**
	 * Get the election system information
	 * @return the election system information
	 */
	public ElectionSystemInfo getElectionSystemInfo() throws ElectionBoardServiceFault {
		if(sysInfo == null)
			sysInfo = eb.getElectionSystemInfo();
		
		return sysInfo;
	}
	
	/**
	 * Get the election definition
	 * @return the election definition
	 */
	public ElectionDefinition getElectionDefinition() throws ElectionBoardServiceFault {
		if(elDef == null)
			elDef = eb.getElectionDefinition(eID);
		
		return elDef;
	}
	
	/**
	 * Get the encryption parameters
	 * @return the encryption parameters
	 */
	public  EncryptionParameters getEncryptionParameters() throws ElectionBoardServiceFault {
		if(encParam == null)
			encParam = eb.getEncryptionParameters(eID);
		
		return encParam;
	}
	
	/**
	 * Get the encryption key share of tallierID
	 * @return the encryption key share of tallierID
	 */
	public  EncryptionKeyShare getEncryptionKeyShare(String tallierID) throws ElectionBoardServiceFault {
		if(encKeyShare == null)
			encKeyShare = eb.getEncryptionKeyShare(eID, tallierID);
		
		return encKeyShare;
	}
	
	/**
	 * Get the encryption key
	 * @return the encryption key
	 */
	public  EncryptionKey getEncryptionKey() throws ElectionBoardServiceFault {
		if(encKey == null)
			encKey = eb.getEncryptionKey(eID);
		
		return encKey;
	}
	
	/**
	 * Get the blinded generator of a given mixerID
	 * @return the blinded generator of mixerID
	 */
	public  BlindedGenerator getBlindedGenerator(String mixerID) throws ElectionBoardServiceFault {
		if(blindGen == null)
			blindGen = eb.getBlindedGenerator(eID, mixerID);
		
		return blindGen;
	}
	
	/**
	 * Get the election generator
	 * @return the election generator
	 */
	public  ElectionGenerator getElectionGenerator() throws ElectionBoardServiceFault {
		if(elGen == null)
			elGen = eb.getElectionGenerator(eID);
		
		return elGen;
	}
	
	/**
	 * Get the election option
	 * @return the election option
	 */
	public  ElectionOptions getElectionOptions() throws ElectionBoardServiceFault {
		if(elOpt == null)
			elOpt = eb.getElectionOptions(eID);
		
		return elOpt;
	}
	
	/**
	 * Get the election data
	 * @return the election data
	 */
	public  ElectionData getElectionData() throws ElectionBoardServiceFault {
		if(elData == null)
			elData = eb.getElectionData(eID);
		
		return elData;
	}
	
	/**
	 * Get the electoral roll
	 * @return the electoral roll
	 */
	public  ElectoralRoll getElectoralRoll() throws ElectionBoardServiceFault {
		if(elRoll == null)
			elRoll = eb.getElectoralRoll(eID);
		
		return elRoll;
	}
	
	/**
	 * Get the voter certificates
	 * @return the voter certificates
	 */
	public  VoterCertificates getVoterCerts() throws ElectionBoardServiceFault {
		if(voterCerts == null)
			voterCerts = eb.getVoterCertificates(eID);
		
		return voterCerts;
	}
	
	/**
	 * @return the mixVerKeyBy
	 */
	public  MixedVerificationKeys getMixedVerificationKeysBy(String mixerID) throws ElectionBoardServiceFault {
		if(mixVerKeyBy == null)
			mixVerKeyBy = eb.getVerificationKeysMixedBy(eID, mixerID);
		
		return mixVerKeyBy;
	}
	
	/**
	 * Get the mixed verification keys
	 * @return the mixed verification keys
	 */
	public  MixedVerificationKeys getMixedVerificationKeys() throws ElectionBoardServiceFault {
		if(mixVerKey == null)
			mixVerKey = eb.getMixedVerificationKeys(eID);
		
		return mixVerKey;
	}
	
	/**
	 * Get the lately registered voter certificate
	 * @return the lately registered voter certificate
	 */
	public  List<VoterCertificate> getLatelyRegisteredVoterCerts() throws ElectionBoardServiceFault {
		if(latelyRegVoteCerts == null)
			latelyRegVoteCerts = eb.getLatelyRegisteredVoterCertificates(eID);
		
		return latelyRegVoteCerts;
	}
	
	/**
	 * Get the lately mixed verification key mixed by mixerID
	 * @param mixerID the ID of a given mixer
	 * @return the lately mixed verification key of mixerID
	 */
	public  List<MixedVerificationKey> getLatelyMixedVerificationKeysBy(String mixerID) throws ElectionBoardServiceFault {
		if(latelyMixVerKeyBy == null)
			latelyMixVerKeyBy = eb.getVerificationKeysLatelyMixedBy(eID, mixerID);
		
		return latelyMixVerKeyBy;
	}
	
	/**
	 * Get the lately mixed verification key
	 * @return the lately mixed verification key
	 */
	public  List<MixedVerificationKey> getLateyMixedVerificationKeys() throws ElectionBoardServiceFault {
		if(latelyMixVerKey == null)
			latelyMixVerKey = eb.getLatelyMixedVerificationKeys(eID);
		
		return latelyMixVerKey;
	}
	
	/**
	 * Get the ballots
	 * @return the ballots
	 */
	public  Ballots getBallots() throws ElectionBoardServiceFault {
		if(ballots == null)
			ballots = eb.getBallots(eID);
		
		return ballots;
	}
	
	/**
	 * Get the mixed encrypted votes by a given mixer
	 * @param mixerID the ID of a given mixer
	 * @return the mixed encrypted votes of the given mixer
	 */
	public  MixedEncryptedVotes getMixedEncryptedVotesBy(String mixerID) throws ElectionBoardServiceFault {
		if(mixEncVotesBy == null)
			mixEncVotesBy = eb.getEncryptedVotesMixedBy(eID, mixerID);
		
		return mixEncVotesBy;
	}
	
	/**
	 * Get the mixed encrypted votes
	 * @return the mixed encrypted votes
	 */
	public  MixedEncryptedVotes getMixedEncryptedVotes() throws ElectionBoardServiceFault {
		if(mixEncVotes == null)
			mixEncVotes = eb.getMixedEncryptedVotes(eID);
		
		return mixEncVotes;
	}
	
	/**
	 * Get the partially decrypted votes
	 * @param taillierID the ID of a given tallier
	 * @return the partially decrypted votes
	 */
	public  PartiallyDecryptedVotes getPartiallyDecryptedVotes(String tallierID) throws ElectionBoardServiceFault {
		if(parDecVotes == null)
			parDecVotes = eb.getPartiallyDecryptedVotes(eID, tallierID);
		
		return parDecVotes;
	}
	
	/**
	 * Get the decrypted votes
	 * @return the decrypted votes
	 */
	public  DecryptedVotes getDecryptedVotes() throws ElectionBoardServiceFault {
		if(decryptedVotes == null)
			decryptedVotes = eb.getDecryptedVotes(eID);
		
		return decryptedVotes;
	}
	
	/**
	 * Get the decoded votes
	 * @return the decoded votes
	 */
	public  DecodedVotes getDecodedVotes() throws ElectionBoardServiceFault {
		if(decodedVotes == null)
			decodedVotes = eb.getDecodedVotes(eID);
		
		return decodedVotes;
	}
	
	/**
	 * Get a ballot
	 * @param verificationKey the verification key for this ballor
	 * @return the ballot
	 * @throws ElectionBoardServiceFault
	 */
	public Ballot getBallot(BigInteger verificationKey) throws ElectionBoardServiceFault{
		return eb.getBallot(eID, verificationKey);
	}
	
}
