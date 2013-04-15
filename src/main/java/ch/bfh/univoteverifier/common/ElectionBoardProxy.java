/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.common;

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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;
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
	
	private SignatureParameters signParam;
	private Certificate rootCert;
	private ElectionSystemInfo sysInfo;
	private ElectionDefinition elDef;
	private EncryptionParameters encParam;
	private EncryptionKeyShare encKeyShare;
	private EncryptionKey encKey;
	private BlindedGenerator blindGen;
	private ElectionGenerator elGen;
	private ElectionOptions elOpt;
	private ElectionData elData;
	private ElectoralRoll elRoll;
	private VoterCertificates voterCerts;
	private MixedVerificationKeys mixVerKeyBy;
	private MixedVerificationKeys mixVerKey;
	private List<VoterCertificate> latelyRegVoteCerts;
	private List<MixedVerificationKey> latelyMixVerKeyBy;
	private List<MixedVerificationKey> latelyMixVerKey;
	private Ballots ballots;
	private MixedEncryptedVotes mixEncVotesBy;
	private MixedEncryptedVotes mixEncVotes;
	private PartiallyDecryptedVotes parDecVotes;
	private DecryptedVotes decryptedVotes;
	private DecodedVotes decodedVotes;
	
	private RSAPublicKey CAPubKey;
	private RSAPublicKey EMPubKey;
	private RSAPublicKey EAPubKey;
	private Map<String, RSAPublicKey> talliersPubKeys;
	private Map<String, RSAPublicKey> mixersPubKeys;	
	
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
	 * @throws ElectionBoardServiceFault  
	 */
	public SignatureParameters getSignatureParameters() throws ElectionBoardServiceFault {
		if(signParam == null)
			signParam = eb.getSignatureParameters();
		
		return signParam;
	}
	
	/**
	 * Get the root certificate
	 * @return the root certificate
	 * @throws ElectionBoardServiceFault  
	 */
	public Certificate getRootCertificate() throws ElectionBoardServiceFault {
		if(rootCert == null)
			rootCert = eb.getRootCertificate();
		
		return rootCert;
	}
	
	/**
	 * Get the election system information
	 * @return the election system information
	 * @throws ElectionBoardServiceFault  
	 */
	public ElectionSystemInfo getElectionSystemInfo() throws ElectionBoardServiceFault {
		if(sysInfo == null)
			sysInfo = eb.getElectionSystemInfo();
			
		return sysInfo;
	}
	
	/**
	 * Get the election definition
	 * @return the election definition
	 * @throws ElectionBoardServiceFault  
	 */
	public ElectionDefinition getElectionDefinition() throws ElectionBoardServiceFault {
		if(elDef == null)
			elDef = eb.getElectionDefinition(eID);
		
		return elDef;
	}
	
	/**
	 * Get the encryption parameters
	 * @return the encryption parameters
	 * @throws ElectionBoardServiceFault  
	 */
	public  EncryptionParameters getEncryptionParameters() throws ElectionBoardServiceFault {
		if(encParam == null)
			encParam = eb.getEncryptionParameters(eID);
		
		return encParam;
	}
	
	/**
	 * Get the encryption key share of tallierID
	 * @param tallierID 
	 * @return the encryption key share of tallierID
	 * @throws ElectionBoardServiceFault  
	 */
	public  EncryptionKeyShare getEncryptionKeyShare(String tallierID) throws ElectionBoardServiceFault {
		if(encKeyShare == null)
			encKeyShare = eb.getEncryptionKeyShare(eID, tallierID);
		
		return encKeyShare;
	}
	
	/**
	 * Get the encryption key
	 * @return the encryption key
	 * @throws ElectionBoardServiceFault  
	 */
	public  EncryptionKey getEncryptionKey() throws ElectionBoardServiceFault {
		if(encKey == null)
			encKey = eb.getEncryptionKey(eID);
		
		return encKey;
	}
	
	/**
	 * Get the blinded generator of a given mixerID
	 * @param mixerID 
	 * @return the blinded generator of mixerID
	 * @throws ElectionBoardServiceFault  
	 */
	public  BlindedGenerator getBlindedGenerator(String mixerID) throws ElectionBoardServiceFault {
		if(blindGen == null)
			blindGen = eb.getBlindedGenerator(eID, mixerID);
		
		return blindGen;
	}
	
	/**
	 * Get the election generator
	 * @return the election generator
	 * @throws ElectionBoardServiceFault  
	 */
	public  ElectionGenerator getElectionGenerator() throws ElectionBoardServiceFault {
		if(elGen == null)
			elGen = eb.getElectionGenerator(eID);
		
		return elGen;
	}
	
	/**
	 * Get the election option
	 * @return the election option
	 * @throws ElectionBoardServiceFault  
	 */
	public  ElectionOptions getElectionOptions() throws ElectionBoardServiceFault {
		if(elOpt == null)
			elOpt = eb.getElectionOptions(eID);
		
		return elOpt;
	}
	
	/**
	 * Get the election data
	 * @return the election data
	 * @throws ElectionBoardServiceFault  
	 */
	public  ElectionData getElectionData() throws ElectionBoardServiceFault {
		if(elData == null)
			elData = eb.getElectionData(eID);
		
		return elData;
	}
	
	/**
	 * Get the electoral roll
	 * @return the electoral roll
	 * @throws ElectionBoardServiceFault  
	 */
	public  ElectoralRoll getElectoralRoll() throws ElectionBoardServiceFault {
		if(elRoll == null)
			elRoll = eb.getElectoralRoll(eID);
		
		return elRoll;
	}
	
	/**
	 * Get the voter certificates
	 * @return the voter certificates
	 * @throws ElectionBoardServiceFault  
	 */
	public  VoterCertificates getVoterCerts() throws ElectionBoardServiceFault {
		if(voterCerts == null)
			voterCerts = eb.getVoterCertificates(eID);
		
		return voterCerts;
	}
	
	/**
	 * @param mixerID 
	 * @return the mixVerKeyBy
	 * @throws ElectionBoardServiceFault  
	 */
	public  MixedVerificationKeys getMixedVerificationKeysBy(String mixerID) throws ElectionBoardServiceFault {
		if(mixVerKeyBy == null)
			mixVerKeyBy = eb.getVerificationKeysMixedBy(eID, mixerID);
		
		return mixVerKeyBy;
	}
	
	/**
	 * Get the mixed verification keys
	 * @return the mixed verification keys
	 * @throws ElectionBoardServiceFault  
	 */
	public  MixedVerificationKeys getMixedVerificationKeys() throws ElectionBoardServiceFault {
		if(mixVerKey == null)
			mixVerKey = eb.getMixedVerificationKeys(eID);
		
		return mixVerKey;
	}
	
	/**
	 * Get the lately registered voter certificate
	 * @return the lately registered voter certificate
	 * @throws ElectionBoardServiceFault  
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
	 * @throws ElectionBoardServiceFault  
	 */
	public  List<MixedVerificationKey> getLatelyMixedVerificationKeysBy(String mixerID) throws ElectionBoardServiceFault {
		if(latelyMixVerKeyBy == null)
			latelyMixVerKeyBy = eb.getVerificationKeysLatelyMixedBy(eID, mixerID);
		
		return latelyMixVerKeyBy;
	}
	
	/**
	 * Get the lately mixed verification key
	 * @return the lately mixed verification key
	 * @throws ElectionBoardServiceFault  
	 */
	public  List<MixedVerificationKey> getLateyMixedVerificationKeys() throws ElectionBoardServiceFault {
		if(latelyMixVerKey == null)
			latelyMixVerKey = eb.getLatelyMixedVerificationKeys(eID);
		
		return latelyMixVerKey;
	}
	
	/**
	 * Get the ballots
	 * @return the ballots
	 * @throws ElectionBoardServiceFault  
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
	 * @throws ElectionBoardServiceFault  
	 */
	public  MixedEncryptedVotes getMixedEncryptedVotesBy(String mixerID) throws ElectionBoardServiceFault {
		if(mixEncVotesBy == null)
			mixEncVotesBy = eb.getEncryptedVotesMixedBy(eID, mixerID);
		
		return mixEncVotesBy;
	}
	
	/**
	 * Get the mixed encrypted votes
	 * @return the mixed encrypted votes
	 * @throws ElectionBoardServiceFault  
	 */
	public  MixedEncryptedVotes getMixedEncryptedVotes() throws ElectionBoardServiceFault {
		if(mixEncVotes == null)
			mixEncVotes = eb.getMixedEncryptedVotes(eID);
		
		return mixEncVotes;
	}
	
	/**
	 * Get the partially decrypted votes
	 * @param tallierID 
	 * @return the partially decrypted votes
	 * @throws ElectionBoardServiceFault  
	 */
	public  PartiallyDecryptedVotes getPartiallyDecryptedVotes(String tallierID) throws ElectionBoardServiceFault {
		if(parDecVotes == null)
			parDecVotes = eb.getPartiallyDecryptedVotes(eID, tallierID);
		
		return parDecVotes;
	}
	
	/**
	 * Get the decrypted votes
	 * @return the decrypted votes
	 * @throws ElectionBoardServiceFault  
	 */
	public  DecryptedVotes getDecryptedVotes() throws ElectionBoardServiceFault {
		if(decryptedVotes == null)
			decryptedVotes = eb.getDecryptedVotes(eID);
		
		return decryptedVotes;
	}
	
	/**
	 * Get the decoded votes
	 * @return the decoded votes
	 * @throws ElectionBoardServiceFault  
	 */
	public  DecodedVotes getDecodedVotes() throws ElectionBoardServiceFault {
		if(decodedVotes == null)
			decodedVotes = eb.getDecodedVotes(eID);
		
		return decodedVotes;
	}
	
	/**
	 * Get a ballot
	 * @param verificationKey the verification key for this ballot
	 * @return the ballot
	 * @throws ElectionBoardServiceFault
	 */
	public Ballot getBallot(BigInteger verificationKey) throws ElectionBoardServiceFault{
		return eb.getBallot(eID, verificationKey);
	}

	
	
}
