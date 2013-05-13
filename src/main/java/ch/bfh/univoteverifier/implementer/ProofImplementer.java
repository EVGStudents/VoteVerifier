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
import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.StringConcatenator;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * This class contains all the methods that need a NIZKP verification.
 *
 * @author snake
 */
public class ProofImplementer extends Implementer {

	/**
	 * Construct a new ProofImplementer with a given ElectionBoardProxy and
	 * RunnerName.
	 *
	 * @param ebp the ElectionBoardProxy used to download the data.
	 * @param rn the RunnerName who used this implementer.
	 */
	public ProofImplementer(ElectionBoardProxy ebp, RunnerName rn) {
		super(ebp, rn);
	}

	/**
	 * NIZKP for knowledge of discrete log.
	 *
	 * @param proof the Proof containing t and s.
	 * @param c the "c" value.
	 * @param param the paramter used to compute v and w.
	 * @param prime the prime number used for modulo operations.
	 * @return true if the prover knows the discrete log, false otherwise.
	 */
	public boolean knowledgeOfDiscreteLog(Proof proof, BigInteger c, BigInteger paramV, BigInteger paramW, BigInteger prime) {
		BigInteger t = proof.getCommitment().get(0);
		BigInteger s = proof.getResponse().get(0);

		//v = param^s mod prime
		BigInteger v = paramV.modPow(s, prime);

		System.out.println("v: " + v);

		//w = t*param^c mod prime
		BigInteger w = t.multiply(paramW.modPow(c, prime)).mod(prime);
		System.out.println("w: " + w);

		return v.equals(w);
	}

	/**
	 * NIZKP for equality of discrete log
	 *
	 * @param proof the Proof containing t and s.
	 * @param c the "c" value.
	 * @param paramV1 the parameter used to compute the first value of v.
	 * @param paramV2 the parameter used to compute the second value of v.
	 * @param paramW1 the parameter used to compute the first value of w.
	 * @param paramW2 the parameter used to compute the second value of w.
	 * @param prime the prime number used for modulo operations.
	 * @return true if the prover has verified the equality, false
	 * otherwise.
	 */
	public boolean equalityOfDiscreteLog(Proof proof, BigInteger c, BigInteger paramV1, BigInteger paramV2, BigInteger paramW1, BigInteger paramW2, BigInteger prime) {
		boolean result = false;

		List<BigInteger> t = proof.getCommitment();
		BigInteger s = proof.getResponse().get(0);

		//compute v = (v1, v2)
		BigInteger v1 = paramV1.modPow(s, Config.p);
		BigInteger v2 = paramV2.modPow(s, Config.p);

		//compute w = (w1, w2)
		BigInteger w1 = t.get(0).multiply(paramW1.modPow(c, Config.p)).mod(Config.p);
		BigInteger w2 = t.get(1).multiply(paramW2.modPow(c, Config.p)).mod(Config.p);

		return v1.equals(w1) && v2.equals(w2);
	}

