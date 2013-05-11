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

import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.StringConcatenator;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * This class contains all the methods that need a RSA Verification.
 *
 * @author snake
 */
public class RSAImplementer extends Implementer {

	private final StringConcatenator sc;
	private final RSAPublicKey emPubKey;
	private final RSAPublicKey eaPubKey;

	/**
	 * Create a new RSAImplementer.
	 *
	 * @param ebp the election board proxy from where get the data.
	 */
	public RSAImplementer(ElectionBoardProxy ebp, RunnerName rn) throws CertificateException, ElectionBoardServiceFault {
		super(ebp, rn);
		sc = new StringConcatenator();
		emPubKey = (RSAPublicKey) ebp.getEMCert().getPublicKey();
		eaPubKey = (RSAPublicKey) ebp.getEACert().getPublicKey();

	}

	/**
	 * Verify a RSA signature.
	 *
	 * @param s the RSAPublicKey.
	 * @param clearText the text from where to get the signature.
	 * @param signature the pre-computed signature from the board.
	 * @return true if the signature is verified correctly otherwise not.
	 */
	public boolean vrfRSASign(RSAPublicKey s, BigInteger hash, BigInteger signature) {
		//compute signature^e mod s, this must be equal to the hash we have computed
		BigInteger decSign = signature.modPow(s.getPublicExponent(), s.getModulus());

		boolean result = decSign.equals(hash);

		return result;
	}

	/**
	 * Verify the signature of the election administrator certificate plus
	 * the election ID.
	 *
	 * Specification: 1.3.4, a.
	 *
	 * @returna verification event.
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
		sc.pushObjectDelimiter(eaCertStr, StringConcatenator.RIGHT_DELIMITER);

		String strRes = sc.pullAll();

		//compute the hash of the concatenated string
		BigInteger hash = CryptoFunc.sha1(strRes);

		//find the signature of Page 13, Initialization, Step 3 - ToDO
		BigInteger signature = new BigInteger("1");

		boolean r = vrfRSASign(emPubKey, hash, signature);

		return new VerificationResult(VerificationType.EL_SETUP_EA_CERT_ID_SIGN, r, ebp.getElectionID(), rn);
	}

	/**
	 * Verify the signature of the basic parameters (id, description, key
	 * length, talliers and mixers) of an election.
	 *
	 * Specification: 1.3.4, b.
	 *
	 * @returna a VerificationResul.
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

		//concatenate to (id|descr|keyLength|(t_1|...|t_n)|(m_1|...|m_n)|timestamp)
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(eID, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(descr, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(keyLength, StringConcatenator.INNER_DELIMITER);
		sc.pushList(talliers, true);
		sc.pushInnerDelim();
		sc.pushList(mixers, true);
		sc.pushInnerDelim();
		sc.pushObjectDelimiter(signature.getTimestamp().toString(), StringConcatenator.RIGHT_DELIMITER);

		String res = sc.pullAll();

		//compute the hash of the concatenated string
		BigInteger hash = CryptoFunc.sha256(res);

		//verify the signature
		boolean r = vrfRSASign(eaPubKey, hash, signature.getValue());

		//create the VerificationResult
		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_BASICS_PARAMS_SIGN, r, ebp.getElectionID(), rn);

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

		//compute the hash of the concatenated string
		BigInteger hash = CryptoFunc.sha1(res);

		//verify the signature
		boolean r = vrfRSASign(emPubKey, hash, signature.getValue());

		//create the VerificationResult
		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_T_CERT_M_CERT_ID_SIGN, r, ebp.getElectionID(), rn);

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

		//concatenate to (id|P|Q|G|timestamp)
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionDefinition().getElectionId(), StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(P, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(Q, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(G, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(signature.getTimestamp(), StringConcatenator.RIGHT_DELIMITER);

		String res = sc.pullAll();

		//compute the hash of the concatenated string
		BigInteger hash = CryptoFunc.sha1(res);

		//verify the signature
		boolean r = vrfRSASign(emPubKey, hash, signature.getValue());

		//create the VerificationResult
		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAMS_SIGN, r, ebp.getElectionID(), rn);

		if (!r) {
			v.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return v;
	}
}
