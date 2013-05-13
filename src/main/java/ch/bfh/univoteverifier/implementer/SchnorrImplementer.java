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
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.StringConcatenator;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * This class contains all the methods that need a Schnorr verification.
 *
 * @author snake
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
	 * @param publicKey the public key we must use to verify the signature.
	 * @param m the message to be verified.
	 * @param a the first value of a valid Schnorr signature.
	 * @param b the second value of a valid Schnorr signature.
	 * @return true if the verification succeed, false otherwise
	 * @throws NoSuchAlgorithmException if the hash function used in this
	 * method does not find the algorithm.
	 * @throws UnsupportedEncodingException if the hash function used in
	 * this method does not find the encoding.
	 */
	public boolean vrfSchnorrSign(BigInteger verificationKey, String message, BigInteger a, BigInteger b) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		//compute r = g^b * vk^a mod p
		BigInteger r = g.modPow(b, p).multiply(verificationKey.modPow(a, p)).mod(p);

		//concatenate clear text with r: (m|r)
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(message, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(r, StringConcatenator.RIGHT_DELIMITER);
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
	 * Verify the signature of the given Ballot.
	 *
	 * @param b the Ballot we want to verify the signature.
	 * @return true if the signature is verified correctly, false otherwise.
	 */
	public boolean vrfBallotSignature(Ballot b) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		VoterSignature signature = b.getSignature();

		//concatenate to (id|(firstValue|secondValue)|proof)
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(ebp.getElectionID(), StringConcatenator.INNER_DELIMITER);

		sc.pushLeftDelim();
		sc.pushObjectDelimiter(b.getEncryptedVote().getFirstValue(), StringConcatenator.INNER_DELIMITER);
		sc.pushObject(b.getEncryptedVote().getSecondValue());
		sc.pushRightDelim();

		sc.pushInnerDelim();
		sc.pushObjectDelimiter(b.getProof(), StringConcatenator.RIGHT_DELIMITER);

		//Timestamp for the schnorr signature??? - ToDo timestamp, and string format
		boolean signVrf = vrfSchnorrSign(b.getVerificationKey(), sc.pullAll(), signature.getFirstValue(), signature.getSecondValue());

		return signVrf;
	}
}
