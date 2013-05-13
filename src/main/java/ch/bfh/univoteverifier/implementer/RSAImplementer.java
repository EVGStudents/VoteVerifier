/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.implementer;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.ElectionGenerator;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ElectoralRoll;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.ForallRule;
import ch.bfh.univote.common.LocalizedText;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.Rule;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.SummationRule;
import ch.bfh.univote.common.VoterCertificates;
import ch.bfh.univote.common.VoterSignature;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.StringConcatenator;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;
import javax.naming.InvalidNameException;

/**
 * This class contains all the methods that need a RSA Verification.
 *
 * @author snake
 */
public class RSAImplementer extends Implementer {

	private final RSAPublicKey emPubKey;
	private final RSAPublicKey eaPubKey;
	private final Map<String, X509Certificate> talliersCerts;
	private final Map<String, X509Certificate> mixersCerts;

	/**
	 * Create a new RSAImplementer.
	 *
	 * @param ebp the election board proxy from where get the data.
	 */
	public RSAImplementer(ElectionBoardProxy ebp, RunnerName rn) throws CertificateException, ElectionBoardServiceFault, InvalidNameException {
		super(ebp, rn);
		emPubKey = (RSAPublicKey) ebp.getEMCert().getPublicKey();
		eaPubKey = (RSAPublicKey) ebp.getEACert().getPublicKey();
		talliersCerts = ebp.getTalliersCerts();
		mixersCerts = ebp.getMixersCerts();
	}

