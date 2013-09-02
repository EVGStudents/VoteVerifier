/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
 *
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
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.EncryptionParameters;
import ch.bfh.univote.common.KnownElectionIds;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.SignatureParameters;
import ch.bfh.univote.common.VerificationKeys;
import ch.bfh.univote.common.VoterCertificates;
import ch.bfh.univote.election.ElectionBoard;
import ch.bfh.univote.election.ElectionBoardService;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.x500.X500Principal;

/**
 * This class is used as a proxy for the ElectionBoard of UniVote. It is used as
 * a connection point in order to download the relative data needed to perform
 * verifications.
 *
 * @author Scalzi Giuseppe
 */
public class ElectionBoardProxy {

	private final String eID;
	private URL wsdlURL;
	private static final Logger LOGGER =
        Logger.getLogger(ElectionBoardProxy.class.getName());
	private ElectionBoard eb;
	/**
	 * These instance variables store the data from the web services.
	 */
	private Ballot ballot;
	private Ballots ballots;
	private Map<String, BlindedGenerator> blindGen;
	private DecodedVotes decodedVotes;
	private DecryptedVotes decryptedVotes;
	private ElectionData elData;
	private ElectionDefinition elDef;
	private ElectionGenerator elGen;
	private ElectionOptions elOpt;
	private ElectoralRoll elRoll;
	private ElectionSystemInfo elSysInfo;
	private EncryptionKey encKey;
	private Map<String, EncryptionKeyShare> encKeyShare;
	private EncryptionParameters encParam;
	private List<MixedVerificationKey> latelyMixVerKey;
	private Map<String, List<MixedVerificationKey>> latelyMixVerKeyBy;
	private List<Certificate> latelyRegVoteCerts;
	private EncryptedVotes encVotes;
	private Map<String, MixedEncryptedVotes> mixEncVotesBy;
	private VerificationKeys verKey;
	private Map<String, MixedVerificationKeys> mixVerKeyBy;
	private Map<String, PartiallyDecryptedVotes> parDecVotes;
	private Certificate rootCert;
	private SignatureParameters signParam;
	private VoterCertificates voterCerts;
	private Map<String, X509Certificate> talliersCerts;
	private Map<String, X509Certificate> mixersCerts;
	private KnownElectionIds electionIds;
	private List<X509Certificate> revokedCertificates;

	/**
	 * Construct an ElectionBoardProxy with a given election id.
	 *
	 * @param eID the ID of the election.
	 */
	public ElectionBoardProxy(String eID) {
		this.eID = eID;

		try {
			this.wsdlURL = new URL(Config.wsdlLocation);
		} catch (MalformedURLException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}

		getElectionBoard();
	}

	/**
	 * Construct an ElectionBoardProxy with a given election id and a URL as
	 * a source of the web services.
	 *
	 * @param eID the ID of the election.
	 * @param wsdlURL the URL of the WSDL file.
	 */
	public ElectionBoardProxy(String eID, String wsdlURL) {
		this.eID = eID;

		try {
			this.wsdlURL = new URL(wsdlURL);
		} catch (MalformedURLException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}

		getElectionBoard();
	}

	/**
	 * Construct an ElectionBoardProxy. This constructor will load the
	 * locally saved data previously downloaded from the univote election
	 * board.
	 *
	 * USE FOR TEST ONLY!
	 *
	 * @param electionID the electionID
	 * @param test if true the local data will be used. If false do nothing.
	 * @throws FileNotFoundException if the files where the election data
	 * are stored cannot be found.
	 */
	public ElectionBoardProxy(String electionID, boolean test) throws FileNotFoundException {
		//this eID must correspond to the suffix in the name of the XML file
		this.eID = electionID;

		if (test) {
			readElectionDataFromXML();
		}
	}

	/**
	 * Used to instantiate the ElectionBoardPort from where we get the data
	 * of an election.
	 */
	private void getElectionBoard() {
		ElectionBoardService ebs = new ElectionBoardService(wsdlURL);
		eb = ebs.getElectionBoardPort();
	}

