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

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.math.BigInteger;

/**
 * This class is used to check the validity of the parameters.
 *
 * @author snake
 */
public class ParametersImplementer extends Implementer {

	private final int PRIME_NUMBER_CERTAINITY = 1000;

	/**
	 * Construct a new ParametersImplementer with a given election ID.
	 *
	 * @param ebp the ElectionBoardProxy used to get the data.
	 * @param rn the name of the runner who used this implementer.
	 */
	public ParametersImplementer(ElectionBoardProxy ebp, RunnerName rn) {
		super(ebp, rn);
	}

	/**
	 * Check if the parameters for the Schnorr's signature scheme are
	 * corrects by reading them from the configuration file.
	 *
	 * Specification: 1.3.1.
	 *
	 * @return a VerificationResult with the relative result.
	 */
	public VerificationResult vrfSchnorrParamLen(BigInteger p, BigInteger q, BigInteger g) throws ElectionBoardServiceFault {
		int lengthPG = 1024;
		int lengthQ = 256;

		boolean r = p.bitLength() == lengthPG && q.bitLength() == lengthQ && g.bitLength() == lengthPG;

		VerificationResult ve = new VerificationResult(VerificationType.SETUP_SCHNORR_PARAM_LEN, r, ebp.getElectionID(), rn);

		if (!r) {
			ve.setFailureCode(FailureCode.FALSE_PARAMETERS_LENGTH);
		}

		return ve;

	}

	/*
	 * Check the length of the ElGamal parameter p.
	 *
	 * Specification: 1.3.4, c.
	 *
	 * @return a VerificationResult with the relative result.
	 */
	public VerificationResult vrfElGamalParamLen(BigInteger p, int keyLength) {
		boolean r = p.bitLength() == keyLength;

		VerificationResult ve = new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAM_LEN, r, ebp.getElectionID(), rn);
		if (!r) {
			ve.setFailureCode(FailureCode.FALSE_PARAMETERS_LENGTH);
		}

		return ve;
	}

	/**
	 * Check if a number is a prime number.
	 *
	 * @return a VerificationResult with the relative result.
	 */
	public VerificationResult vrfPrime(BigInteger p, VerificationType type) throws ElectionBoardServiceFault {
		boolean r = p.isProbablePrime(PRIME_NUMBER_CERTAINITY);

		VerificationResult ve = new VerificationResult(type, r, ebp.getElectionID(), rn);

		if (!r) {
			ve.setFailureCode(FailureCode.COMPOSITE_PRIME_NUMBER);
		}

		return ve;
	}

	/**
	 * Check if p is a safe prime (p = k*q + 1).
	 *
	 * @return a VerificationResult with the relative result.
	 */
	public VerificationResult vrfSafePrime(BigInteger p, BigInteger q, VerificationType type) throws ElectionBoardServiceFault {
		//subtract one from p, now (p-1) must be divisible by q without rest
		BigInteger rest = p.subtract(BigInteger.ONE).mod(q);

		boolean r = rest.equals(BigInteger.ZERO);

		VerificationResult ve = new VerificationResult(type, r, ebp.getElectionID(), rn);

		if (!r) {
			ve.setFailureCode(FailureCode.NOT_SAFE_PRIME);
		}

		return ve;
	}

	/**
	 * Check if g is a generator of a subgroup H_q of G_q.
	 *
	 * @return a VerificationResult with the relative result.
	 */
	public VerificationResult vrfGenerator(BigInteger p, BigInteger q, BigInteger g, VerificationType type) throws ElectionBoardServiceFault {
		BigInteger res = g.modPow(q, p);

		boolean r = res.equals(BigInteger.ONE);
		VerificationResult ve = new VerificationResult(type, r, ebp.getElectionID(), rn);

		if (!r) {
			ve.setFailureCode(FailureCode.NOT_A_GENERATOR);
		}

		return ve;
	}
}