	/**
	 * Verify a RSA signature.
	 *
	 * @param s the RSAPublicKey.
	 * @param clearText the text that has to be hashed and compared with the
	 * decrypted signature.
	 * @param signature the pre-computed signature from the board.
	 * @return true if the signature is verified correctly otherwise not.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public boolean vrfRSASign(RSAPublicKey pubKey, String clearText, BigInteger signature) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		BigInteger hash = CryptoFunc.sha256(clearText);

		//compute signature^e mod s, this must be equal to the hash we have computed
		BigInteger decSign = signature.modPow(pubKey.getPublicExponent(), pubKey.getModulus());

//		System.out.println("Public key: " + s);
//		System.out.println("Signature value: " + signature);
//		System.out.println("Hash:\t\t\t" + hash);
//		System.out.println("Decripted signature:\t" + decSign);
		System.out.println(clearText);
		System.out.println("===========");

		boolean result = decSign.equals(hash);

		return result;
	}

	/**
	 * Verify the signature of the election administrator certificate plus
	 * the election ID.
	 *
	 * Specification: 1.3.4, a.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory used in this verification cannot be found.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfEACertIDSign() throws ElectionBoardServiceFault, CertificateException, NoSuchAlgorithmException, UnsupportedEncodingException {
		//get the certificte as a string
		String eaCertStr = ebp.getCACert().toString();

		//get the election id
		String eID = ebp.getElectionDefinition().getElectionId();

		//concatenate to (id|Z_ea|timestamp)
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(eID, StringConcatenator.INNER_DELIMITER);
		//add the timestamp when we found where this signature is - ToDo
		//sc.pushObjectDelimiter(timestamp,StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter("CertToString", StringConcatenator.RIGHT_DELIMITER);

		String res = sc.pullAll();

		//find the signature of Page 13, Initialization, Step 3 - ToDO
		BigInteger signature = new BigInteger("1");

		boolean r = vrfRSASign(emPubKey, res, signature);

		return new VerificationResult(VerificationType.EL_SETUP_EA_CERT_ID_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM);
	}

	/**
	 * Verify the signature of the basic parameters (id, description, key
	 * length, talliers and mixers) of an election.
	 *
	 * Specification: 1.3.4, b.
	 *
	 * @returna a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfBasicParamSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		ElectionDefinition ed = ebp.getElectionDefinition();
		Signature signature = ed.getSignature();

		//get the data
		String eID = ed.getElectionId();
		String descr = ed.getTitle();
		String keyLength = String.valueOf(ed.getKeyLength());
		List< String> talliers = ed.getTallierId();
		List<String> mixers = ed.getMixerId();

		//concatenate to (id|descr|keyLength|(t_1|...|t_n)|(m_1|...|m_n))|timestamp
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(eID, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(descr, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(keyLength, StringConcatenator.INNER_DELIMITER);
		sc.pushList(talliers, true);
		sc.pushInnerDelim();
		sc.pushList(mixers, true);
		sc.pushRightDelim();
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp().toString());

		String res = sc.pullAll();

		//verify the signature
		boolean r = vrfRSASign(eaPubKey, res, signature.getValue());

		//create the VerificationResult
		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_BASICS_PARAMS_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EA);

		if (!r) {
			v.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return v;
	}

	/**
	 * Verify the signature of the talliers and mixers certificates with the
	 * election id.
	 *
	 * Specification: 1.3.4, b.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfTMCertsSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		//ToDo - change to the correct value when we find it.
		Signature signature = null;

		//concatenate to (id|(Z_t1|....|Z_tn)|(Z_m1|...|Z_mn)|timestamp)
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionDefinition().getElectionId(), StringConcatenator.INNER_DELIMITER);
		sc.pushList(ebp.getElectionSystemInfo().getTallier(), true);
		sc.pushInnerDelim();
		sc.pushList(ebp.getElectionSystemInfo().getMixer(), true);
		sc.pushInnerDelim();
		//get the timestamp when we will know where it is
		//sc.pushObjectDelimiter(timestamp, StringConcatenator.RIGHT_DELIMITER);

		String res = sc.pullAll();

		//verify the signature
//		boolean r = vrfRSASign(emPubKey, res, signature.getValue());
		boolean r = false;

		//create the VerificationResult
		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_T_CERT_M_CERT_ID_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM);

		if (!r) {
			v.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return v;
	}

	/**
	 * Verify the signature of the ElGamal parameters with the election id.
	 *
	 * Specification: 1.3.4, c.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfElGamalParamSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		Signature signature = ebp.getEncryptionParameters().getSignature();
		BigInteger P = ebp.getEncryptionParameters().getPrime();
		BigInteger Q = ebp.getEncryptionParameters().getGroupOrder();
		BigInteger G = ebp.getEncryptionParameters().getGenerator();

		//concatenate to (id|P|Q|G)|timestamp
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(P, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(Q, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(G, StringConcatenator.RIGHT_DELIMITER);
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp());

		String res = sc.pullAll();

		//verify the signature
		boolean r = vrfRSASign(emPubKey, res, signature.getValue());

		//create the VerificationResult
		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAMS_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM);

		if (!r) {
			v.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return v;
	}

	/**
	 * Verify the signature of the proof for the distributed key generation.
	 *
	 * Specification: 1.3.4, d.
	 *
	 * @param tallierName The name of the tallier.
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 *
	 */
	public VerificationResult vrfDistributedKeyBySign(String tallierName) throws NoSuchAlgorithmException, UnsupportedEncodingException, ElectionBoardServiceFault {
		EncryptionKeyShare eks = ebp.getEncryptionKeyShare(tallierName);
		Signature signature = eks.getSignature();

		//concatenate to (id|y_j|t|s)|timestamp
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(eks.getKey(), StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(eks.getProof().getCommitment().get(0), StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(eks.getProof().getResponse().get(0), StringConcatenator.RIGHT_DELIMITER);
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp());

		String res = sc.pullAll();

		//verifiy the signature
		RSAPublicKey tallierPubKey = (RSAPublicKey) talliersCerts.get(tallierName).getPublicKey();
		boolean r = vrfRSASign(tallierPubKey, res, signature.getValue());

		VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_T_NIZKP_OF_X_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.TALLIER);
		vr.setEntityName(tallierName);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}