	/**
	 * Read the relative data for the generated web service artifacts from
	 * XML files instead of from the network.
	 *
	 * @throws FileNotFoundException if the files where the election data
	 * are stored cannot be found.
	 *
	 */
	private void readElectionDataFromXML() throws FileNotFoundException {
		XStream xstream = new XStream();
		String dataPath = "src/test/java/ch/bfh/univoteverifier/testresources/";
		String EXT = ".xml";
		String eIDSeparator = "-" + eID;

		//read the object from the XML file and store it in the relative object
		this.ballot = (Ballot) xstream.fromXML(new FileInputStream(dataPath + "SingleBallot" + eIDSeparator + EXT));
		this.ballots = (Ballots) xstream.fromXML(new FileInputStream(dataPath + "Ballots" + eIDSeparator + EXT));
		this.blindGen = (Map<String, BlindedGenerator>) xstream.fromXML(new FileInputStream(dataPath + "BlindedGenerator" + eIDSeparator + EXT));
		this.decodedVotes = (DecodedVotes) xstream.fromXML(new FileInputStream(dataPath + "DecodedVotes" + eIDSeparator + EXT));
		this.decryptedVotes = (DecryptedVotes) xstream.fromXML(new FileInputStream(dataPath + "DecryptedVotes" + eIDSeparator + EXT));
		this.elData = (ElectionData) xstream.fromXML(new FileInputStream(dataPath + "ElectionData" + eIDSeparator + EXT));
		this.elDef = (ElectionDefinition) xstream.fromXML(new FileInputStream(dataPath + "ElectionDefinition" + eIDSeparator + EXT));
		this.elGen = (ElectionGenerator) xstream.fromXML(new FileInputStream(dataPath + "ElectionGenerator" + eIDSeparator + EXT));
		this.elOpt = (ElectionOptions) xstream.fromXML(new FileInputStream(dataPath + "ElectionOptions" + eIDSeparator + EXT));
		this.elRoll = (ElectoralRoll) xstream.fromXML(new FileInputStream(dataPath + "ElectoralRoll" + eIDSeparator + EXT));
		this.elSysInfo = (ElectionSystemInfo) xstream.fromXML(new FileInputStream(dataPath + "ElectionSystemInfo" + eIDSeparator + EXT));
		this.encKey = (EncryptionKey) xstream.fromXML(new FileInputStream(dataPath + "EncryptionKey" + eIDSeparator + EXT));
		this.encKeyShare = (Map<String, EncryptionKeyShare>) xstream.fromXML(new FileInputStream(dataPath + "EncryptionKeyShare" + eIDSeparator + EXT));
		this.encParam = (EncryptionParameters) xstream.fromXML(new FileInputStream(dataPath + "EncryptionParameters" + eIDSeparator + EXT));
		this.latelyMixVerKey = (List<MixedVerificationKey>) xstream.fromXML(new FileInputStream(dataPath + "LatelyMixedVerificationKeys" + eIDSeparator + EXT));
		this.latelyMixVerKeyBy = (Map<String, List<MixedVerificationKey>>) xstream.fromXML(new FileInputStream(dataPath + "LatelyMixedVerificationKeysBy" + eIDSeparator + EXT));
		this.latelyRegVoteCerts = (List<Certificate>) xstream.fromXML(new FileInputStream(dataPath + "LatelyRegisteredVoterCerts" + eIDSeparator + EXT));
		this.encVotes = (EncryptedVotes) xstream.fromXML(new FileInputStream(dataPath + "EncryptedVotes" + eIDSeparator + EXT));
		this.mixEncVotesBy = (Map<String, MixedEncryptedVotes>) xstream.fromXML(new FileInputStream(dataPath + "MixedEncryptedVotesBy" + eIDSeparator + EXT));
		this.verKey = (VerificationKeys) xstream.fromXML(new FileInputStream(dataPath + "MixedVerificationKeys" + eIDSeparator + EXT));
		this.mixVerKeyBy = (Map<String, MixedVerificationKeys>) xstream.fromXML(new FileInputStream(dataPath + "MixedVerificationKeysBy" + eIDSeparator + EXT));
		this.parDecVotes = (Map<String, PartiallyDecryptedVotes>) xstream.fromXML(new FileInputStream(dataPath + "PartiallyDecryptedVotes" + eIDSeparator + EXT));
//		this.rootCert = (Certificate) xstream.fromXML(new FileInputStream(dataPath + "RootCertificate" + eIDSeparator + EXT));
		this.signParam = (SignatureParameters) xstream.fromXML(new FileInputStream(dataPath + "SignatureParameters" + eIDSeparator + EXT));
		this.voterCerts = (VoterCertificates) xstream.fromXML(new FileInputStream(dataPath + "VoterCerts" + eIDSeparator + EXT));
	}

