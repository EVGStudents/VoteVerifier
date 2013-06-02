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
import ch.bfh.univote.common.VoterSignature;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
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

/**
 * This class contains all the methods that need a Schnorr verification.
 *
 * @author Scalzi Giuseppe
 */
public class SchnorrImplementer extends Implementer {

	private BigInteger p, q, g;

	/**
	 * Construct a Schnorr implementer.
	 *
	 * @param ebp the election board proxy from where get the data.
	 */
	public SchnorrImplementer(ElectionBoardProxy ebp, RunnerName rn) {
		super(ebp, rn, ImplementerType.SCHNORR);
		p = Config.p;
		q = Config.q;
		g = Config.g;
	}

	/**
	 * Verify the given Schnorr's signature against the hash we have
	 * computed.
	 *
	 *
	 * @param verificationKey the public key we must use to verify the
	 * signature.
	 * @param message the message to be verified.
	 * @param a the first value of a valid Schnorr signature.
	 * @param b the second value of a valid Schnorr signature.
	 * @param gen the chose generator.
	 * @return true if the verification succeed, false otherwise
	 * @throws NoSuchAlgorithmException if the hash function used in this
	 * method does not find the algorithm.
	 * @throws UnsupportedEncodingException if the hash function used in
	 * this method does not find the encoding.
	 */
	public boolean vrfSchnorrSign(BigInteger verificationKey, String message, BigInteger a, BigInteger b, BigInteger gen) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		//compute r = g^b * vk^a mod p
		BigInteger r = gen.modPow(b, p).multiply(verificationKey.modPow(a, p)).mod(p);

		//concatenate clear text with r: (m|r)
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(message, StringConcatenator.INNER_DELIMITER);
		sc.pushObject(r);
		sc.pushRightDelim();

		String concat = sc.pullAll();

		//hashResult = sha-256(concat) mod q => this must be equal to a
		BigInteger hashResult = CryptoFunc.sha256(concat).mod(q);

		//return the result of hashResult == a
		return hashResult.equals(a);
	}

	/**
	 * Set a new value for p.
	 *
	 * @param p the p value of the Schnorr's parameters.
	 */
	public void setP(BigInteger p) {
		this.p = p;
	}

	/**
	 * Set a new value for q.
	 *
	 * @param q the q value of the Schnorr's parameters.
	 */
	public void setQ(BigInteger q) {
		this.q = q;
	}

	/**
	 * Set a new value for g.
	 *
	 * @param g the g value of the Schnorr's parameters.
	 */
	public void setG(BigInteger g) {
		this.g = g;
	}

	/**
	 * Verify the signature of the given Ballot or of the given
	 * ElectionReceipt.
	 *
	 * @param b the Ballot we want to verify the signature.
	 * @param er the ElectionReceipt that contains the necessary data.
	 * generator instead of the normal generator g from the config file
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfBallotSignature(Ballot b, ElectionReceipt er) {
		Exception exc = null;
		boolean r = false;
		Report rep;

		try {
			BigInteger encFirstValue = null, encSecondValue = null, proofCommitment = null, proofResponse = null, schnorrFirstValue = null, schnorrSecondValue = null, verificationKey = null;
			String eID = null;

			//get the different values for the object that is not null.
			if (b != null) {
				eID = b.getElectionId();
				encFirstValue = b.getEncryptedVote().getFirstValue();
				encSecondValue = b.getEncryptedVote().getSecondValue();
				proofCommitment = b.getProof().getCommitment().get(0);
				proofResponse = b.getProof().getResponse().get(0);
				schnorrFirstValue = b.getSignature().getFirstValue();
				schnorrSecondValue = b.getSignature().getSecondValue();
				verificationKey = b.getVerificationKey();
			} else if (er != null) {
				eID = er.getElectionID();
				//ToDo check if these value are equals from the one of the eb
				encFirstValue = new BigInteger(1, er.getEncValueA().toByteArray());
				encSecondValue = new BigInteger(1, er.getEncValueB().toByteArray());
				proofCommitment = new BigInteger(1, er.getProofCommitment().toByteArray());
				proofResponse = new BigInteger(1, er.getProofResponse().toByteArray());
				schnorrFirstValue = new BigInteger(1, er.getSchnorrValueA().toByteArray());
				schnorrSecondValue = new BigInteger(1, er.getSchnorrValueB().toByteArray());
				verificationKey = new BigInteger(1, er.getVerificationKey().toByteArray());
			}

			//concatenate to ( id | (firstValue|secondValue) | ((t)|(s)) )
			sc.pushLeftDelim();
			//election ID
			sc.pushObjectDelimiter(eID, StringConcatenator.INNER_DELIMITER);
			//encrypted vote
			sc.pushLeftDelim();
			sc.pushObjectDelimiter(encFirstValue, StringConcatenator.INNER_DELIMITER);
			sc.pushObject(encSecondValue);
			sc.pushRightDelim();
			sc.pushInnerDelim();
			//proof
			sc.pushLeftDelim();
			sc.pushLeftDelim();
			sc.pushObject(proofCommitment);
			sc.pushRightDelim();
			sc.pushInnerDelim();
			sc.pushLeftDelim();
			sc.pushObject(proofResponse);
			sc.pushRightDelim();
			sc.pushRightDelim();
			//right parenthesis
			sc.pushRightDelim();

			String res = sc.pullAll();

			r = vrfSchnorrSign(verificationKey, res, schnorrFirstValue, schnorrSecondValue, ebp.getElectionData().getElectionGenerator());
		} catch (NoSuchAlgorithmException | ElectionBoardServiceFault | UnsupportedEncodingException ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.SINGLE_BALLOT_SCHNORR_SIGN, r, ebp.getElectionID(), rn, it, EntityType.VOTERS);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_SCHNORR_SIGN);
			v.setReport(rep);
		}

		return v;

	}
}