	/**
	 * Verify the NIZKP of the Distributed Key Generation by the given
	 * tallier.
	 *
	 * Specification: 1.3.4, d.
	 *
	 * @param tallierName the name of the tallier.
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfDistributedKeyByProof(String tallierName) throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {

		EncryptionKeyShare eks = ebp.getEncryptionKeyShare(tallierName);

		//get the part of the key for this tallier
		BigInteger y_j = eks.getKey();

		//get the proof and its paramters
		Proof proof = eks.getProof();
		BigInteger t = proof.getCommitment().get(0);

		//concatenate to y_j|t|tallierName
		sc.pushObjectDelimiter(y_j, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(t, StringConcatenator.INNER_DELIMITER);
		sc.pushObject(tallierName);

		String res = sc.pullAll();

		//get the  ElGamal parameters
		BigInteger elGamalP = ebp.getEncryptionParameters().getPrime();
		BigInteger elGamalG = ebp.getEncryptionParameters().getGenerator();
		BigInteger elGamalQ = ebp.getEncryptionParameters().getGroupOrder();

		//c = H(y_j|t|tallierName) mod Q
		BigInteger c = CryptoFunc.sha256(res).mod(elGamalQ);

		boolean result = knowledgeOfDiscreteLog(proof, c, elGamalG, y_j, elGamalP);

		VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_T_NIZKP_OF_X, result, ebp.getElectionID(), rn, ImplementerType.NIZKP, EntityType.PARAMETER);
		vr.setEntityName(tallierName);

		if (!result) {
			vr.setFailureCode(FailureCode.INVALID_NIZKP);
		}

		return vr;
	}

	/**
	 * Verify the NIZKP of the elction generator for each mixer.
	 *
	 * Specification: 1.3.4, e.
	 *
	 * @param mixerName the name of the mixer.
	 * @param previousMixerName the name of the previous mixer in order to
	 * get the previous generator. Use "schnorr_generator" as name to get
	 * the Schnorr public generator.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfElectionGeneratorByProof(String mixerName, String previousMixerName) throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		BlindedGenerator bg = ebp.getBlindedGenerator(mixerName);
		BigInteger g_k = bg.getGenerator();

		//get the proof and its paramters
		Proof proof = bg.getProof();
		BigInteger t = proof.getCommitment().get(0);

		//concatenate to g_k|t|mixerName
		sc.pushObjectDelimiter(g_k, StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(t, StringConcatenator.INNER_DELIMITER);
		sc.pushObject(mixerName);

		String res = sc.pullAll();

		//c = H(g_k|t|mixerName_k) mod q
		BigInteger c = CryptoFunc.sha256(res).mod(Config.q);

		//g_k-1
		BigInteger previous_g_k;

		if (previousMixerName.equals("schnorr_generator")) {
			previous_g_k = Config.g;
		} else {
			previous_g_k = ebp.getBlindedGenerator(previousMixerName).getGenerator();
		}

		boolean result = knowledgeOfDiscreteLog(proof, c, previous_g_k, g_k, Config.p);

		VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_M_NIZKP_OF_ALPHA, result, ebp.getElectionID(), rn, ImplementerType.NIZKP, EntityType.PARAMETER);
		vr.setEntityName(mixerName);

		if (!result) {
			vr.setFailureCode(FailureCode.INVALID_NIZKP);
		}

		return vr;
	}

	/**
	 * Verify the NIZKP of the lately mixes verification keys.
	 *
	 * Specification: 1.3.6, a.
	 *
	 * @param mixerName the name of the mixer.
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	public VerificationResult vrfLatelyVerificationKeysProof(String mixerName) throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		List<MixedVerificationKey> mvk = ebp.getLatelyMixedVerificationKeysBy(mixerName);
		boolean result = false;

		//for each key in the set - ToDo check how to get the values from the previous mixer
		for (MixedVerificationKey key : mvk) {
			Proof proof = key.getProof();
			List<BigInteger> t = proof.getCommitment();
			BigInteger s = proof.getResponse().get(0);

			//change this when it will be available
			BigInteger g_k = BigInteger.ONE;
			BigInteger previous_g_k = BigInteger.ONE;
			BigInteger previous_key = BigInteger.ONE;

			//concatenate to g_k|vk_i,k|t|mixerName
			sc.pushObject(g_k);
			sc.pushInnerDelim();
			sc.pushObject(key);
			sc.pushInnerDelim();
			sc.pushList(t, true);
			sc.pushInnerDelim();
			sc.pushObject(mixerName);

			String res = sc.pullAll();

			BigInteger c = CryptoFunc.sha256(res).mod(Config.q);

			result = equalityOfDiscreteLog(proof, c, previous_g_k, previous_key, g_k, key.getKey(), Config.p);

			//if one does not succeed, break.
			if (!result) {
				break;
			}
		}

		VerificationResult vr = new VerificationResult(VerificationType.EL_PERIOD_M_NIZKP_EQUALITY_NEW_VRF, result, ebp.getElectionID(), rn, ImplementerType.NIZKP, EntityType.PARAMETER);
		vr.setEntityName(mixerName);
		vr.setImplemented(false);

		if (!result) {
			vr.setFailureCode(FailureCode.INVALID_NIZKP);
		}

		return vr;
	}

	/**
	 * Verify the NIZKP of the proof for a ballot.
	 *
	 * @param b the Ballot.
	 * @return true if the prove has been verified successfully, false
	 * otherwise.
	 */
	public boolean vrfBallotProof(Ballot b) throws NoSuchAlgorithmException, UnsupportedEncodingException, ElectionBoardServiceFault {
		Proof proof = b.getProof();
		BigInteger t = proof.getCommitment().get(0);
		BigInteger elGamalQ = ebp.getEncryptionParameters().getGroupOrder();
		BigInteger elGamalP = ebp.getEncryptionParameters().getPrime();
		BigInteger elGamalG = ebp.getEncryptionParameters().getGenerator();

		//concatenate to (a|t|vk)
		//where is a?? - ToDo also ask how the hash for a proof are concatenated
		sc.pushLeftDelim();
		sc.pushObjectDelimiter("a", StringConcatenator.INNER_DELIMITER);
		sc.pushObjectDelimiter(t, StringConcatenator.INNER_DELIMITER);
		sc.pushObject(b.getVerificationKey());
		sc.pushRightDelim();

		String res = sc.pullAll();

		BigInteger c = CryptoFunc.sha256(res).mod(elGamalQ);

		//replace BigInteger.ZERO with the a value
		boolean proofVrf = knowledgeOfDiscreteLog(proof, c, elGamalG, BigInteger.ZERO, elGamalP);

		return false;
	}
}