	/**
	 * Get a ballot.
	 *
	 * @param verificationKey the verification key for this ballot.
	 * @return the ballot.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public Ballot getBallot(BigInteger verificationKey) throws ElectionBoardServiceFault {
		if (eb != null) {//when we test using local data, eb is null
            // Eric Dubuis: Added try/catch clause to log exception.
            try {
                ballot = eb.getBallot(eID, verificationKey);
                return ballot;
            } catch (ElectionBoardServiceFault ex) {
                LOGGER.log(Level.SEVERE, "Caught ElectionBoardServiceFault, message = {0}",
                    ex.getMessage());
                throw ex;
            }
		} else {//so look in the ballots
			for (Ballot b : getBallots().getBallot()) {
				if (b.getVerificationKey().equals(verificationKey)) {
					return ballot;
				}
			}
		}

		//maybe change this - is not so good to return null
		return null;
	}

	/**
	 * Get the ballots.
	 *
	 * @return the ballots.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public Ballots getBallots() throws ElectionBoardServiceFault {
		if (ballots == null) {
			ballots = eb.getBallots(eID);
		}

		return ballots;
	}

	/**
	 * Get the blinded generator of a given mixerID.
	 *
	 * @param mixerID the name of the mixer.
	 * @return the blinded generator of mixerID.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public BlindedGenerator getBlindedGenerator(String mixerID) throws ElectionBoardServiceFault {
		if (blindGen == null) {
			blindGen = new HashMap<>();
		}

		if (blindGen.get(mixerID) == null) {
			blindGen.put(mixerID, eb.getBlindedGenerator(eID, mixerID));
		}

		return blindGen.get(mixerID);
	}

	/**
	 * Get the decoded votes.
	 *
	 * @return the decoded votes.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public DecodedVotes getDecodedVotes() throws ElectionBoardServiceFault {
		if (decodedVotes == null) {
			decodedVotes = eb.getDecodedVotes(eID);
		}

		return decodedVotes;
	}

	/**
	 * Get the decrypted votes.
	 *
	 * @return the decrypted votes.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public DecryptedVotes getDecryptedVotes() throws ElectionBoardServiceFault {
		if (decryptedVotes == null) {
			decryptedVotes = eb.getDecryptedVotes(eID);
		}

		return decryptedVotes;
	}

	/**
	 * Get the election data.
	 *
	 * @return the election data.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public ElectionData getElectionData() throws ElectionBoardServiceFault {
		if (elData == null) {
			elData = eb.getElectionData(eID);
		}

		return elData;
	}

	/**
	 * Get the election definition.
	 *
	 * @return the election definition.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public ElectionDefinition getElectionDefinition() throws ElectionBoardServiceFault {
		if (elDef == null) {
			elDef = eb.getElectionDefinition(eID);
		}

		return elDef;
	}

	/**
	 * Get the election generator.
	 *
	 * @return the election generator.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public ElectionGenerator getElectionGenerator() throws ElectionBoardServiceFault {
		if (elGen == null) {
			elGen = eb.getElectionGenerator(eID);
		}

		return elGen;
	}

	/**
	 * Get the election option.
	 *
	 * @return the election option.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public ElectionOptions getElectionOptions() throws ElectionBoardServiceFault {
		if (elOpt == null) {
			elOpt = eb.getElectionOptions(eID);
		}

		return elOpt;
	}

	/**
	 * Get the election system information.
	 *
	 * @return the election system information.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public ElectionSystemInfo getElectionSystemInfo() throws ElectionBoardServiceFault {
		if (elSysInfo == null) {
			elSysInfo = eb.getElectionSystemInfo(eID);
		}

		return elSysInfo;
	}

	/**
	 * Get the encryption key.
	 *
	 * @return the encryption key.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public EncryptionKey getEncryptionKey() throws ElectionBoardServiceFault {
		if (encKey == null) {
			encKey = eb.getEncryptionKey(eID);
		}

		return encKey;
	}

	/**
	 * Get the encryption parameters.
	 *
	 * @return the encryption parameters.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public EncryptionParameters getEncryptionParameters() throws ElectionBoardServiceFault {
		if (encParam == null) {
			encParam = eb.getEncryptionParameters(eID);
		}

		return encParam;
	}

	/**
	 * Get the encryption key share of tallierID.
	 *
	 * @param tallierID the ID of the tallier.
	 * @return the encryption key share of tallierID.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public EncryptionKeyShare getEncryptionKeyShare(String tallierID) throws ElectionBoardServiceFault {
		if (encKeyShare == null) {
			encKeyShare = new HashMap<>();
		}

		if (encKeyShare.get(tallierID) == null) {
			encKeyShare.put(tallierID, eb.getEncryptionKeyShare(eID, tallierID));
		}

		return encKeyShare.get(tallierID);
	}

	/**
	 * Get the electoral roll.
	 *
	 * @return the electoral roll.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public ElectoralRoll getElectoralRoll() throws ElectionBoardServiceFault {
		if (elRoll == null) {
			elRoll = eb.getElectoralRoll(eID);
		}

		return elRoll;
	}

	/**
	 * Get the lately mixed verification key.
	 *
	 * @return the lately mixed verification key.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public List<MixedVerificationKey> getLateyMixedVerificationKeys() throws ElectionBoardServiceFault {
		if (latelyMixVerKey == null) {
			latelyMixVerKey = eb.getLatelyMixedVerificationKeys(eID);
		}

		return latelyMixVerKey;
	}

	/**
	 * Get the lately mixed verification key mixed by mixerID.
	 *
	 * @param mixerID the ID of a given mixer.
	 * @return the lately mixed verification key of mixerID.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public List<MixedVerificationKey> getLatelyMixedVerificationKeysBy(String mixerID) throws ElectionBoardServiceFault {
		if (latelyMixVerKeyBy == null) {
			latelyMixVerKeyBy = new HashMap<>();
		}

		if (latelyMixVerKeyBy.get(mixerID) == null) {
			latelyMixVerKeyBy.put(mixerID, eb.getVerificationKeysLatelyMixedBy(eID, mixerID));
		}

		return latelyMixVerKeyBy.get(mixerID);
	}

	/**
	 * Get the lately registered voter certificate.
	 *
	 * @return the lately registered voter certificate.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public List<Certificate> getLatelyRegisteredVoterCerts() throws ElectionBoardServiceFault {
		if (latelyRegVoteCerts == null) {
			latelyRegVoteCerts = eb.getLatelyRegisteredVoterCertificates(eID);
		}

		return latelyRegVoteCerts;
	}

	/**
	 * Get the mixed encrypted votes.
	 *
	 * @return the mixed encrypted votes.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public EncryptedVotes getEncryptedVotes() throws ElectionBoardServiceFault {
		if (encVotes == null) {
			encVotes = eb.getEncryptedVotes(eID);
		}

		return encVotes;
	}

	/**
	 * Get the mixed encrypted votes of a given mixer.
	 *
	 * @param mixerID the ID of a given mixer.
	 * @return the mixed encrypted votes of the given mixer.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public MixedEncryptedVotes getMixedEncryptedVotesBy(String mixerID) throws ElectionBoardServiceFault {
		if (mixEncVotesBy == null) {
			mixEncVotesBy = new HashMap<>();
		}

		if (mixEncVotesBy.get(mixerID) == null) {
			mixEncVotesBy.put(mixerID, eb.getEncryptedVotesMixedBy(eID, mixerID));
		}

		return mixEncVotesBy.get(mixerID);
	}

	/**
	 * Get the mixed verification keys.
	 *
	 * @return the mixed verification keys.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public VerificationKeys getMixedVerificationKeys() throws ElectionBoardServiceFault {
		if (verKey == null) {
			verKey = eb.getMixedVerificationKeys(eID);
		}

		return verKey;
	}

	/**
	 * Get the mixed verification key of a given mixer.
	 *
	 * @param mixerID the ID of the mixer.
	 * @return the mixed verification keys of the mixer.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public MixedVerificationKeys getMixedVerificationKeysBy(String mixerID) throws ElectionBoardServiceFault {
		if (mixVerKeyBy == null) {
			mixVerKeyBy = new HashMap<>();
		}


		if (mixVerKeyBy.get(mixerID) == null) {
			mixVerKeyBy.put(mixerID, eb.getVerificationKeysMixedBy(eID, mixerID));
		}

		return mixVerKeyBy.get(mixerID);
	}

	/**
	 * Get the partially decrypted votes.
	 *
	 * @param tallierID the ID of the tallier.
	 * @return the partially decrypted votes.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public PartiallyDecryptedVotes getPartiallyDecryptedVotes(String tallierID) throws ElectionBoardServiceFault {
		if (parDecVotes == null) {
			parDecVotes = new HashMap<>();
		}

		if (parDecVotes.get(tallierID) == null) {
			parDecVotes.put(tallierID, eb.getPartiallyDecryptedVotes(eID, tallierID));
		}

		return parDecVotes.get(tallierID);
	}

	/**
	 * Get the root certificate.
	 *
	 * @return the root certificate.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public Certificate getRootCertificate() throws ElectionBoardServiceFault {
		if (rootCert == null) {
			rootCert = eb.getRootCertificate();
		}

		return rootCert;
	}

	/**
	 * Get the signature parameters.
	 *
	 * @return the signature parameters.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public SignatureParameters getSignatureParameters() throws ElectionBoardServiceFault {
		if (signParam == null) {
			signParam = eb.getSignatureParameters();
		}

		return signParam;
	}

	/**
	 * Get the voter certificates.
	 *
	 * @return the voter certificates.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public VoterCertificates getVoterCerts() throws ElectionBoardServiceFault {
		if (voterCerts == null) {
			voterCerts = eb.getVoterCertificates(eID);
		}

		return voterCerts;
	}

	/**
	 * Get the X509 Certificate of the CA.
	 *
	 * @return the X509Certificate of the CA.
	 * @throws CertificateException if there are problem with the instance
	 * of the certificate factory.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public X509Certificate getCACert() throws CertificateException, ElectionBoardServiceFault {
		X509Certificate c = CryptoFunc.getX509Certificate(getElectionSystemInfo().getCertificateAuthority().getValue());

		return c;
	}

	/**
	 * Get the X509 Certificate of the EM.
	 *
	 * @return the X509Certificate of the EM.
	 * @throws CertificateException if there are problem with the instance
	 * of the certificate factory.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public X509Certificate getEMCert() throws CertificateException, ElectionBoardServiceFault {
		X509Certificate c = CryptoFunc.getX509Certificate(getElectionSystemInfo().getElectionManager().getValue());

		return c;
	}

	/**
	 * Get the X509 Certificate of the EA.
	 *
	 * @return the X509Certificate of the EA.
	 * @throws CertificateException if there are problem with the instance
	 * of the certificate factory.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public X509Certificate getEACert() throws ElectionBoardServiceFault, CertificateException {
		X509Certificate c = CryptoFunc.getX509Certificate(getElectionSystemInfo().getElectionAdministration().getValue());

		return c;
	}

	/**
	 * Get the X509 Certificates of the talliers.
	 *
	 * @return a map containing the name as key and the X509Certificates as
	 * object of the talliers.
	 * @throws CertificateException if there are problem with the instance
	 * of the certificate factory.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public Map<String, X509Certificate> getTalliersCerts() throws ElectionBoardServiceFault, CertificateException {
		if (talliersCerts == null) {
			talliersCerts = new HashMap<>();
			int counter = 0;

			for (Certificate cert : getElectionSystemInfo().getTallier()) {
				X509Certificate xCert = CryptoFunc.getX509Certificate(cert.getValue());
				talliersCerts.put(getElectionDefinition().getTallierId().get(counter), xCert);
				counter++;
			}
		}

		return talliersCerts;
	}

	/**
	 * Get the X509 Certificates of the mixers.
	 *
	 * @return a map containing the name as key and the X509Certificates as
	 * object of the mixers.
	 * @throws CertificateException if there are problem with the instance
	 * of the certificate factory.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public Map<String, X509Certificate> getMixersCerts() throws ElectionBoardServiceFault, CertificateException {
		if (mixersCerts == null) {
			mixersCerts = new HashMap<>();
			int counter = 0;

			for (Certificate cert : getElectionSystemInfo().getMixer()) {
				X509Certificate xCert = CryptoFunc.getX509Certificate(cert.getValue());
				mixersCerts.put(getElectionDefinition().getMixerId().get(counter), xCert);
				counter++;
			}
		}

		return mixersCerts;
	}

	/**
	 * Get all the known election Ids.
	 *
	 * @return the known election Ids.
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 */
	public KnownElectionIds getElectionsID() throws ElectionBoardServiceFault {
		if (electionIds == null) {
			electionIds = eb.getKnownElectionIds();
		}

		return electionIds;
	}

