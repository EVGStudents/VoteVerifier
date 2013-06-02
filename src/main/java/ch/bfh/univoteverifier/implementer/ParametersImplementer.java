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
import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.EncryptionKey;
import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.PartiallyDecryptedVotes;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Report;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.SOAPFaultException;

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
	 */
	public VerificationResult vrfSchnorrParamLen(BigInteger p, BigInteger q, BigInteger g) {
		int lengthPG = 1024;
		int lengthQ = 256;

		boolean r = p.bitLength() == lengthPG && q.bitLength() == lengthQ && g.bitLength() == lengthPG;

		VerificationResult ve = new VerificationResult(VerificationType.SETUP_SCHNORR_PARAM_LEN, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			ve.setReport(new Report(FailureCode.FALSE_PARAMETERS_LENGTH));
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
			ve.setReport(new Report(FailureCode.FALSE_PARAMETERS_LENGTH));
		}

		return ve;
	}

	/**
	 * Check if a number is a prime number.
	 *
	 * @return a VerificationResult with the relative result.
	 */
	public VerificationResult vrfPrime(BigInteger p, VerificationType type) {
		boolean r = p.isProbablePrime(PRIME_NUMBER_CERTAINITY);

		VerificationResult ve = new VerificationResult(type, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			ve.setReport(new Report(FailureCode.COMPOSITE_PRIME_NUMBER));
		}

		return ve;
	}

	/**
	 * Check if p is a safe prime (p = k*q + 1).
	 *
	 * @return a VerificationResult with the relative result.
	 */
	public VerificationResult vrfSafePrime(BigInteger p, BigInteger q, VerificationType type) {
		//subtract one from p, now (p-1) must be divisible by q without rest
		BigInteger rest = p.subtract(BigInteger.ONE).mod(q);

		boolean r = rest.equals(BigInteger.ZERO);

		VerificationResult ve = new VerificationResult(type, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			ve.setReport(new Report(FailureCode.NOT_SAFE_PRIME));
		}

		return ve;
	}

	/**
	 * Check if g is a generator of a subgroup H_q of G_q.
	 *
	 * @return a VerificationResult with the relative result.
	 */
	public VerificationResult vrfGenerator(BigInteger p, BigInteger q, BigInteger g, VerificationType type) {
		BigInteger res = g.modPow(q, p);

		boolean r = res.equals(BigInteger.ONE);
		VerificationResult ve = new VerificationResult(type, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (!r) {
			ve.setReport(new Report(FailureCode.NOT_A_GENERATOR));
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
	 */
	public VerificationResult vrfDistributedKey() {
		Exception exc = null;
		Report rep;
		boolean r = false;

		try {
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

			r = encKey.getKey().equals(resultEncKey.mod(elGamalP));
		} catch (ElectionBoardServiceFault ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_T_PUBLIC_KEY, r, ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.ENC_KEY_SHARE_NOT_EQUALS);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Check that the election generator g is equal to the blinded generator
	 * of the last mixer.
	 *
	 * Specification: 1.3.4, e.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfElectionGenerator() {
		Exception exc = null;
		Report rep;
		boolean r = false;

		try {
			ElectionGenerator eg = ebp.getElectionGenerator();
			BigInteger egValue = eg.getGenerator();

			List<String> mixersName = ebp.getElectionDefinition().getMixerId();
			String lastMixer = mixersName.get(mixersName.size() - 1);

			BlindedGenerator bg = ebp.getBlindedGenerator(lastMixer);
			BigInteger bgValue = bg.getGenerator();

			//check that g^ == g_m
			r = egValue.equals(bgValue);
		} catch (ElectionBoardServiceFault ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.EL_SETUP_ANON_GEN, r, ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.ELECTION_GEN_NOT_EQUALS);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Check the set of public mixed verification keys against the set of
	 * public keys of last mixer.
	 *
	 * Specification: 1.3.5, d.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfVerificationKeysMixed() {
		Exception exc = null;
		boolean r = false;
		Report rep;

		try {
			MixedVerificationKeys vk = ebp.getMixedVerificationKeys();

			//get the verification key of the last mixer
			List<String> mixerID = ebp.getElectionDefinition().getMixerId();
			String lastMixer = mixerID.get(mixerID.size() - 1);
			MixedVerificationKeys lastMixerKeys = ebp.getMixedVerificationKeysBy(lastMixer);

			//check that vk' == vk_m
			r = vk.equals(lastMixerKeys);

		} catch (NullPointerException | ElectionBoardServiceFault ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.EL_PREP_PUB_VER_KEYS, r, ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.SET_VERIFICATION_KEYS_NOT_EQUALS);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Check the set of lately mixed verification keys against the set of
	 * public lately mixed verification keys of last mixer.
	 *
	 * Specification: 1.3.6, a.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfLatelyVerificatonKeys() {
		Exception exc = null;
		boolean r = false;
		Report rep;

		try {
			List<MixedVerificationKey> mk = ebp.getLateyMixedVerificationKeys();

			//get the lately verifcation keys of the last mixer
			List<String> mixerID = ebp.getElectionDefinition().getMixerId();
			List<MixedVerificationKey> lastMixerKeys = ebp.getLatelyMixedVerificationKeysBy(mixerID.get(mixerID.size() - 1));

			//check that vk'_i = vk_i,m
			for (int i = 0; i < mk.size(); i++) {
				if (mk.get(i).getKey().equals(lastMixerKeys.get(i).getKey())) {
					r = true;
				} else {
					r = false;
					break;
				}

			}
		} catch (NullPointerException | SOAPFaultException | ElectionBoardServiceFault ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.EL_PERIOD_NEW_VER_KEY, r, ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.NEW_SET_VERIFICATION_KEYS_NOT_EQUALS);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Check the set of late renewal of registration mixed verification keys
	 * against the set of public late renewal verification keys of last
	 * mixer.
	 *
	 * Specification: 1.3.6, b.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfLateRenewalOfRegistrationKeys() {
		VerificationResult v = new VerificationResult(VerificationType.EL_PERIOD_M_NIZKP_EQUALITY_LATEREN, false, ebp.getElectionID(), rn, it, EntityType.EM);
		v.setImplemented(false);

		Report rep = new Report(FailureCode.NOT_YET_IMPLEMENTED);
		rep.setAdditionalInformation("This verification is not yet implemented due the lack of necessary data.");
		v.setReport(rep);

		return v;
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
	public VerificationResult vrfBallotVerificationKey(BigInteger verificationKey) {
		Exception exc = null;
		boolean r = false;
		Report rep;

		try {
			MixedVerificationKeys mvk = ebp.getMixedVerificationKeys();

			//check that vk belongs to VK'
			for (BigInteger key : mvk.getKey()) {
				if (key.equals(verificationKey)) {
					r = true;
					break;
				}
			}
		} catch (ElectionBoardServiceFault ex) {
			exc = ex;
		}

		//check that no other recent ballots contain this vk -  Not yet available

		//if vk is in the late renewal key set, check that no other recent ballots contain this v in the late renewal key set -  Not yet availble

		VerificationResult v = new VerificationResult(VerificationType.SINGLE_BALLOT_VERIFICATION_KEY, r, ebp.getElectionID(), rn, it, EntityType.VOTERS);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_BALLOT_VK);
			v.setReport(rep);
		}

		return v;
	}

	/**
	 * Check the set of mixed encrypted votes against the one of the last
	 * mixer.
	 *
	 * Specification: 1.3.7, a.
	 *
	 * @return a VerificationResult.
	 */
	public VerificationResult vrfMixedEncryptedVotes() {
		Exception exc = null;
		boolean r = false;
		Report rep;

		try {
			EncryptedVotes mev = ebp.getEncryptedVotes();

			//get last mixer encrypted votes
			List<String> mixersName = ebp.getElectionDefinition().getMixerId();
			MixedEncryptedVotes lastMixerEncVotes = ebp.getMixedEncryptedVotesBy(mixersName.get(mixersName.size() - 1));

			//check if the two set correspond
			r = mev.getVote().equals(lastMixerEncVotes.getVote());

		} catch (NullPointerException | SOAPFaultException | ElectionBoardServiceFault ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.MT_ENC_VOTES_SET, r, ebp.getElectionID(), rn, it, EntityType.EA);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.ENCRYPTED_VOTES_NOT_EQUALS);
			v.setReport(rep);
		}

		return v;
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
	public VerificationResult vrfVotes() {
		Exception exc = null;
		boolean r = false;
		Report rep;

		try {
			Ballots ballots = ebp.getBallots();
			BigInteger elGamalP = ebp.getEncryptionParameters().getPrime();
			BigInteger elGamalQ = ebp.getEncryptionParameters().getGroupOrder();

			for (int i = 0; i < ballots.getBallot().size(); i++) {
				Ballot b = ballots.getBallot().get(i);
				BigInteger bValue = b.getEncryptedVote().getSecondValue();

				BigInteger aProducts = BigInteger.ONE;

				//get the a value for each tallier
				for (int j = 0; j < ebp.getElectionDefinition().getTallierId().size(); j++) {
					String tName = ebp.getElectionDefinition().getTallierId().get(j);
					PartiallyDecryptedVotes pdv = ebp.getPartiallyDecryptedVotes(tName);

					//get the a value for this tallier
					BigInteger aValue = pdv.getVote().get(i);

					//multiply with other tallier a value
					aProducts = aProducts.multiply(aValue);
				}

				BigInteger m = bValue.multiply(aProducts).mod(elGamalP);


				//compute G^-1
				BigInteger mInverse;
				if (m.compareTo(elGamalQ) == -1 || m.compareTo(elGamalQ) == 0) {
					mInverse = m.subtract(BigInteger.ONE);
				} else {
					mInverse = elGamalP.subtract(m).subtract(BigInteger.ONE);
				}



				//ToDo decode the vote


			}
		} catch (NullPointerException | SOAPFaultException | ElectionBoardServiceFault ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.MT_VALID_PLAINTEXT_VOTES, false, ebp.getElectionID(), rn, it, EntityType.EM);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.INVALID_VOTES);
			v.setReport(rep);
		}

		return v;
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
	public VerificationResult vrfBallotInSet(BigInteger verificationKey) {
		Exception exc = null;
		boolean r = false;
		Report rep;

		try {
			Ballot qrCodeBallot = ebp.getBallot(verificationKey);

			//check if the ballot belongs to the set of all ballots
			for (Ballot b : ebp.getBallots().getBallot()) {
				if (qrCodeBallot.equals(b)) {
					r = true;
					break;
				}
			}
		} catch (ElectionBoardServiceFault ex) {
			exc = ex;
		}

		VerificationResult v = new VerificationResult(VerificationType.SINGLE_BALLOT_IN_BALLOTS, r, ebp.getElectionID(), rn, it, EntityType.PARAMETER);

		if (exc != null) {
			rep = new Report(exc);
			v.setReport(rep);
		} else if (!r) {
			rep = new Report(FailureCode.BALLOT_NOT_IN_SET);
			v.setReport(rep);
		}

		return v;
	}
}
