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
import ch.bfh.univote.common.BlindedGenerator;
import ch.bfh.univote.common.DecodedVotes;
import ch.bfh.univote.common.ElectionGenerator;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.math.BigInteger;
import java.util.List;

/**
 * This class is used to check the validity of the parameters, like ElGamal,
 * Schnorr or other things, like encryption key.
 *
 * @author Scalzi Giuseppe
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
		super(ebp, rn, ImplementerType.PARAMETER);
	}

	/**
	 * Check if the parameters for the Schnorr's signature scheme are
	 * corrects by reading them from the configuration file.
	 *
	 * Specification: 1.3.1.
	 *
	 * @return a VerificationResult with the relative result.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfSchnorrParamLen(BigInteger p, BigInteger q, BigInteger g) throws ElectionBoardServiceFault {
		int lengthPG = 1024;
		int lengthQ = 256;

		boolean r = p.bitLength() == lengthPG && q.bitLength() == lengthQ && g.bitLength() == lengthPG;

		VerificationResult ve = new VerificationResult(VerificationType.SETUP_SCHNORR_PARAM_LEN, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			ve.setFailureCode(FailureCode.FALSE_PARAMETERS_LENGTH);
		}

		return ve;

	}

	/**
	 * Check the length of the ElGamal parameter p.
	 *
	 * Specification: 1.3.4, c.
	 *
	 * @return a VerificationResult with the relative result.
	 */
	public VerificationResult vrfElGamalParamLen(BigInteger p, int keyLength) {
		boolean r = p.bitLength() == keyLength;

		VerificationResult ve = new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAM_LEN, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			ve.setFailureCode(FailureCode.FALSE_PARAMETERS_LENGTH);
		}

		return ve;
	}

	/**
	 * Check if a number is a prime number.
	 *
	 * @return a VerificationResult with the relative result.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfPrime(BigInteger p, VerificationType type) throws ElectionBoardServiceFault {
		boolean r = p.isProbablePrime(PRIME_NUMBER_CERTAINITY);

		VerificationResult ve = new VerificationResult(type, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			ve.setFailureCode(FailureCode.COMPOSITE_PRIME_NUMBER);
		}

		return ve;
	}

	/**
	 * Check if p is a safe prime (p = k*q + 1).
	 *
	 * @return a VerificationResult with the relative result.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfSafePrime(BigInteger p, BigInteger q, VerificationType type) throws ElectionBoardServiceFault {
		//subtract one from p, now (p-1) must be divisible by q without rest
		BigInteger rest = p.subtract(BigInteger.ONE).mod(q);

		boolean r = rest.equals(BigInteger.ZERO);

		VerificationResult ve = new VerificationResult(type, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			ve.setFailureCode(FailureCode.NOT_SAFE_PRIME);
		}

		return ve;
	}

	/**
	 * Check if g is a generator of a subgroup H_q of G_q.
	 *
	 * @return a VerificationResult with the relative result.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfGenerator(BigInteger p, BigInteger q, BigInteger g, VerificationType type) throws ElectionBoardServiceFault {
		BigInteger res = g.modPow(q, p);

		boolean r = res.equals(BigInteger.ONE);
		VerificationResult ve = new VerificationResult(type, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			ve.setFailureCode(FailureCode.NOT_A_GENERATOR);
		}

		return ve;
	}

	/**
	 * Check that the distributed key of each mixer y_j, correspond to the
	 * general distribution key y.
	 *
	 * Specification: 1.3.4, d.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfDistributedKey() throws ElectionBoardServiceFault {
		EncryptionKey encKey = ebp.getEncryptionKey();
		BigInteger resultEncKey = null;
		BigInteger elGamalP = ebp.getEncryptionParameters().getPrime();

		//compute y_j1 * ... * y_jn mod P
		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			EncryptionKeyShare encKeyShare = ebp.getEncryptionKeyShare(tName);

			if (resultEncKey == null) {
				resultEncKey = encKeyShare.getKey();
			} else {
				resultEncKey = resultEncKey.multiply(encKeyShare.getKey());
			}
		}

		boolean r = encKey.getKey().equals(resultEncKey.mod(elGamalP));

		VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_T_PUBLIC_KEY, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			vr.setFailureCode(FailureCode.ENC_KEY_SHARE_NOT_EQUALS);
		}

		return vr;
	}

	/**
	 * Check that the election generator g is equal to the blinded generator
	 * of the last mixer.
	 *
	 * Specification: 1.3.4, e.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfElectionGenerator() throws ElectionBoardServiceFault {
		ElectionGenerator eg = ebp.getElectionGenerator();
		BigInteger egValue = eg.getGenerator();

		List<String> mixersName = ebp.getElectionDefinition().getMixerId();
		String lastMixer = mixersName.get(mixersName.size() - 1);

		BlindedGenerator bg = ebp.getBlindedGenerator(lastMixer);
		BigInteger bgValue = bg.getGenerator();

		//check that g^ == g_m
		boolean r = egValue.equals(bgValue);

		VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_ANON_GEN, r, ebp.getElectionID(), rn, it, EntityType.EM);
		if (!r) {
			vr.setFailureCode(FailureCode.ELECTION_GEN_NOT_EQUALS);
		}

		return vr;
	}

	/**
	 * Check the set of public mixed verification keys against the set of
	 * public keys of last mixer.
	 *
	 * Specification: 1.3.5, d.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfVerificationKeysMixed() throws ElectionBoardServiceFault {
		MixedVerificationKeys vk = ebp.getMixedVerificationKeys();

		//get the verification key of the last mixer
		List<String> mixerID = ebp.getElectionDefinition().getMixerId();
		String lastMixer = mixerID.get(mixerID.size() - 1);
		MixedVerificationKeys lastMixerKeys = ebp.getMixedVerificationKeysBy(lastMixer);

		//check that vk' == vk_m
		boolean r = vk.equals(lastMixerKeys);

		VerificationResult vr = new VerificationResult(VerificationType.EL_PREP_PUB_VER_KEYS, r, ebp.getElectionID(), rn, it, EntityType.EM);

		if (!r) {
			vr.setFailureCode(FailureCode.SET_VERIFICATION_KEYS_NOT_EQUALS);
		}

		return vr;
	}

	/**
	 * Check the set of lately mixed verification keys against the set of
	 * public lately mixed verification keys of last mixer.
	 *
	 * Specification: 1.3.6, a.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfLatelyVerificatonKeys() throws ElectionBoardServiceFault {
		List<MixedVerificationKey> mk = ebp.getLateyMixedVerificationKeys();

		//get the lately verifcation keys of the last mixer
		List<String> mixerID = ebp.getElectionDefinition().getMixerId();
		List<MixedVerificationKey> lastMixerKeys = ebp.getLatelyMixedVerificationKeysBy(mixerID.get(mixerID.size() - 1));

		//check that vk'_i = vk_i,m
		boolean r = mk.equals(lastMixerKeys);

		VerificationResult vr = new VerificationResult(VerificationType.EL_PERIOD_NEW_VER_KEY, r, ebp.getElectionID(), rn, it, EntityType.EM);

		if (!r) {
			vr.setFailureCode(FailureCode.NEW_SET_VERIFICATION_KEYS_NOT_EQUALS);
		}

		return vr;
	}

	/**
	 * Check the verification key of a ballot by performing some checks,
	 * like if the key belong to the set of verification keys.
	 *
	 * Specification: 1.3.6, d.
	 *
	 * @param b the Ballot where we can find the verification key to be
	 * verified.
	 * @return true if the checks have been successfully executed.
	 */
	public VerificationResult vrfBallotVerificationKey(BigInteger verificationKey) throws ElectionBoardServiceFault {
		MixedVerificationKeys mvk = ebp.getMixedVerificationKeys();
		boolean result = false;

		//check that vk belongs to VK'
		for (BigInteger key : mvk.getKey()) {
			if (key.equals(verificationKey)) {
				result = true;
				break;
			}

		}

		//check that no other recent ballots contain this vk - ToDo

		//if vk is in the late renewal key set, check that no other recent ballots contain this v in the late renewal key set - ToDo

		VerificationResult vr = new VerificationResult(VerificationType.SINGLE_BALLOT_VERIFICATION_KEY, result, ebp.getElectionID(), rn, it, EntityType.VOTERS);

		if (!result) {
			vr.setFailureCode(FailureCode.INVALID_BALLOT_VK);
		}

		return vr;
	}

	/**
	 * Check the set of mixed encrypted votes against the one of the last
	 * mixer.
	 *
	 * Specification: 1.3.7, a.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfMixedEncryptedVotes() throws ElectionBoardServiceFault {
		MixedEncryptedVotes mev = ebp.getMixedEncryptedVotes();

		//get last mixer encrypted votes
		List<String> mixersName = ebp.getElectionDefinition().getMixerId();
		MixedEncryptedVotes lastMixerEncVotes = ebp.getMixedEncryptedVotesBy(mixersName.get(mixersName.size() - 1));

		//check if the two set correspond
		boolean r = mev.equals(lastMixerEncVotes);

		VerificationResult vr = new VerificationResult(VerificationType.MT_ENC_VOTES_SET, r, ebp.getElectionID(), rn, it, EntityType.EA);

		if (!r) {
			vr.setFailureCode(FailureCode.ENCRYPTED_VOTES_NOT_EQUALS);
		}

		return vr;
	}

	/**
	 * Verify the validity of the ballots.
	 *
	 * Specification: 1.3.7, b.
	 *
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfVotes() throws ElectionBoardServiceFault {
		Ballots ballots = ebp.getBallots();
		BigInteger elGamalP = ebp.getEncryptionParameters().getPrime();


		for (int i = 0; i < ballots.getBallot().size(); i++) {
			Ballot b = ballots.getBallot().get(i);
			BigInteger bValue = b.getEncryptedVote().getSecondValue();

			BigInteger m = bValue.mod(elGamalP);

			//get the a value for each tallier
			for (int j = 0; j < ebp.getElectionDefinition().getTallierId().size(); i++) {
				String tName = ebp.getElectionDefinition().getTallierId().get(j);
				PartiallyDecryptedVotes pdv = ebp.getPartiallyDecryptedVotes(tName);

				//get the a value
				BigInteger aValue = pdv.getVote().get(i);
				m = m.multiply(aValue).mod(elGamalP);
			}

			//ToDo
			System.out.println(m);


		}

		VerificationResult vr = new VerificationResult(VerificationType.MT_VALID_PLAINTEXT_VOTES, false, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		//set failure code

		return vr;
	}

	/**
	 * Verify that a ballot from a verification key belongs to the set of
	 * all ballots.
	 *
	 * Specification: individual verification.
	 *
	 * @param verificationKey the verification key of the ballot.
	 * @return a VerificationResult.
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	public VerificationResult vrfBallotInSet(BigInteger verificationKey) throws ElectionBoardServiceFault {
		Ballot qrCodeBallot = ebp.getBallot(verificationKey);
		boolean r = false;

		//check if the ballot belongs to the set of all ballots
		for (Ballot b : ebp.getBallots().getBallot()) {
			if (qrCodeBallot.equals(b)) {
				r = true;
				break;
			}
		}

		VerificationResult vr = new VerificationResult(VerificationType.SINGLE_BALLOT_IN_BALLOTS, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			vr.setFailureCode(FailureCode.BALLOT_NOT_IN_SET);
		}

		return vr;
	}
}
