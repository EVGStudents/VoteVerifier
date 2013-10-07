/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of Applied Sciences, Engineering and
 * Information Technology, Research Institute for Security in the Information Society, E-Voting Group, Biel,
 * Switzerland.
 *
 * Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.implementer;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.Candidate;
import ch.bfh.univote.common.Certificate;
import ch.bfh.univote.common.Choice;
import ch.bfh.univote.common.DecodedVote;
import ch.bfh.univote.common.DecodedVoteEntry;
import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.ElectionData;
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.ElectionGenerator;
import ch.bfh.univote.common.ElectionOptions;
import ch.bfh.univote.common.ElectoralRoll;
import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.ForallRule;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.common.PoliticalList;
import ch.bfh.univote.common.Rule;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.common.SummationRule;
import ch.bfh.univote.common.VerificationKeys;
import ch.bfh.univote.common.VoterCertificates;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Report;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.StringConcatenator;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * This class contains all the methods that need a RSA Verification.
 *
 * @author Scalzi Giuseppe
 */
public class RSAImplementer extends Implementer {

	/**
	 * Create a new RSAImplementer.
	 *
	 * @param ebp the election board proxy from where get the data.
	 */
	public RSAImplementer(ElectionBoardProxy ebp, RunnerName rn) {
		super(ebp, rn, ImplementerType.RSA);
	}

	/**
	 * Verify a RSA signature.
	 *
	 * @param s the RSAPublicKey.
	 * @param clearText the text that has to be hashed and compared with the decrypted signature.
	 * @param signature the pre-computed signature from the board.
	 * @return true if the signature is verified correctly otherwise not.
	 */
	public boolean vrfRSASign(RSAPublicKey pubKey, String clearText, BigInteger signature) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		BigInteger hash = CryptoFunc.sha256(clearText);
		//TODO Remove
		String ha = hash.toString(10);
		String pk = pubKey.getPublicExponent().toString(10);
		String mod = pubKey.getModulus().toString(10);
		String sig = signature.toString(10);
		//compute signature^e mod s, this must be equal to the hash we have computed
		BigInteger decSign = signature.modPow(pubKey.getPublicExponent(), pubKey.getModulus());

		String decsig = decSign.toString(10);
		boolean result = decSign.equals(hash);

