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
import ch.bfh.univote.common.Ballots;
import ch.bfh.univote.common.ElectionDefinition;
import ch.bfh.univote.common.Signature;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.StringConcatenator;
import ch.bfh.univoteverifier.utils.RSASignature;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains all the methods that need a RSA Verification.
 *
 * @author snake
 */
public class RSAImplementer {

	private static final Logger LOGGER = Logger.getLogger(RSAImplementer.class.getName());
	private final ElectionBoardProxy ebp;
	private final StringConcatenator sc;
	private final RSAPublicKey emPubKey;
	private final RSAPublicKey eaPubKey;

	/**
	 * Create a new RSAImplementer.
	 *
	 * @param ebp the election board proxy from where get the data
	 */
	public RSAImplementer(ElectionBoardProxy ebp) {
		this.ebp = ebp;
		sc = new StringConcatenator();
		//ToDo - decomment when we have the data
		//emPubKey = (RSAPublicKey) Config.emCert.getPublicKey();
		emPubKey = null;
		//eaPubKey = (RSAPublicKey)Config.eaCert.getPublicKey();
		eaPubKey = null;

	}

	/**
	 * ToDo - This method is used in the class utils. Maybe move it there in
	 * another class
	 *
	 * @param s
	 * @param mIn
	 * @return
	 */
	public boolean vrfRSASign(RSASignature s, BigInteger mIn) {
		BigInteger ver = s.getSig().modPow(s.getE(), s.getN());

		boolean result = ver.equals(mIn);

		LOGGER.log(Level.SEVERE, "RSA Verification failed");

		return result;
	}

	/**
	 * Verify a RSA signature.
	 *
	 * @param s the RSAPublicKey
	 * @param clearText the text from where to get the signature
	 * @param signature the pre-computed signature from the board
	 * @return true if the signature is verified correctly otherwise not
	 */
	public boolean vrfRSASign(RSAPublicKey s, BigInteger clearText, BigInteger signature) {
		BigInteger ver = clearText.modPow(s.getPublicExponent(), s.getModulus());

		boolean result = ver.equals(signature);

		return result;
	}

	/**
	 * Verify the signature of the election administrator certificate plus
	 * the election ID.
	 *
	 * @return a verification event
	 * @throws ElectionBoardServiceFault
	 * @throws CertificateException
	 */
	public VerificationEvent vrfEACertIDSign() throws ElectionBoardServiceFault, CertificateException, NoSuchAlgorithmException {
		//get the certificte as a string
		String eaCertStr = Config.eaCert.toString();

		//get the election id
		String eID = ebp.getElectionDefinition().getElectionId();

		//concatenate to (id|Z_ea|timestamp)
		sc.pushLeftDelim();
		sc.pushObject(eID);
		sc.pushInnerDelim();
		sc.pushObject(eaCertStr);
		//add the timestamp when we found where this signature is - ToDo
		//sc.pushInnerDelim();
		//sc.pushObject(timestamp)
		//sc.pushInnerDelim();
		sc.pushRightDelim();

		String strRes = sc.pullAll();

		//compute the sha-1 hash of (id|Z_a|timestamp)
		BigInteger hash = CryptoFunc.sha1(strRes);

		//find the signature of Page 13, Initialization, Step 3 - ToDO
		BigInteger signature = new BigInteger("1");

		boolean r = vrfRSASign(emPubKey, hash, signature);

		return new VerificationEvent(VerificationType.EL_SETUP_EA_CERT_ID_SIGN, r);
	}

	/**
	 * Verify the signature of the basic parameters (id, description, key
	 * length, talliers and mixers) of an election.
	 *
	 * @return a verification event
	 * @throws ElectionBoardServiceFault
	 * @throws NoSuchAlgorithmException
	 */
	public VerificationEvent vrfBasicParamSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException {
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
		sc.pushObject(eID);
		sc.pushInnerDelim();
		sc.pushObject(descr);
		sc.pushInnerDelim();
		sc.pushObject(keyLength);
		sc.pushInnerDelim();
		sc.pushList(talliers, true);
		sc.pushInnerDelim();
		sc.pushList(mixers, true);
		sc.pushInnerDelim();
		sc.pushObject(signature.getTimestamp().toString());
		sc.pushRightDelim();

		String res = sc.pullAll();

		//compute the hash of the concatenated string
		BigInteger hash = CryptoFunc.sha1(res);

		//verify the signature
		boolean r = vrfRSASign(eaPubKey, hash, signature.getValue());

		//create the VerificationEvent
		VerificationEvent v = new VerificationEvent(VerificationType.EL_SETUP_BASICS_PARAMS_SIGN, r);

		if (!r) {
			v.setFailureCode(FailureCode.INVALID_RSA_SIGNATURE);
		}

		return v;
	}
}