	/**
	 * Get the revoked certificates.
	 *
	 * @return the list o revoked certificates
	 * @throws ElectionBoardServiceFault if there is a problem with the
	 * public board such as a network connection problem or a wrong
	 * parameter.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory cannot be found.
	 */
	public List<X509Certificate> getRevokedCertificates() throws ElectionBoardServiceFault, CertificateException {
		if (revokedCertificates == null) {
			List<X509Certificate> allCert = new ArrayList<>();
			revokedCertificates = new ArrayList<>();
			Map<X500Principal, List<X509Certificate>> certsPerVoter = new HashMap<>();

			//add voter certs
			for (Certificate c : getVoterCerts().getCertificate()) {
				allCert.add(CryptoFunc.getX509Certificate(c.getValue()));
			}

			//add lately registered voters certs - ToDo decommetn when it will be available
//			for (VoterCertificate c : getLatelyRegisteredVoterCerts()) {
//				allCert.add(CryptoFunc.getX509Certificate(c.getCertificate().getValue(), false));
//			}

			//now take all the certificates of one voter and build a map "subject => certificate list"
			for (X509Certificate c : allCert) {
				X500Principal certSubject = c.getSubjectX500Principal();

				if (certsPerVoter.get(certSubject) == null) {
					certsPerVoter.put(certSubject, new ArrayList());
				}

				//get the actual list and add the certificate for this subject
				List<X509Certificate> certList = certsPerVoter.get(certSubject);
				certList.add(c);
				certsPerVoter.put(certSubject, certList);
			}

			//remove the most recent
			for (Entry<X500Principal, List<X509Certificate>> e : certsPerVoter.entrySet()) {
				List<X509Certificate> subjectCerts = e.getValue();
				X509Certificate moreRecent = subjectCerts.get(0);

				for (int i = 1; i < subjectCerts.size(); i++) {
					if (subjectCerts.get(i).getNotBefore().after(moreRecent.getNotBefore())) {
						moreRecent = subjectCerts.get(i);
					}
				}

				//delete the most recent
				subjectCerts.remove(moreRecent);
				certsPerVoter.put(e.getKey(), subjectCerts);
			}

			//finally build the list of revoked certs
			for (Entry<X500Principal, List<X509Certificate>> e : certsPerVoter.entrySet()) {
				revokedCertificates.addAll(e.getValue());
			}
		}

		return revokedCertificates;
	}

	/**
	 * Get the election ID.
	 *
	 * @return the election ID of this ElectionBoardProxy.
	 */
	public String getElectionID() {
		return this.eID;
	}
}
