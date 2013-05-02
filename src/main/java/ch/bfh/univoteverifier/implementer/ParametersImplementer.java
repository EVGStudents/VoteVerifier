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
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.math.BigInteger;

/**
 * This class is used to check the validity of the parameters.
 *
 * @author snake
 */
public class ParametersImplementer {

	private final int PRIME_NUMBER_CERTAINITY = 1000;
	private BigInteger p, q, g;

	public ParametersImplementer() {
		p = Config.p;
		q = Config.q;
		g = Config.g;
	}

	/**
	 * Check if the parameters for the Schnorr's signature scheme are
	 * corrects by reading them from the configuration file.
	 *
	 * @return boolean true if the parameters are correct, false otherwise.
	 */
	public VerificationEvent vrfParamLen() {
		int lengthPG = 1024;
		int lengthQ = 256;

		boolean r = p.bitLength() == lengthPG && q.bitLength() == lengthQ && g.bitLength() == lengthPG;

		VerificationEvent ve = new VerificationEvent(VerificationType.SETUP_PARAM_LEN, r);

		if (!r) {
			ve.setFailureCode(FailureCode.FALSE_PARAMETERS_LENGTH);
		}

		return ve;

	}

	/**
	 * Check if p is a prime number.
	 *
	 * @return true if p is prime, false otherwise.
	 */
	public VerificationEvent vrfPrimeP() {
		boolean r = p.isProbablePrime(PRIME_NUMBER_CERTAINITY);

		VerificationEvent ve = new VerificationEvent(VerificationType.SETUP_P_IS_PRIME, r);

		if (!r) {
			ve.setFailureCode(FailureCode.COMPOSITE_PRIME_NUMBER);
		}

		return ve;
	}

	/**
	 * Check if q is a prime number.
	 *
	 * @return true if q is prime, false otherwise.
	 */
	public VerificationEvent vrfPrimeQ() {
		boolean r = q.isProbablePrime(PRIME_NUMBER_CERTAINITY);

		VerificationEvent ve = new VerificationEvent(VerificationType.SETUP_Q_IS_PRIME, r);

		if (!r) {
			ve.setFailureCode(FailureCode.COMPOSITE_PRIME_NUMBER);
		}

		return ve;
	}

	/**
	 * Check if p is a safe prime (p = k*q + 1).
	 *
	 * @return true if p is a safe prime, false otherwise.
	 */
	public VerificationEvent vrfSafePrime() {
		//subtract one from p, now (p-1) must be divisible by q without rest
		BigInteger rest = p.subtract(BigInteger.ONE).mod(q);

		boolean r = rest.equals(BigInteger.ZERO);

		VerificationEvent ve = new VerificationEvent(VerificationType.SETUP_P_IS_SAFE_PRIME, r);

		if (!r) {
			ve.setFailureCode(FailureCode.NOT_SAFE_PRIME);
		}

		return ve;
	}

	/**
	 * Check if g is a generator of a subgroup H_q.
	 *
	 * @return if g is a valid generator.
	 */
	public VerificationEvent vrfGenerator() {
		BigInteger res = g.modPow(q, p);

		boolean r = res.equals(BigInteger.ONE);
		VerificationEvent ve = new VerificationEvent(VerificationType.SETUP_G_IS_GENERATOR, r);

		if (!r) {
			ve.setFailureCode(FailureCode.NOT_A_GENERATOR);
		}

		return ve;
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
