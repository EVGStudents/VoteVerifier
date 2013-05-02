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
import ch.bfh.univoteverifier.common.StringConcatenator;
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
	private BigInteger p, q, g;

	/**
	 * Construct a Schnorr implementer.
	 *
	 * @param ebp the election board proxy from where get the data.
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
	 * @param signature the signature to check.
	 * @param message the content to sign.
	 * @param publicKey the public key used to verify the signature.
	 * @return return true if the signature is verified correctly, false
	 * otherwise.
	 */
	public boolean vrfSchnorrSign(DSAPublicKey publicKey, BigInteger clearText, BigInteger s, BigInteger e) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		BigInteger pubKeyValue = publicKey.getY();

		//compute r = g^s * y^e mod p - ToDO check s and e
		BigInteger r = g.modPow(s, p).multiply(pubKeyValue.modPow(e, p)).mod(p);

		//concatenate clear text with r: (clearText|r)
		StringConcatenator sc = new StringConcatenator();
		sc.pushLeftDelim();
		sc.pushObjectDelimiter(clearText, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(r, StringConcatenator.RIGHT_DELIMITER);
		String concat = sc.pullAll();

		//hashResult = sha-256(concat) mod q => this must be equal to e
		BigInteger hashResult = CryptoFunc.sha256(concat);

		//check that hashResult = e
		boolean res = hashResult.equals(e);

		return res;
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
}