		return result;
	}

	/**
	 * Verify the signature of the election administrator certificate plus the election ID.
	 *
	 * Specification: 1.3.4, a.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public board, such as a wrong parameter or a
	 * network connection problem.
	 */
	public VerificationResult vrfEACertIDSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
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

			r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature);
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_SETUP_EA_CERT_ID_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);
		v.setImplemented(false);

		//create a report for the non-implemented
		rep = new Report(FailureCode.NOT_YET_IMPLEMENTED);
		v.setReport(rep);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		}


		return v;
	}

	/**
	 * Verify the signature of the basic parameters (id, description, key length, talliers and mixers) of an election.
	 *
	 * Specification: 1.3.4, b.
	 *
	 * @returna a VerificationResult.
	 */
	public VerificationResult vrfBasicParamSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			ElectionDefinition ed = ebp.getElectionDefinition();

			Signature signature = ed.getSignature();

			//concatenate to (id|descr|keyLength|(t_1|...|t_n)|(m_1|...|m_n))|timestamp
			sc.pushLeftDelim();

			sc.pushObjectDelimiter(ed.getElectionId(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(ed.getTitle(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(ed.getKeyLength(), StringConcatenator.INNER_DELIMITER);
			sc.pushList(ed.getTallierId(), true);
			sc.pushInnerDelim();
			sc.pushList(ed.getMixerId(), true);

			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());


			String res = sc.pullAll();

			//verify the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEACert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		//create the VerificationResult
		VerificationResult v =
            new VerificationResult(VerificationType.EL_SETUP_BASICS_PARAMS_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EA);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the talliers and mixers certificates with the election id.
	 *
	 * Specification: 1.3.4, b.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfTMCertsSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			//ToDo - change to the correct value when we find it.
			Signature signature = null;

			//concatenate to (id|(Z_t1|....|Z_tn)|(Z_m1|...|Z_mn))|timestamp
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(ebp.getElectionDefinition().getElectionId(), StringConcatenator.INNER_DELIMITER);
			sc.pushList(ebp.getElectionSystemInfo().getTallier(), true);
			sc.pushInnerDelim();
			sc.pushList(ebp.getElectionSystemInfo().getMixer(), true);
			sc.pushInnerDelim();
			sc.pushRightDelim();
			sc.pushInnerDelim();
			//get the timestamp when we will know where it is
			//sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			//boolean r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature.getValue());
			r = false;
		} catch (ElectionBoardServiceFault ex) {
			exc = ex;
		}

		//create the VerificationResult
		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_T_CERT_M_CERT_ID_SIGN, r, ebp.getElectionID(), rn, it, EntityType.EM);
		v.setImplemented(false);
		//create a report for the non-implemented
		rep = new Report(FailureCode.NOT_YET_IMPLEMENTED);
		v.setReport(rep);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		}
		return v;
	}

	/**
	 * Verify the signature of the ElGamal parameters with the election id.
	 *
	 * Specification: 1.3.4, c.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfElGamalParamSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
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
			r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		//create the VerificationResult
		VerificationResult v =
            new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAMS_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
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
	 */
	public VerificationResult vrfDistributedKeyBySign(String tallierName) {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			EncryptionKeyShare eks = ebp.getEncryptionKeyShare(tallierName);
			Signature signature = eks.getSignature();

			//concatenate to (id|y_j|(t|s))|timestamp
			sc.pushLeftDelim();

			sc.pushObjectDelimiter(eks.getElectionId(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(eks.getKey(), StringConcatenator.INNER_DELIMITER);
			//proof ((t|...|tn)|(s|...|sn))
			sc.pushLeftDelim();
			sc.pushList(eks.getProof().getCommitment(), true);
			sc.pushInnerDelim();
			sc.pushList(eks.getProof().getResponse(), true);
			sc.pushRightDelim();
			//end proof

			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verifiy the signature
			RSAPublicKey tallierPubKey = (RSAPublicKey) ebp.getTalliersCerts().get(tallierName).getPublicKey();
			r = vrfRSASign(tallierPubKey, res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_SETUP_T_NIZKP_OF_X_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.TALLIER);
		v.setEntityName(tallierName);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;

	}

	/**
	 * Verify the signature of the distributed key y.
	 *
	 * Specification: 1.3.4, d.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfDistributedKeySign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			EncryptionKey ek = ebp.getEncryptionKey();
			Signature signature = ek.getSignature();

			//concatenate to (id|y)|timestamp
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(ek.getElectionId(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(ek.getKey(), StringConcatenator.RIGHT_DELIMITER);
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_SETUP_T_PUBLIC_KEY_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the proof for the election generator.
	 *
	 * Specification: 1.3.4, e.
	 *
	 * @param mixerName The name of the mixer.
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfElectionGeneratorBySign(String mixerName) {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			BlindedGenerator bg = ebp.getBlindedGenerator(mixerName);
			Signature signature = bg.getSignature();

			//concatenate to (id|g_k|proof)|timestamp
			sc.pushLeftDelim();

			sc.pushObjectDelimiter(bg.getElectionId(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(bg.getGenerator(), StringConcatenator.INNER_DELIMITER);
			//proof ((t|...|tn)|(s|...|sn))
			sc.pushLeftDelim();
			sc.pushList(bg.getProof().getCommitment(), true);
			sc.pushInnerDelim();
			sc.pushList(bg.getProof().getResponse(), true);
			sc.pushRightDelim();
			//end proof
			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			RSAPublicKey mixerPubKey = (RSAPublicKey) ebp.getMixersCerts().get(mixerName).getPublicKey();
			r = vrfRSASign(mixerPubKey, res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_SETUP_M_NIZKP_OF_ALPHA_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.MIXER);
		v.setEntityName(mixerName);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the election generator.
	 *
	 * Specification: 1.3.4, e.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfElectionGeneratorSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			ElectionGenerator eg = ebp.getElectionGenerator();
			Signature signature = eg.getSignature();

			//concatenate to (id|g^)|timestamp
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(eg.getElectionId(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(eg.getGenerator(), StringConcatenator.RIGHT_DELIMITER);
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}


		VerificationResult v =
            new VerificationResult(VerificationType.EL_SETUP_ANON_GEN_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the election options.
	 *
	 * Specification: 1.3.5, a.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfElectionOptionsSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			ElectionOptions eo = ebp.getElectionOptions();
			Signature signature = eo.getSignature();

			//concatenate to (id|(c1|....|cn)|(r1|...|rn))|timestamp
			sc.pushLeftDelim();

			sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);

			sc.pushLeftDelim();
			//for each choice
			for (int i = 0; i < eo.getChoice().size(); i++) {
				Choice c = eo.getChoice().get(i);

				if (i > 0) {
					sc.pushInnerDelim();
				}

				sc.pushLeftDelim();
				sc.pushObjectDelimiter(c.getChoiceId(), StringConcatenator.INNER_DELIMITER);

				if (c instanceof PoliticalList) {
					PoliticalList pl = (PoliticalList) c;

					sc.pushObjectDelimiter(pl.getNumber(), StringConcatenator.INNER_DELIMITER);

					sc.pushLocalizedText(pl.getTitle());

					sc.pushInnerDelim();
					sc.pushLocalizedText(pl.getPartyName());

					sc.pushInnerDelim();

					sc.pushLocalizedText(pl.getPartyShortName());

				} else if (c instanceof Candidate) {
					Candidate can = (Candidate) c;

					sc.pushObjectDelimiter(can.getNumber(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getLastName(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getFirstName(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getSex(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getYearOfBirth(), StringConcatenator.INNER_DELIMITER);

					//getStudyBranc ((lan|text)|...|(lan|text))
					sc.pushLocalizedText(can.getStudyBranch());

					sc.pushInnerDelim();

					//getStudyDegree ((lan|text)|...|(lan|text))
					sc.pushLocalizedText(can.getStudyDegree());

					sc.pushInnerDelim();

					sc.pushObjectDelimiter(can.getSemesterCount(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getStatus(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getListId(), StringConcatenator.INNER_DELIMITER);
					sc.pushObject(can.getCumulation());
				}

				sc.pushRightDelim();
			}

			sc.pushRightDelim();
			sc.pushInnerDelim();

			sc.pushLeftDelim();

			//for each rule
			for (int k = 0; k < eo.getRule().size(); k++) {
				if (k > 0) {
					sc.pushInnerDelim();
				}

				Rule ru = eo.getRule().get(k);
				int lowerBound = 0, upperBound = 0;

				sc.pushLeftDelim();

				if (ru instanceof SummationRule) {
					sc.pushObjectDelimiter("summationRule", StringConcatenator.INNER_DELIMITER);
					lowerBound = ((SummationRule) ru).getLowerBound();
					upperBound = ((SummationRule) ru).getUpperBound();
				} else if (ru instanceof ForallRule) {
					sc.pushObjectDelimiter("forallRule", StringConcatenator.INNER_DELIMITER);
					lowerBound = ((ForallRule) ru).getLowerBound();
					upperBound = ((ForallRule) ru).getUpperBound();
				}
				//array for the choices
				sc.pushLeftDelim();
				for (int f = 0; f < ru.getChoiceId().size(); f++) {
					if (f > 0) {
						sc.pushInnerDelim();
					}

					sc.pushObject(ru.getChoiceId().get(f));
				}
				sc.pushRightDelim();
				sc.pushInnerDelim();
				sc.pushObjectDelimiter(lowerBound, StringConcatenator.INNER_DELIMITER);
				sc.pushObject(upperBound);

				sc.pushRightDelim();
			}

			sc.pushRightDelim();
			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEACert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_PREP_C_AND_R_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EA);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the election data.
	 *
	 * Specification: 1.3.5, b.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfElectionDataSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			ElectionData ed = ebp.getElectionData();
			Signature signature = ed.getSignature();

			//change this when it will be available - ToDo
			String eaIdentifier = ebp.getElectionOptions().getSignature().getSignerId();

			//concatenate to (id|EA|descr|P|Q|G|y|g^|(c1|...|cn)|(r1|...|rn))|timestamp
			sc.pushLeftDelim();

			sc.pushObjectDelimiter(ed.getElectionId(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(eaIdentifier, StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(ed.getTitle(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(ed.getPrime(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(ed.getGroupOrder(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(ed.getGenerator(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(ed.getEncryptionKey(), StringConcatenator.INNER_DELIMITER);
			sc.pushObjectDelimiter(ed.getElectionGenerator(), StringConcatenator.INNER_DELIMITER);

			//choices and rules
			sc.pushLeftDelim();
			//for each choice
			for (int i = 0; i < ed.getChoice().size(); i++) {
				Choice c = ed.getChoice().get(i);

				if (i > 0) {
					sc.pushInnerDelim();
				}

				sc.pushLeftDelim();
				sc.pushObjectDelimiter(c.getChoiceId(), StringConcatenator.INNER_DELIMITER);

				if (c instanceof PoliticalList) {
					PoliticalList pl = (PoliticalList) c;

					sc.pushObjectDelimiter(pl.getNumber(), StringConcatenator.INNER_DELIMITER);

					sc.pushLocalizedText(pl.getTitle());

					sc.pushInnerDelim();
					sc.pushLocalizedText(pl.getPartyName());

					sc.pushInnerDelim();

					sc.pushLocalizedText(pl.getPartyShortName());

				} else if (c instanceof Candidate) {
					Candidate can = (Candidate) c;

					sc.pushObjectDelimiter(can.getNumber(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getLastName(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getFirstName(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getSex(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getYearOfBirth(), StringConcatenator.INNER_DELIMITER);

					//getStudyBranc ((lan|text)|...|(lan|text))
					sc.pushLocalizedText(can.getStudyBranch());

					sc.pushInnerDelim();

					//getStudyDegree ((lan|text)|...|(lan|text))
					sc.pushLocalizedText(can.getStudyDegree());

					sc.pushInnerDelim();

					sc.pushObjectDelimiter(can.getSemesterCount(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getStatus(), StringConcatenator.INNER_DELIMITER);
					sc.pushObjectDelimiter(can.getListId(), StringConcatenator.INNER_DELIMITER);
					sc.pushObject(can.getCumulation());
				}

				sc.pushRightDelim();
			}

			sc.pushRightDelim();
			sc.pushInnerDelim();

			sc.pushLeftDelim();

			//for each rule
			for (int k = 0; k < ed.getRule().size(); k++) {
				if (k > 0) {
					sc.pushInnerDelim();
				}

				Rule ru = ed.getRule().get(k);
				int lowerBound = 0, upperBound = 0;

				sc.pushLeftDelim();

				if (ru instanceof SummationRule) {
					sc.pushObjectDelimiter("summationRule", StringConcatenator.INNER_DELIMITER);
					lowerBound = ((SummationRule) ru).getLowerBound();
					upperBound = ((SummationRule) ru).getUpperBound();
				} else if (ru instanceof ForallRule) {
					sc.pushObjectDelimiter("forallRule", StringConcatenator.INNER_DELIMITER);
					lowerBound = ((ForallRule) ru).getLowerBound();
					upperBound = ((ForallRule) ru).getUpperBound();
				}

				//array for the choices
				sc.pushLeftDelim();
				for (int f = 0; f < ru.getChoiceId().size(); f++) {
					if (f > 0) {
						sc.pushInnerDelim();
					}

					sc.pushObject(ru.getChoiceId().get(f));
				}
				sc.pushRightDelim();
				sc.pushInnerDelim();
				sc.pushObjectDelimiter(lowerBound, StringConcatenator.INNER_DELIMITER);
				sc.pushObject(upperBound);

				sc.pushRightDelim();
			}

			sc.pushRightDelim();
			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_PREP_EDATA_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the electoral roll.
	 *
	 * Specification: 1.3.5, c.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfElectoralRollSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			ElectoralRoll er = ebp.getElectoralRoll();
			Signature signature = er.getSignature();

			//concatenate to (id|(h1|...|hn))|timestamp
			sc.pushLeftDelim();

			sc.pushObjectDelimiter(er.getElectionId(), StringConcatenator.INNER_DELIMITER);
			sc.pushList(er.getVoterHash(), true);

			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEACert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_PREP_ELECTORAL_ROLL_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EA);


		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the set of Voters certificates with the election ID.
	 *
	 * Specification: 1.3.5, c.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfVotersCertIDSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			VoterCertificates vc = ebp.getVoterCerts();
			Signature signature = vc.getSignature();
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(vc.getElectionId(), StringConcatenator.INNER_DELIMITER);



			//for all certs
			sc.pushLeftDelim();
			for (int k = 0; k < vc.getCertificate().size(); k++) {
				if (k > 0) {
					sc.pushInnerDelim();
				}

				Certificate cert = vc.getCertificate().get(k);
				sc.pushObject(new BigInteger(cert.getValue()));
			}
			sc.pushRightDelim();
			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_PREP_VOTERS_CERT_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);


		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the set of verification keys mixed by a given mixer.
	 *
	 * @param mixerName the name of the mixer.
	 *
	 * Specification: 1.3.5, d.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfMixedVerificationKeysBySign(String mixerName) {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			MixedVerificationKeys mk = ebp.getMixedVerificationKeysBy(mixerName);
			Signature signature = mk.getSignature();

			//concatenate to (id|(vk1|...|vkn)|(()|()))|timestamp - WARNING the proof is empty.
			sc.pushLeftDelim();

			sc.pushObjectDelimiter(mk.getElectionId(), StringConcatenator.INNER_DELIMITER);
			sc.pushList(mk.getKey(), true);
			sc.pushInnerDelim();

			//empty proof
			sc.pushLeftDelim();

			sc.pushLeftDelim();
			sc.pushRightDelim();

			sc.pushInnerDelim();

			sc.pushLeftDelim();
			sc.pushRightDelim();

			sc.pushRightDelim();
			//end empty proof

			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			RSAPublicKey mixerPubKey = (RSAPublicKey) ebp.getMixersCerts().get(mixerName).getPublicKey();
			r = vrfRSASign(mixerPubKey, res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_PREP_M_PUB_VER_KEYS_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.MIXER);
		v.setEntityName(mixerName);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature over the set of mixed verification keys.
	 *
	 * Specification: 1.3.5, d.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfMixedVerificationKeysSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			VerificationKeys mk = ebp.getMixedVerificationKeys();
			Signature signature = mk.getSignature();

			//concatenate to (id|(vk1|...|vkn))|timestamp
			sc.pushLeftDelim();

			sc.pushObjectDelimiter(mk.getElectionId(), StringConcatenator.INNER_DELIMITER);
			sc.pushList(mk.getKey(), true);

			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_PREP_PUB_VER_KEYS_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the election ID with the late registered voter certificate.
	 *
	 * Specification: 1.3.6, a.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfLatelyRegisteredVotersCertificateSign() {
		boolean r = false;
		Report rep;
		Exception exc = null;

		try {
			List<Certificate> vc = ebp.getLatelyRegisteredVoterCerts();
			//ToDo - Find the signature
		} catch (ElectionBoardServiceFault ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.EL_PERIOD_LATE_NEW_VOTER_CERT_SIGN, r, ebp.getElectionID(), rn, it, EntityType.EM);
		v.setImplemented(false);
		//create a report for the non-implemented
		rep = new Report(FailureCode.NOT_YET_IMPLEMENTED);
		v.setReport(rep);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the lately verification keys with the proof. The proof is not included in the signature
	 * computation because is not yet available.
	 *
	 * Specification: 1.3.6, a.
	 *
	 * @param mixerName the name of the mixer
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfLatelyVerificationKeysBySign(String mixerName) {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			List<MixedVerificationKey> mvk = ebp.getLatelyMixedVerificationKeysBy(mixerName);
			RSAPublicKey mixerPubKey = (RSAPublicKey) ebp.getMixersCerts().get(mixerName).getPublicKey();

			//for each key of this mixer
			for (MixedVerificationKey key : mvk) {
				Signature signature = key.getSignature();

				//concatenate to (id|vk|(()|()))|timestamp - WARNING: The proof is not yet implemented.
				sc.pushLeftDelim();

				sc.pushObjectDelimiter(key.getElectionId(), StringConcatenator.INNER_DELIMITER);
				sc.pushObjectDelimiter(key.getKey(), StringConcatenator.INNER_DELIMITER);

				//empty proof
				sc.pushLeftDelim();
				sc.pushList(key.getProof().getCommitment(), true);
				sc.pushInnerDelim();
				sc.pushList(key.getProof().getResponse(), true);
				sc.pushRightDelim();
				//end empty proof

				sc.pushRightDelim();
				sc.pushInnerDelim();
				sc.pushObject(signature.getTimestamp());

				String res = sc.pullAll();

				//verify the signature
				r = vrfRSASign(mixerPubKey, res, signature.getValue());

				//if one signature fails, all is false so break.
				if (!r) {
					break;
				}
			}
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_PERIOD_M_NIZKP_EQUALITY_NEW_VRF_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.MIXER);
		v.setEntityName(mixerName);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 *
	 * Verify the signature of the verification keys of the lately mixed verification set.
	 *
	 * Specification: 1.3.6, a.
	 *
	 * @param mixerName the name of the mixer.
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfLatelyVerificationKeysSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			List<MixedVerificationKey> mk = ebp.getLateyMixedVerificationKeys();

			//check the signature for each key in the set
			for (MixedVerificationKey key : mk) {
				Signature signature = key.getSignature();

				//concatenate to (id|voterId|key)|timestamp - ToDo look for the Voter identiy
				sc.pushLeftDelim();

				sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
				sc.pushObject(key.getKey());

				sc.pushRightDelim();
				sc.pushInnerDelim();
				sc.pushObject(signature.getTimestamp());

				String res = sc.pullAll();

				//verify the signature - EM has not signed the data!
				r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature.getValue());

				if (!r) {
					break;
				}
			}
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_PERIOD_NEW_VER_KEY_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the late renewal of registration keys with the proof. The proof is not included in the
	 * signature computation because is not yet available.
	 *
	 * Specification: 1.3.6, b.
	 *
	 * @param mixerName the name of the mixer.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfLateRenewalOfRegistrationSignBy(String mixerName) {
		VerificationResult v = new VerificationResult(VerificationType.EL_PERIOD_M_NIZKP_EQUALITY_LATEREN_SIGN, false, ebp.getElectionID(), rn, it, EntityType.MIXER);

		v.setImplemented(false);
		v.setEntityName(mixerName);

		Report rep = new Report(FailureCode.NOT_YET_IMPLEMENTED);
		rep.setAdditionalInformation("This verification is not yet implemented due the lack of necessary data.");
		v.setReport(rep);

		return v;
	}

	/**
	 * Verify the signature of the late renewal of registration keys.
	 *
	 * Specification: 1.3.6, b.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfLateRenewalOfRegistrationSign() {
		VerificationResult v = new VerificationResult(VerificationType.EL_PERIOD_M_LAST_M_LATEREN_KEY_SIGN, false, ebp.getElectionID(), rn, it, EntityType.EM);

		v.setImplemented(false);

		Report rep = new Report(FailureCode.NOT_YET_IMPLEMENTED);
		rep.setAdditionalInformation("This verification is not yet implemented due the lack of necessary data.");
		v.setReport(rep);

		return v;
	}

	/**
	 *
	 * Verify the signature of the ballots set.
	 *
	 * Specification: 1.3.6, d.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfBallotsSetSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			Ballots b = ebp.getBallots();
			Signature signature = b.getSignature();

			//concatenate to (id|ballotsstate|ballots)|timestamp
			sc.pushLeftDelim();

			sc.pushObjectDelimiter(b.getElectionId(), StringConcatenator.INNER_DELIMITER);

			//for each ballot
			sc.pushLeftDelim();
			for (int i = 0; i < b.getBallot().size(); i++) {
				Ballot singleBallot = b.getBallot().get(i);
				if (i > 0) {
					sc.pushInnerDelim();
				}

				sc.pushLeftDelim();
				sc.pushObjectDelimiter(singleBallot.getElectionId(), StringConcatenator.INNER_DELIMITER);

				//verification key
				sc.pushObjectDelimiter(singleBallot.getVerificationKey(), StringConcatenator.INNER_DELIMITER);

				//encrypted vote
				sc.pushLeftDelim();
				sc.pushObjectDelimiter(singleBallot.getEncryptedVote().getFirstValue(), StringConcatenator.INNER_DELIMITER);
				sc.pushObject(singleBallot.getEncryptedVote().getSecondValue());
				sc.pushRightDelim();

				sc.pushInnerDelim();

				//proof
				sc.pushLeftDelim();
				sc.pushList(singleBallot.getProof().getCommitment(), true);
				sc.pushInnerDelim();
				sc.pushList(singleBallot.getProof().getResponse(), true);
				sc.pushRightDelim();

				sc.pushInnerDelim();

				//voter signature
				sc.pushLeftDelim();
				sc.pushObjectDelimiter(singleBallot.getSignature().getFirstValue(), StringConcatenator.INNER_DELIMITER);
				sc.pushObject(singleBallot.getSignature().getSecondValue());
				sc.pushRightDelim();

				sc.pushRightDelim();
			}
			sc.pushRightDelim();

			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verifiy the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.EL_PERIOD_BALLOT_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 *
	 * Verify the signature of the shuffled encrypted votes for a given mixer.
	 *
	 * Specification: 1.3.7, a.
	 *
	 * @param mixerName the name of the mixer
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfMixedEncryptedVotesBySign(String mixerName) {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			MixedEncryptedVotes mev = ebp.getMixedEncryptedVotesBy(mixerName);
			Signature signature = mev.getSignature();

			//concatenate to (id|((firstValue|secondValue)|.......|(nfirstValue|nSecondValue))(()|()))|timestamp - WARNING :the proof is not implemented
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);

			sc.pushLeftDelim();
			//for each vote
			for (int i = 0; i < mev.getVote().size(); i++) {
				EncryptedVote e = mev.getVote().get(i);

				if (i > 0) {
					sc.pushInnerDelim();
				}

				sc.pushLeftDelim();
				sc.pushObject(e.getFirstValue());
				sc.pushInnerDelim();
				sc.pushObject(e.getSecondValue());
				sc.pushRightDelim();
			}
			sc.pushRightDelim();
			//Empty proof
			sc.pushInnerDelim();
			sc.pushLeftDelim();
			sc.pushLeftDelim();
			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushLeftDelim();
			sc.pushRightDelim();
			sc.pushRightDelim();

			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verifiy the signature
			RSAPublicKey mixerPubKey = (RSAPublicKey) ebp.getMixersCerts().get(mixerName).getPublicKey();
			r = vrfRSASign(mixerPubKey, res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.MT_M_ENC_VOTES_SET_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.MIXER);
		v.setEntityName(mixerName);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 *
	 * Verify the signature of the shuffled encrypted votes.
	 *
	 * Specification: 1.3.7, a.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfMixedEncryptedVotesSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			EncryptedVotes mev = ebp.getEncryptedVotes();
			Signature signature = mev.getSignature();

			//concatenate to (id|((firstValue|secondValue)|.......|(nfirstValue|nSecondValue)))|timestamp
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);
			sc.pushLeftDelim();

			for (int i = 0; i < mev.getVote().size(); i++) {
				EncryptedVote e = mev.getVote().get(i);

				if (i > 0) {
					sc.pushInnerDelim();
				}

				sc.pushLeftDelim();
				sc.pushObject(e.getFirstValue());
				sc.pushInnerDelim();
				sc.pushObject(e.getSecondValue());
				sc.pushRightDelim();
			}

			sc.pushRightDelim();
			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verifiy the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEACert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.MT_ENC_VOTES_ID_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EA);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 *
	 * Verify the signature of the NIZKP for the decrypting votes.
	 *
	 * Specification: 1.3.7, b.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfDecryptedVotesBySign(String tallierName) {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			PartiallyDecryptedVotes pdv = ebp.getPartiallyDecryptedVotes(tallierName);
			Signature signature = pdv.getSignature();

			//concatenate to (id|(a_1|a_2|....|a_n)|proof)|timestamp
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);

			sc.pushList(pdv.getVote(), true);

			sc.pushInnerDelim();

			//push the proof
			sc.pushLeftDelim();
			sc.pushList(pdv.getProof().getCommitment(), true);
			sc.pushInnerDelim();
			sc.pushList(pdv.getProof().getResponse(), true);
			sc.pushRightDelim();
			//end proof

			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();

			//verify the signature
			RSAPublicKey tallierPubKey = (RSAPublicKey) ebp.getTalliersCerts().get(tallierName).getPublicKey();
			r = vrfRSASign(tallierPubKey, res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.MT_T_NIZKP_OF_X_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.TALLIER);
		v.setEntityName(tallierName);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of the plaintext votes.
	 *
	 * Specification: 1.3.7, b.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfPlaintextVotesSign() {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			DecodedVotes dv = ebp.getDecodedVotes();
			Signature signature = dv.getSignature();

			//concatenate to (id|(v1|v2|....|vn))|timestamp
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);

			//( ((cID|count)|(cID|count))|...|((cID|count)|(cID|count)) )
			sc.pushLeftDelim();
			for (int i = 0; i < dv.getDecodedVote().size(); i++) {
				if (i > 0) {
					sc.pushInnerDelim();
				}

				DecodedVote decVote = dv.getDecodedVote().get(i);

				sc.pushLeftDelim();
				//(cID|count)|(cID|count)|....|(cID|count)
				for (int j = 0; j < dv.getDecodedVote().get(i).getEntry().size(); j++) {
					DecodedVoteEntry dve = decVote.getEntry().get(j);

					if (j > 0) {
						sc.pushInnerDelim();
					}

					sc.pushLeftDelim();
					sc.pushObject(dve.getChoiceId());
					sc.pushInnerDelim();
					sc.pushObject(dve.getCount());
					sc.pushRightDelim();
				}
				sc.pushRightDelim();
			}
			sc.pushRightDelim();

			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(signature.getTimestamp());

			String res = sc.pullAll();
			System.out.println(res);

			//verify the signature
			r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signature.getValue());
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.MT_VALID_PLAINTEXT_VOTES_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Verify the signature of a single ballot from a QR-Code.
	 *
	 * Specification: individual verification.
	 *
	 * @param er the ElectionReceipt containing the necessary information.
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfSingleBallotSign(ElectionReceipt er) {
		boolean r = false;
		Exception exc = null;
		Report rep;

		try {
			BigInteger signatureValue = er.getSignatureValue();

			//concatenate to - (id|(encValueA|encValueB)|((t)|(s)))|timestamp
			sc.pushLeftDelim();
			//election ID
			sc.pushObjectDelimiter(er.getElectionID(), StringConcatenator.INNER_DELIMITER);
			//(encValueA|encValueB)
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(er.getEncValueA(), StringConcatenator.INNER_DELIMITER);
			sc.pushObject(er.getEncValueB());
			sc.pushRightDelim();
			sc.pushInnerDelim();
			//((t)|(s))
			sc.pushLeftDelim();

			sc.pushLeftDelim();
			sc.pushObject(er.getProofCommitment());
			sc.pushRightDelim();

			sc.pushInnerDelim();

			sc.pushLeftDelim();
			sc.pushObject(er.getProofResponse());
			sc.pushRightDelim();

			sc.pushRightDelim();
			//right parenthesis
			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushObject(er.getTimeStamp());

			String res = sc.pullAll();

			r = vrfRSASign((RSAPublicKey) ebp.getEMCert().getPublicKey(), res, signatureValue);
		} catch (Exception ex) {
			exc = ex;
		}

		VerificationResult v =
            new VerificationResult(VerificationType.SINGLE_BALLOT_RSA_SIGN, r,
            ebp.getElectionID(), rn, it, EntityType.EM);


		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_RSA_SIGNATURE);
			v.setReport(rep);
		}

		return v;
	}
}
