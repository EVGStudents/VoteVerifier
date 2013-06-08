/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.implementertest;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of the RSAImplementer.
 *
 * @author Scalzi Giuseppe
 */
public class RSAImplTest {

	RSAImplementer ri;
	ElectionBoardProxy ebp;

	public RSAImplTest() throws FileNotFoundException {
		ebp = new ElectionBoardProxy("risis-2013-1", true);
		ri = new RSAImplementer(ebp, RunnerName.UNSET);
	}

	/**
	 * Test the result of vrfEACertIDSign().
	 */
	@Test
	public void testEACertId() {
		VerificationResult vr = ri.vrfEACertIDSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfBasicParamSign().
	 */
	@Test
	public void testBasicParam() {
		VerificationResult vr = ri.vrfBasicParamSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfTMCertsSign().
	 */
	@Test
	public void testTMCert() {
		VerificationResult vr = ri.vrfTMCertsSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElGamalParamSign().
	 */
	@Test
	public void testElGamal() {
		VerificationResult vr = ri.vrfElGamalParamSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfDistributedKeyBySign().
	 */
	@Test
	public void testDistributedKeyBy() throws ElectionBoardServiceFault {
		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = ri.vrfDistributedKeyBySign(tName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfDistributedKeySign().
	 */
	@Test
	public void testDistributedKey() {
		VerificationResult vr = ri.vrfDistributedKeySign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElectionGeneratorBySign().
	 */
	@Test
	public void testElectionGeneratorBy() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = ri.vrfElectionGeneratorBySign(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfElectionGeneratorSign().
	 */
	@Test
	public void testElectionGenerator() {
		VerificationResult vr = ri.vrfElectionGeneratorSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElectionOptionsSign().
	 */
	@Test
	public void testElectionOptions() {
		VerificationResult vr = ri.vrfElectionOptionsSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElectionDataSign().
	 */
	@Test
	public void testElectionData() {
		VerificationResult vr = ri.vrfElectionDataSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElectoralRollSign().
	 */
	@Test
	public void testElectoralRoll() {
		VerificationResult vr = ri.vrfElectoralRollSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfVotersCertIDSign().
	 */
	@Test
	public void testVoterCertID() {
		VerificationResult vr = ri.vrfVotersCertIDSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfMixedVerificationKeysBySign().
	 */
	@Test
	public void testMixedVerificationKeysBySign() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = ri.vrfMixedVerificationKeysBySign(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfMixedVerificationKeysSign().
	 */
	@Test
	public void testMixedVerificationKeysSign() {
		VerificationResult vr = ri.vrfMixedVerificationKeysSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfLatelyRegisteredVotersCertificateSign().
	 */
	@Test
	public void testLatelyRegisteredVotersCertSign() {
		VerificationResult vr = ri.vrfLatelyRegisteredVotersCertificateSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfLatelyVerificationKeysBySign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testLatelyRegisteredVotersKeysBySign() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = ri.vrfLatelyVerificationKeysBySign(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfLatelyVerificationKeysSign().
	 */
	@Test
	public void testLatelyRegisteredVotersKeysSign() {
		VerificationResult vr = ri.vrfLatelyVerificationKeysSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfMixedEncryptedVotesBySign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testMixedEncVotesBySign() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {

			VerificationResult vr = ri.vrfMixedEncryptedVotesBySign(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfMixedEncryptedVotesSign().
	 */
	@Test
	public void testShuffledEncVotesSign() {
		VerificationResult vr = ri.vrfMixedEncryptedVotesSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfDecryptedVotesBySign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testDecryptedVotesBySign() throws ElectionBoardServiceFault {
		for (String tName : ebp.getElectionDefinition().getTallierId()) {

			VerificationResult vr = ri.vrfDecryptedVotesBySign(tName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfPlaintextVotesSign().
	 */
	@Test
	public void testPlaintextVotesSign() {
		VerificationResult vr = ri.vrfPlaintextVotesSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfSingleBallotSign().
	 */
	@Test
	public void testSingleBallot() {
		File qrCodeFile = new File(this.getClass().getResource("/qrcodeGiu").getPath());
		QRCode qrCode = new QRCode(new Messenger());
		ElectionReceipt er = qrCode.decodeReceipt(qrCodeFile);

		VerificationResult vr = ri.vrfSingleBallotSign(er);
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfLateRenewalOfRegistrationSignBy().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testLateRenewalOfRegSignBy() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = ri.vrfLateRenewalOfRegistrationSignBy(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfLateRenewalOfRegistrationSign().
	 */
	@Test
	public void testLateRenewalOfRegSign() {
		VerificationResult vr = ri.vrfLateRenewalOfRegistrationSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfBallotSetSign().
	 */
	@Test
	public void testBallotSetSign() {
		VerificationResult vr = ri.vrfBallotsSetSign();
		assertTrue(vr.getResult());
	}
}
