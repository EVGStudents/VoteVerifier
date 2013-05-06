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

import ch.bfh.univote.common.EncryptionKeyShare;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class contains all the methods that need a NIZKP verification.
 *
 * @author snake
 */
public class ProofImplementer {

	private static final Logger LOGGER = Logger.getLogger(ProofImplementer.class.getName());
	private final ElectionBoardProxy ebp;

	public ProofImplementer(ElectionBoardProxy ebp) {
		this.ebp = ebp;
	}

	public List<VerificationResult> vrfProofDistributedKeyGen() throws ElectionBoardServiceFault {

		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			EncryptionKeyShare eks = ebp.getEncryptionKeyShare(tName);

			BigInteger y_j = eks.getKey();
			Proof p = eks.getProof();

			//ToDo
			//we have (t,c,s) in the proof, getCommitment (t) and getResponse (s) where is the challenge c (univote-crypto.js)?.

		}
		return null;
	}
}
