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

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.DSAPublicKey;

/**
 * This class contains all the methods that need a Schnorr verification
 *
 * @author snake
 */
public class SchnorrImplementer {

	private final ElectionBoardProxy ebp;
	BigInteger p, q, g;

	/**
	 * Construct a Schnorr implementer.
	 *
	 * @param ebp the election board proxy from where get the data
	 */
	public SchnorrImplementer(ElectionBoardProxy ebp) {
		this.ebp = ebp;
		p = Config.p;
		q = Config.q;
		g = Config.g;
	}

	/**
	 * Verify the given Schnorr's signature against the hash we have
	 * computed.
	 *
	 * @param signature the signature to check
	 * @param message the content to sign
	 * @param publicKey the public key used to verify the signature
	 * @return boolean return true if the signature is verified correctly,
	 * false otherwise
	 */
	public boolean vrfSchnorrSign(DSAPublicKey publicKey, BigInteger clearText, BigInteger firstValue, BigInteger secondValue) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		BigInteger pubKeyValue = publicKey.getY();

		//r = g^s * y^e mod p - ToDO check if firstValue = s and secondValue = e
		BigInteger rValue = g.modPow(firstValue, Config.p).multiply(pubKeyValue.modPow(secondValue, p)).mod(p);

		//e_2 = sha-256(clearText|r) mod q => this must be equal to e
		BigInteger hashResult = CryptoFunc.sha256(clearText.toString() + rValue.toString());

		//check that e_2 = e
		boolean res = hashResult.equals(secondValue);

		return res;
	}

	/**
	 * Set a new value for p.
	 *
	 * @param p the p value of the Schnorr's parameters
	 */
	public void setP(BigInteger p) {
		this.p = p;
	}

	/**
	 * Set a new value for q.
	 *
	 * @param q the q value of the Schnorr's parameters
	 */
	public void setQ(BigInteger q) {
		this.q = q;
	}

	/**
	 * Set a new value for g.
	 *
	 * @param g the g value of the Schnorr's parameters
	 */
	public void setG(BigInteger g) {
		this.g = g;
	}
}
