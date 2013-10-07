/*
 *
 *  Copyright (c) 2013 Be RunnerName.ELECTION_PERIODer Fachhochschule, Switzerland.
 *   Be RunnerName.ELECTION_PERIOD University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.verificationtest;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.UniversalVerification;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * This class test the behavior of a UniversalVerification.
 *
 * @author Scalzi Giuseppe
 */
public class UniversalVerificationTester {

	private final UniversalVerification v;
	private final List<VerificationResult> mockList;
	private final List<VerificationResult> realList;
	private final String eID = "vsbfh-2013";
	private final ElectionBoardProxy ebp;

	public UniversalVerificationTester() throws FileNotFoundException, ElectionBoardServiceFault {
		ebp = new ElectionBoardProxy(eID, true);
		v = new UniversalVerification(new Messenger(), eID, true);
		realList = v.runVerification();

		mockList = new ArrayList<VerificationResult>();
		buildMockList();
	}

	/**
	 * Build the mock list.
	 *
	 * @throws ElectionBoardServiceFault
	 */
	private void buildMockList() throws ElectionBoardServiceFault {
		//System Setup
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_P, true, eID, RunnerName.SYSTEM_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_Q, true, eID, RunnerName.SYSTEM_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_G, true, eID, RunnerName.SYSTEM_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_P_SAFE_PRIME, true, eID, RunnerName.SYSTEM_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SETUP_SCHNORR_PARAM_LEN, true, eID, RunnerName.SYSTEM_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.SETUP_CA_CERT, true, eID, RunnerName.SYSTEM_SETUP, ImplementerType.CERTIFICATE, EntityType.CA));
		mockList.add(new VerificationResult(VerificationType.SETUP_EM_CERT, true, eID, RunnerName.SYSTEM_SETUP, ImplementerType.CERTIFICATE, EntityType.EM));

		//Election Setup
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_EA_CERT, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.CERTIFICATE, EntityType.CA));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_EA_CERT_ID_SIGN, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.RSA, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_BASICS_PARAMS_SIGN, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.RSA, EntityType.EA));

		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_TALLIERS_CERT, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.CERTIFICATE, EntityType.TALLIER);
			vr.setEntityName(tName);
			mockList.add(vr);
		}

		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_MIXERS_CERT, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.CERTIFICATE, EntityType.MIXER);
			vr.setEntityName(mName);
			mockList.add(vr);
		}
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_T_CERT_M_CERT_ID_SIGN, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.RSA, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_P, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_Q, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_G, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_SAFE_PRIME, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAM_LEN, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ELGAMAL_PARAMS_SIGN, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.RSA, EntityType.EM));

		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_T_NIZKP_OF_X, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.NIZKP, EntityType.PARAMETER);
			vr.setEntityName(tName);
			mockList.add(vr);

			VerificationResult vrSign = new VerificationResult(VerificationType.EL_SETUP_T_NIZKP_OF_X_SIGN, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.RSA, EntityType.TALLIER);
			vrSign.setEntityName(tName);
			mockList.add(vrSign);
		}

		mockList.add(new VerificationResult(VerificationType.EL_SETUP_T_PUBLIC_KEY, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_T_PUBLIC_KEY_SIGN, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.RSA, EntityType.EM));

		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = new VerificationResult(VerificationType.EL_SETUP_M_NIZKP_OF_ALPHA, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.NIZKP, EntityType.PARAMETER);
			vr.setEntityName(mName);
			mockList.add(vr);

			VerificationResult vrSign = new VerificationResult(VerificationType.EL_SETUP_M_NIZKP_OF_ALPHA_SIGN, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.RSA, EntityType.MIXER);
			vrSign.setEntityName(mName);
			mockList.add(vrSign);
		}

		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ANON_GEN, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.EL_SETUP_ANON_GEN_SIGN, true, eID, RunnerName.ELECTION_SETUP, ImplementerType.RSA, EntityType.EM));

		//ElectionPreparation
		mockList.add(new VerificationResult(VerificationType.EL_PREP_C_AND_R_SIGN, true, ebp.getElectionID(), RunnerName.ELECTION_PREPARATION, ImplementerType.RSA, EntityType.EA));
		mockList.add(new VerificationResult(VerificationType.EL_PREP_EDATA_SIGN, true, ebp.getElectionID(), RunnerName.ELECTION_PREPARATION, ImplementerType.RSA, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.EL_PREP_ELECTORAL_ROLL_SIGN, true, ebp.getElectionID(), RunnerName.ELECTION_PREPARATION, ImplementerType.RSA, EntityType.EA));
		mockList.add(new VerificationResult(VerificationType.EL_PREP_VOTERS_CERT, true, ebp.getElectionID(), RunnerName.ELECTION_PREPARATION, ImplementerType.CERTIFICATE, EntityType.CA));
		mockList.add(new VerificationResult(VerificationType.EL_PREP_VOTERS_CERT_SIGN, false, ebp.getElectionID(), RunnerName.ELECTION_PREPARATION, ImplementerType.RSA, EntityType.CA));

		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult v = new VerificationResult(VerificationType.EL_PREP_M_PUB_VER_KEYS, true, ebp.getElectionID(), RunnerName.ELECTION_PREPARATION, ImplementerType.PARAMETER, EntityType.PARAMETER);
			v.setEntityName(mName);
			mockList.add(v);

			VerificationResult vSign = new VerificationResult(VerificationType.EL_PREP_M_PUB_VER_KEYS_SIGN, true, ebp.getElectionID(), RunnerName.ELECTION_PREPARATION, ImplementerType.RSA, EntityType.MIXER);
			vSign.setEntityName(mName);
			mockList.add(vSign);
		}

		mockList.add(new VerificationResult(VerificationType.EL_PREP_PUB_VER_KEYS, true, ebp.getElectionID(), RunnerName.ELECTION_PREPARATION, ImplementerType.PARAMETER, EntityType.EM));
		mockList.add(new VerificationResult(VerificationType.EL_PREP_PUB_VER_KEYS_SIGN, true, ebp.getElectionID(), RunnerName.ELECTION_PREPARATION, ImplementerType.RSA, EntityType.EM));

		//ElectionPeriod
		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_LATE_NEW_VOTER_CERT, true, eID, RunnerName.ELECTION_PERIOD, ImplementerType.CERTIFICATE, EntityType.CA));
		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_LATE_NEW_VOTER_CERT_SIGN, true, eID, RunnerName.ELECTION_PERIOD, ImplementerType.RSA, EntityType.EM));

		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult v = new VerificationResult(VerificationType.EL_PERIOD_M_NIZKP_EQUALITY_NEW_VRF, false, ebp.getElectionID(), RunnerName.ELECTION_PERIOD, ImplementerType.NIZKP, EntityType.PARAMETER);
			v.setEntityName(mName);
			v.setImplemented(false);

			mockList.add(v);

			VerificationResult vSign = new VerificationResult(VerificationType.EL_PERIOD_M_NIZKP_EQUALITY_NEW_VRF_SIGN, true, ebp.getElectionID(), RunnerName.ELECTION_PERIOD, ImplementerType.RSA, EntityType.MIXER);
			vSign.setEntityName(mName);
			mockList.add(vSign);
		}

		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_NEW_VER_KEY, true, ebp.getElectionID(), RunnerName.ELECTION_PERIOD, ImplementerType.PARAMETER, EntityType.EM));

		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_NEW_VER_KEY_SIGN, true, ebp.getElectionID(), RunnerName.ELECTION_PERIOD, ImplementerType.RSA, EntityType.EM));

		//ToDo - Check M7,M8, EM16, EM17

		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_BALLOT, true, ebp.getElectionID(), RunnerName.ELECTION_PERIOD, ImplementerType.PARAMETER, EntityType.VOTERS));

		mockList.add(new VerificationResult(VerificationType.EL_PERIOD_BALLOT_SIGN, true, ebp.getElectionID(), RunnerName.ELECTION_PERIOD, ImplementerType.RSA, EntityType.EM));

		//Mixer and Tallier
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult v1 = new VerificationResult(VerificationType.MT_M_ENC_VOTES_SET, true, ebp.getElectionID(), RunnerName.MIXING_TALLING, ImplementerType.NIZKP, EntityType.PARAMETER);
			v1.setEntityName(mName);
			mockList.add(v1);

			VerificationResult v2 = new VerificationResult(VerificationType.MT_M_ENC_VOTES_SET_SIGN, true, ebp.getElectionID(), RunnerName.MIXING_TALLING, ImplementerType.RSA, EntityType.MIXER);
			v2.setEntityName(mName);
		}

		mockList.add(new VerificationResult(VerificationType.MT_ENC_VOTES_SET, true, ebp.getElectionID(), RunnerName.MIXING_TALLING, ImplementerType.PARAMETER, EntityType.EA));

		mockList.add(new VerificationResult(VerificationType.MT_ENC_VOTES_ID_SIGN, true, ebp.getElectionID(), RunnerName.MIXING_TALLING, ImplementerType.RSA, EntityType.EA));

		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = new VerificationResult(VerificationType.MT_T_NIZKP_OF_X, true, ebp.getElectionID(), RunnerName.MIXING_TALLING, ImplementerType.NIZKP, EntityType.TALLIER);
			vr.setEntityName(tName);
			mockList.add(vr);

			VerificationResult vr2 = new VerificationResult(VerificationType.MT_T_NIZKP_OF_X_SIGN, true, ebp.getElectionID(), RunnerName.MIXING_TALLING, ImplementerType.RSA, EntityType.TALLIER);
			vr2.setEntityName(tName);
			mockList.add(vr2);
		}

		mockList.add(new VerificationResult(VerificationType.MT_VALID_PLAINTEXT_VOTES, true, ebp.getElectionID(), RunnerName.MIXING_TALLING, ImplementerType.PARAMETER, EntityType.PARAMETER));
		mockList.add(new VerificationResult(VerificationType.MT_VALID_PLAINTEXT_VOTES_SIGN, true, ebp.getElectionID(), RunnerName.MIXING_TALLING, ImplementerType.RSA, EntityType.EM));
	}

	/**
	 * Test if the list contains all the results
	 */
	@Test
	public void testListSize() {
		assertEquals(mockList.size(), realList.size());
	}

	/**
	 * Test if the election contains the correct election ID
	 */
	@Test
	public void testgetelectionID() {
		assertEquals(eID, v.geteID());
	}

	/**
	 * Test if the final result list is correct
	 */
	@Test
	public void testFinalResultList() {
		int i;

		for (i = 0; i < mockList.size(); i++) {
			assertEquals(realList.get(i).getVerificationType(), mockList.get(i).getVerificationType());
			assertEquals(realList.get(i).getResult(), mockList.get(i).getResult());
			assertTrue(realList.get(i).isImplemented());
		}
	}
}