	/**
	 * Verify the signature of the distributed key y.
	 *
	 * Specification: 1.3.4, d.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfDistributedKeySign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		EncryptionKey ek = ebp.getEncryptionKey();
		Signature signature = ek.getSignature();

		//concatenate to (id|y)|timestamp
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(ek.getKey(), StringConcatenator.RIGHT_DELIMITER);
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp());

		String res = sc.pullAll();

		//verify the signature
		boolean r = vrfRSASign(emPubKey, res, signature.getValue());

		VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_T_PUBLIC_KEY_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}

	/**
	 * Verify the signature of the proof for the election generator.
	 *
	 * Specification: 1.3.4, e.
	 *
	 * @param mixerName The name of the mixer.
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 *
	 */
	public VerificationResult vrfElectionGeneratorBySign(String mixerName) throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		BlindedGenerator bg = ebp.getBlindedGenerator(mixerName);
		Signature signature = bg.getSignature();

		//concatenate to (id|g_k|proof)|timestamp
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(bg.getGenerator(), StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(bg.getProof(), StringConcatenator.RIGHT_DELIMITER);
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp());

		String res = sc.pullAll();

		//verify the signature
		RSAPublicKey mixerPubKey = (RSAPublicKey) mixersCerts.get(mixerName).getPublicKey();
		boolean r = vrfRSASign(mixerPubKey, res, signature.getValue());

		VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_M_NIZKP_OF_ALPHA_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.MIXER);
		vr.setEntityName(mixerName);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}

	/**
	 * Verify the signature of the election generator.
	 *
	 * Specification: 1.3.4, e.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfElectionGeneratorSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		ElectionGenerator eg = ebp.getElectionGenerator();
		Signature signature = eg.getSignature();

		//concatenate to (id|g^)|timestamp
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(eg.getGenerator(), StringConcatenator.RIGHT_DELIMITER);
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp());

		String res = sc.pullAll();

		//verify the signature
		boolean r = vrfRSASign(emPubKey, res, signature.getValue());

		VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_ANON_GEN_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}

	/**
	 * Verify the signature of the election options.
	 *
	 * Specification: 1.3.5, a.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfElectionOptionsSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		ElectionOptions eo = ebp.getElectionOptions();
		Signature signature = eo.getSignature();

		//concatenate to (id|(c1|....|cn)|(r1|...|rn))|timestamp
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
		sc.pushList(eo.getChoice(), true);
		sc.pushInnerDelim();
		sc.pushList(eo.getRule(), true);
		sc.pushRightDelim();
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp());

		String res = sc.pullAll();

		//verify the signature
		boolean r = vrfRSASign(eaPubKey, res, signature.getValue());

		VerificationResult vr = new VerificationResult(VerificationType.EL_PREP_C_AND_R_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EA);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}

	/**
	 * Verify the signature of the election data.
	 *
	 * Specification: 1.3.5, b.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfElectionDataSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		ElectionData ed = ebp.getElectionData();
		Signature signature = ed.getSignature();
		String eaIdentifier = ebp.getElectionOptions().getSignature().getSignerId();

		//concatenate to (id|EA|descr|P|Q|G|y|g^|(c1|...|cn)|(r1|...|rn))|timestamp
//		sc.pushLeftDelim();
//		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
//		sc.pushObjectDelimiter(ed.getTitle(), StringConcatenator.INNER_DELIMITER);
//		sc.pushLeftDelim();
//
//		for (int i = 0; i < ed.getChoice().size(); i++) {
//			Choice c = ed.getChoice().get(i);
//
//			if (i > 0) {
//				sc.pushInnerDelim();
//			}
//
//			sc.pushLeftDelim();
//			sc.pushObjectDelimiter(c.getChoiceId(), StringConcatenator.INNER_DELIMITER);
//
//			if (c instanceof PoliticalList) {
//				PoliticalList pl = (PoliticalList) c;
//				sc.pushObjectDelimiter(pl.getNumber(), StringConcatenator.INNER_DELIMITER);
//
//				sc.pushLeftDelim();
//				for (int k = 0; k < pl.getTitle().size(); k++) {
//					LocalizedText lt = pl.getTitle().get(k);
//
//					if (k > 0) {
//						sc.pushInnerDelim();
//					}
//
//					sc.pushLeftDelim();
//					sc.pushObjectDelimiter(lt.getLanguage(), StringConcatenator.INNER_DELIMITER);
//					sc.pushObjectDelimiter(lt.getText(), StringConcatenator.RIGHT_DELIMITER);
//				}
//				sc.pushRightDelim();
//
//				sc.pushInnerDelim();
//
//				sc.pushLeftDelim();
//				for (int k = 0; k < pl.getPartyName().size(); k++) {
//					LocalizedText lt = pl.getPartyName().get(k);
//
//					if (k > 0) {
//						sc.pushInnerDelim();
//					}
//
//					sc.pushLeftDelim();
//					sc.pushObjectDelimiter(lt.getLanguage(), StringConcatenator.INNER_DELIMITER);
//					sc.pushObjectDelimiter(lt.getText(), StringConcatenator.RIGHT_DELIMITER);
//				}
//				sc.pushRightDelim();
//
//				sc.pushInnerDelim();
//
//				sc.pushLeftDelim();
//				for (int k = 0; k < pl.getPartyShortName().size(); k++) {
//					LocalizedText lt = pl.getPartyShortName().get(k);
//
//					if (k > 0) {
//						sc.pushInnerDelim();
//					}
//
//					sc.pushLeftDelim();
//					sc.pushObjectDelimiter(lt.getLanguage(), StringConcatenator.INNER_DELIMITER);
//					sc.pushObjectDelimiter(lt.getText(), StringConcatenator.RIGHT_DELIMITER);
//				}
//				sc.pushRightDelim();
//
//
//			} else if (c instanceof Candidate) {
//				Candidate can = (Candidate) c;
//				sc.pushObjectDelimiter(can.getNumber(), StringConcatenator.INNER_DELIMITER);
//				sc.pushObjectDelimiter(can.getLastName(), StringConcatenator.INNER_DELIMITER);
//				sc.pushObjectDelimiter(can.getFirstName(), StringConcatenator.INNER_DELIMITER);
//				sc.pushObjectDelimiter(can.getSex(), StringConcatenator.INNER_DELIMITER);
//				sc.pushObjectDelimiter(can.getYearOfBirth(), StringConcatenator.INNER_DELIMITER);
//
//				sc.pushLeftDelim();
//				for (int k = 0; k < can.getStudyBranch().size(); k++) {
//					LocalizedText lt = can.getStudyBranch().get(k);
//
//					if (k > 0) {
//						sc.pushInnerDelim();
//					}
//
//					sc.pushLeftDelim();
//					sc.pushObjectDelimiter(lt.getLanguage(), StringConcatenator.INNER_DELIMITER);
//					sc.pushObjectDelimiter(lt.getText(), StringConcatenator.RIGHT_DELIMITER);
//				}
//				sc.pushRightDelim();
//
//				sc.pushInnerDelim();
//
//				sc.pushLeftDelim();
//				for (int k = 0; k < can.getStudyDegree().size(); k++) {
//					LocalizedText lt = can.getStudyDegree().get(k);
//
//					if (k > 0) {
//						sc.pushInnerDelim();
//					}
//
//					sc.pushLeftDelim();
//					sc.pushObjectDelimiter(lt.getLanguage(), StringConcatenator.INNER_DELIMITER);
//					sc.pushObjectDelimiter(lt.getText(), StringConcatenator.RIGHT_DELIMITER);
//				}
//				sc.pushRightDelim();
//
//				sc.pushInnerDelim();
//
//				sc.pushObjectDelimiter(can.getSemesterCount(), StringConcatenator.INNER_DELIMITER);
//				sc.pushObjectDelimiter(can.getStatus(), StringConcatenator.INNER_DELIMITER);
//				sc.pushObjectDelimiter(can.getListId(), StringConcatenator.INNER_DELIMITER);
//				sc.pushObject(can.getYearOfBirth());
//			}
//
//			sc.pushRightDelim();
//		}
//
//		sc.pushRightDelim();
//		sc.pushInnerDelim();
//		sc.pushLeftDelim();
//
//		for (int j = 0; j < ed.getRule().size(); j++) {
//			Rule r = ed.getRule().get(j);
//			int lowerBound = 0, upperBound = 0;
//
//			if (j > 0) {
//				sc.pushInnerDelim();
//			}
//
//			sc.pushLeftDelim();
//
//			if (r instanceof SummationRule) {
//				lowerBound = ((SummationRule) r).getLowerBound();
//				upperBound = ((SummationRule) r).getUpperBound();
//				sc.pushObjectDelimiter("summationRule", StringConcatenator.INNER_DELIMITER);
//			} else if (r instanceof ForallRule) {
//				lowerBound = ((ForallRule) r).getLowerBound();
//				upperBound = ((ForallRule) r).getUpperBound();
//				sc.pushObjectDelimiter("forallRule", StringConcatenator.INNER_DELIMITER);
//			}
//
//			sc.pushLeftDelim();
//
//			for (int q = 0; q < r.getChoiceId().size(); q++) {
//				if (q != 0) {
//					sc.pushInnerDelim();
//				}
//
//				sc.pushObject(r.getChoiceId().get(q));
//			}
//			sc.pushRightDelim();
//
//			sc.pushInnerDelim();
//			sc.pushObjectDelimiter(lowerBound, StringConcatenator.INNER_DELIMITER);
//			sc.pushObjectDelimiter(upperBound, StringConcatenator.INNER_DELIMITER);
//
//			sc.pushRightDelim();
//		}
//
//		sc.pushRightDelim();
//		sc.pushInnerDelim();
//
//		sc.pushObjectDelimiter(ed.getPrime(), StringConcatenator.INNER_DELIMITER);
//		sc.pushObjectDelimiter(ed.getGroupOrder(), StringConcatenator.INNER_DELIMITER);
//		sc.pushObjectDelimiter(ed.getGenerator(), StringConcatenator.INNER_DELIMITER);
//
//		sc.pushObjectDelimiter(ed.getEncryptionKey(), StringConcatenator.INNER_DELIMITER);
//		sc.pushObject(ed.getElectionGenerator());
//		sc.pushRightDelim();
//		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp());

		String res = sc.pullAll();

		//verify the signature
		boolean r = vrfRSASign(emPubKey, res, signature.getValue());

		VerificationResult vr = new VerificationResult(VerificationType.EL_PREP_EDATA_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}

	/**
	 * Verify the signature of the electoral roll.
	 *
	 * Specification: 1.3.5, c.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfElectoralRollSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		ElectoralRoll er = ebp.getElectoralRoll();
		Signature signature = er.getSignature();

		//concatenate to (id|H_v)|timestamp
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
		sc.pushList(er.getVoterHash(), true);
		sc.pushRightDelim();
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp());

		String res = sc.pullAll();

		//verify the signature
		boolean r = vrfRSASign(eaPubKey, res, signature.getValue());

		VerificationResult vr = new VerificationResult(VerificationType.EL_PREP_ELECTORAL_ROLL_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EA);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}

	/**
	 * Verify the signature of the set of Voters certificates with the
	 * election ID.
	 *
	 * Specification: 1.3.5, c.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfVotersCertIDSign() throws ElectionBoardServiceFault {
		VoterCertificates vc = ebp.getVoterCerts();
		Signature signature = vc.getSignature();

		//ToDo - Find the signature
		VerificationResult vr = new VerificationResult(VerificationType.EL_PREP_VOTERS_CERT_SIGN, false, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.CA);
		return vr;
	}

	/**
	 * Verify the signature of the set of verification keys mixed by a given
	 * mixer.
	 *
	 * @param mixerName the name of the mixer.
	 *
	 * Specification: 1.3.5, d.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfMixedVerificationKeysBySign(String mixerName) throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		MixedVerificationKeys mk = ebp.getMixedVerificationKeysBy(mixerName);
		Signature signature = mk.getSignature();

		//concatenate to (id|(vk1|...|vkn)|timestamp
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
		sc.pushList(mk.getKey(), true);
		sc.pushRightDelim();
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp());

		String res = sc.pullAll();

		//verify the signature
		RSAPublicKey mixerPubKey = (RSAPublicKey) mixersCerts.get(mixerName).getPublicKey();
		boolean r = vrfRSASign(mixerPubKey, res, signature.getValue());

		VerificationResult vr = new VerificationResult(VerificationType.EL_PREP_M_PUB_VER_KEYS_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.MIXER);
		vr.setEntityName(mixerName);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}

	/**
	 * Verify the signature over the set of mixed verification keys.
	 *
	 * Specification: 1.3.5, d.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfMixedVerificationKeysSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		MixedVerificationKeys mk = ebp.getMixedVerificationKeys();
		Signature signature = mk.getSignature();

		//concatenate to (id|(vk1|...|vkn))|timestamp
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
		sc.pushList(mk.getKey(), true);
		sc.pushRightDelim();
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp());

		String res = sc.pullAll();

		//verify the signature
		boolean r = vrfRSASign(emPubKey, res, signature.getValue());

		VerificationResult vr = new VerificationResult(VerificationType.EL_PREP_PUB_VER_KEYS_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}

	/**
	 * Verify the signature of the election ID with the late registered
	 * voter certificate.
	 *
	 * Specification: 1.3.6, a.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfLatelyRegisteredVotersCertificateSign() {
		//ToDo - Find the signature

		VerificationResult vr = new VerificationResult(VerificationType.EL_PERIOD_LATE_NEW_VOTER_CERT_SIGN, false, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM);

		return vr;
	}

	/**
	 * Verify the signature of the lately verification keys with the proof.
	 * The proof is not included in the signature computation because is not
	 * yet available.
	 *
	 * Specification: 1.3.6, a.
	 *
	 * @param mixerName the name of the mixer
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfLatelyVerificationKeysBySign(String mixerName) throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		List<MixedVerificationKey> mvk = ebp.getLatelyMixedVerificationKeysBy(mixerName);
		RSAPublicKey mixerPubKey = (RSAPublicKey) mixersCerts.get(mixerName).getPublicKey();
		boolean r = false;

		for (MixedVerificationKey key : mvk) {
			Signature signature = key.getSignature();

			//concatenate to (id|vk|proof)|timestamp - WARNING: The proof is not yet implemented, so we exclude it.
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(key.getKey(), StringConcatenator.RIGHT_DELIMITER);
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			r = vrfRSASign(mixerPubKey, res, signature.getValue());

			if (!r) {
				break;
			}
		}

		VerificationResult vr = new VerificationResult(VerificationType.EL_PERIOD_M_NIZKP_EQUALITY_NEW_VRF_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.MIXER);
		vr.setEntityName(mixerName);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}

	/**
	 *
	 * Verify the signature of the verification keys of the lately mixed
	 * verification set.
	 *
	 * Specification: 1.3.6, a.
	 *
	 * @param mixerName the name of the mixer
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfLatelyVerificationKeysSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		List<MixedVerificationKey> mk = ebp.getLateyMixedVerificationKeys();
		boolean r = false;

		//check the signature for each key in the set
		for (MixedVerificationKey key : mk) {
			Signature signature = key.getSignature();

			//concatenate to (id|key)|timestamp - ToDo look for the Voter identiy
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(key.getKey(), StringConcatenator.RIGHT_DELIMITER);
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			r = vrfRSASign(emPubKey, res, signature.getValue());

			if (!r) {
				break;
			}
		}

		VerificationResult vr = new VerificationResult(VerificationType.EL_PERIOD_NEW_VER_KEY_SIGN, r, ebp.getElectionID(), rn, ImplementerType.RSA, EntityType.EM);

		if (!r) {
			vr.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return vr;
	}
}
