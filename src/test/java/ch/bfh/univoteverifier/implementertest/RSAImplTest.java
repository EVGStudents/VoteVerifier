/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
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
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

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
	//@Test
    @Ignore
	public void testEACertId() {
		VerificationResult vr = ri.vrfEACertIDSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfBasicParamSign().
	 */
	//@Test
    @Ignore
	public void testBasicParam() {
		VerificationResult vr = ri.vrfBasicParamSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfTMCertsSign().
	 */
	//@Test
    @Ignore
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
	//@Test
    @Ignore
	public void testElectionOptions() {
		VerificationResult vr = ri.vrfElectionOptionsSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElectionDataSign().
	 */
	//@Test
    @Ignore
	public void testElectionData() {
		VerificationResult vr = ri.vrfElectionDataSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElectoralRollSign().
	 */
	//@Test
    @Ignore
	public void testElectoralRoll() {
		VerificationResult vr = ri.vrfElectoralRollSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfVotersCertIDSign().
	 */
	//@Test
    @Ignore
	public void testVoterCertID() {
		VerificationResult vr = ri.vrfVotersCertIDSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfMixedVerificationKeysBySign().
	 */
	//@Test
    @Ignore
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
	//@Test
    @Ignore
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
	//@Test
    @Ignore
	public void testLatelyRegisteredVotersKeysBySign() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = ri.vrfLatelyVerificationKeysBySign(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfLatelyVerificationKeysSign().
	 */
	//@Test
    @Ignore
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
	//@Test
    @Ignore
	public void testMixedEncVotesBySign() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {

			VerificationResult vr = ri.vrfMixedEncryptedVotesBySign(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfMixedEncryptedVotesSign().
	 */
	//@Test
    @Ignore
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
	//@Test
    @Ignore
	public void testPlaintextVotesSign() {
		VerificationResult vr = ri.vrfPlaintextVotesSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfSingleBallotSign().
	 */
	//@Test
    @Ignore
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
	//@Test
    @Ignore
	public void testLateRenewalOfRegSignBy() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = ri.vrfLateRenewalOfRegistrationSignBy(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfLateRenewalOfRegistrationSign().
	 */
	//@Test
    @Ignore
	public void testLateRenewalOfRegSign() {
		VerificationResult vr = ri.vrfLateRenewalOfRegistrationSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfBallotSetSign().
	 */
	//@Test
    @Ignore
	public void testBallotSetSign() {
		VerificationResult vr = ri.vrfBallotsSetSign();
		assertTrue(vr.getResult());
	}
}
