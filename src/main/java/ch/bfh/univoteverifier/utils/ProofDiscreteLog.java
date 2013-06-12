/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.utils;

import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.Config;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * This class contains the components of a zero knowledge proof and methods to
 * generate certain kinds of proofs. The class is used for tests
 *
 * @author prinstin
 */
public class ProofDiscreteLog {

	private String name;
	private BigInteger t;
	private BigInteger c;
	private BigInteger s;
	private BigInteger vk;
	private BigInteger q;
	private BigInteger p;
	private BigInteger g;

	public ProofDiscreteLog() {
	}

	public ProofDiscreteLog(String s) {
		this.name = s;
	}

	@Override
	public String toString() {
		String s = "\n\tProof t= " + this.t + "    c= " + this.c + "    s= " + this.s + "    vk= " + this.vk;
		s += "\n\tParameters p= " + this.p + "    q= " + this.q + "    g= " + this.g;
		if (name != null) {
			s = "\t" + this.name + s;
		}
		return s;
	}

	/**
	 * Generate a mock Proof object to use to test the verification method
	 * the values in the proof are small
	 *
	 * @return a mock object that contains the components of a zero
	 * knowledge proof
	 * @throws NoSuchAlgorithmException if
	 * MessageDigest.getInstance(Config.hashAlgorithm) cannot find the gives
	 * hash algorithm
	 */
	public ProofDiscreteLog getProofSmall() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//set public parameters
		q = new BigInteger("11");
		p = new BigInteger("23");
		g = new BigInteger("2");

		//set private and public verification key
		BigInteger sk = new BigInteger("4");
		vk = g.modPow(sk, p);

		//calculate the proof
		//Random r = new Random();
		//BigInteger w= new BigInteger (String.valueOf(r.nextInt()));
		BigInteger w = new BigInteger("3");

		this.name = "Mock Proof with small values for testing";
		//Compute t = g ^ w mod p
		this.t = g.modPow(w, p);
		// the concatenation is currently rudimentary
		BigInteger[] concatB = {vk, t};
		this.c = concatArrayContents(concatB);
		this.s = w.add(this.c.multiply(sk).mod(q));
		return this;
	}

	/**
	 * Generate a mock Proof object to use to test the verification method
	 * the values in the proof are small
	 *
	 * @return a mock object that contains the components of a zero
	 * knowledge proof
	 * @throws NoSuchAlgorithmException if
	 * MessageDigest.getInstance(Config.hashAlgorithm) cannot find the gives
	 * hash algorithm
	 */
	public ProofDiscreteLog getProofLarge() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//set public parameters
		q = Config.q;
		p = Config.q;
		g = Config.g;

		//set private and public verification key
		BigInteger sk = new BigInteger("4");
		vk = g.modPow(sk, p);

		//calculate the proof
		//Random r = new Random();
		//BigInteger w= new BigInteger (String.valueOf(r.nextInt()));
		BigInteger w = new BigInteger("3");

		this.name = "Mock Proof with real values for testing";
		//Compute t = g ^ w mod p
		this.t = g.modPow(w, p);

		BigInteger[] concatB = {vk, t};
		this.c = concatArrayContents(concatB);

		this.s = w.add(this.c.multiply(sk).mod(q));
		return this;
	}

	/**
	 * get the name of this proof
	 *
	 * @return String the name of the proof
	 */
	public String getName() {
		return name;
	}

	/**
	 * get the t value for the proof
	 *
	 * @return BigInteger : the t value
	 */
	public BigInteger getT() {
		return t;
	}

	/**
	 * set the t value for the proof
	 */
	public void setT(BigInteger t) {
		this.t = t;
	}

	/**
	 * get the c value for the proof
	 *
	 * @return BigInteger : the c value
	 */
	public BigInteger getC() {
		return c;
	}

	/**
	 * set the c value for the proof
	 */
	public void setC(BigInteger c) {
		this.c = c;
	}

	/**
	 * get the s value for the proof
	 *
	 * @return BigInteger : the s value
	 */
	public BigInteger getS() {
		return s;
	}

	/**
	 * set the s value for the proof
	 */
	public void setS(BigInteger s) {
		this.s = s;
	}

	/**
	 * get the vk value for the proof
	 *
	 * @return BigInteger : the vk value
	 */
	public BigInteger getVk() {
		return vk;
	}

	/**
	 * get the q value for the proof
	 *
	 * @return BigInteger : the q value
	 */
	public BigInteger getQ() {
		return q;
	}

	/**
	 * get the p value for the proof
	 *
	 * @return BigInteger : the p value
	 */
	public BigInteger getP() {
		return p;
	}

	/**
	 * get the g value for the proof
	 *
	 * @return BigInteger : the g value
	 */
	public BigInteger getG() {
		return g;
	}

	/**
	 * Concatenates n given BigInteger values into a string and pads them
	 * with the arbitrary string 001100.
	 *
	 * @param c an array of BigInteger values
	 * @return and String value of the concatenated contents of the array
	 */
	public BigInteger concatArrayContents(BigInteger[] c) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String concat = "";
		for (BigInteger ci : c) {
			//001100 similates padding
			concat += ci.toString() + "001100";
		}
		return CryptoFunc.sha256(concat);

	}
}
