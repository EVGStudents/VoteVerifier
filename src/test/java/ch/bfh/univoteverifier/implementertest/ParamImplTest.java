/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.implementertest;

import ch.bfh.univote.common.Ballot;
import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.implementer.ParametersImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class test the behavior of the ParametersImplementer.
 *
 * @author Scalzi Giuseppe
 */
public class ParamImplTest {

	private final ParametersImplementer pi, piQR;
	private final BigInteger p, q, g;
	private final BigInteger elQ, elG, elP;
	private final ElectionBoardProxy ebp, ebpQR;

	public ParamImplTest() throws FileNotFoundException, ElectionBoardServiceFault {
		ebp = new ElectionBoardProxy("risis-2013-1", true);
		pi = new ParametersImplementer(ebp, RunnerName.UNSET);

		//create a board proxy for vsbfh, we only have the qr-code for vsbfh
		ebpQR = new ElectionBoardProxy("vsbfh-2013", true);
		piQR = new ParametersImplementer(ebpQR, RunnerName.UNSET);

		//change the value of p,q and g - all the test must fail
		p = Config.p.multiply(new BigInteger("2"));
		q = Config.q.multiply(new BigInteger("2"));
		g = Config.g.multiply(new BigInteger("2"));

		elQ = ebp.getEncryptionParameters().getGroupOrder();
		elG = ebp.getEncryptionParameters().getGenerator();
		elP = ebp.getEncryptionParameters().getPrime();
	}

	/**
	 * Test that the parameters are not long as expected.
	 */
	@Test
	public void testParamNotLen() {
		VerificationResult v = pi.vrfSchnorrParamLen(p, q, g);
		assertFalse(v.getResult());
		assertEquals(v.getReport().getFailureCode(), FailureCode.FALSE_PARAMETERS_LENGTH);
	}

	/**
	 * Test that P is not a safe prime.
	 */
	@Test
	public void testPisNotSafePrime() {
		VerificationResult v = pi.vrfSafePrime(p, q, VerificationType.SETUP_SCHNORR_P_SAFE_PRIME);
		assertFalse(v.getResult());
		assertEquals(v.getReport().getFailureCode(), FailureCode.NOT_SAFE_PRIME);

	}

	/**
	 * Test that P is not prime.
	 */
	@Test
	public void testPisNotPrime() {
		VerificationResult v = pi.vrfPrime(p, VerificationType.SETUP_SCHNORR_P);
		assertFalse(v.getResult());
		assertEquals(v.getReport().getFailureCode(), FailureCode.COMPOSITE_PRIME_NUMBER);
	}

	/**
	 * Test that Q is not prime.
	 */
	@Test
	public void testQisNotPrime() {
		VerificationResult v = pi.vrfPrime(q, VerificationType.SETUP_SCHNORR_Q);
		assertFalse(v.getResult());
		assertEquals(v.getReport().getFailureCode(), FailureCode.COMPOSITE_PRIME_NUMBER);
	}

	/**
	 * Test that G is not a generator.
	 */
	@Test
	public void testGisNotGenerator() {
		VerificationResult v = pi.vrfGenerator(p, q, g, VerificationType.SETUP_SCHNORR_G);
		assertFalse(v.getResult());
		assertEquals(v.getReport().getFailureCode(), FailureCode.NOT_A_GENERATOR);
	}

	/**
	 * Test that the distributed key y correspond to the partial y_j of the
	 * last tallier.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testDistributedKey() {
		VerificationResult v = pi.vrfDistributedKey();
		assertTrue(v.getResult());
	}

	/**
	 * Test that the election generator g^ is equal to the blinded generator
	 * of the last mixer.
	 */
	@Test
	public void testElectionGenerator() {
		VerificationResult v = pi.vrfElectionGenerator();
		assertTrue(v.getResult());
	}

	/**
	 * Test the mixed verification keys.
	 */
	@Test
	public void testMixedVerificationKeys() {
		VerificationResult v = pi.vrfVerificationKeysMixed();
		assertTrue(v.getResult());
	}

	/**
	 * Test the lately mixed verification keys.
	 */
	@Test
	public void testLatelyMixerVerificationKeys() {
		VerificationResult v = pi.vrfLatelyVerificatonKeys();
		assertTrue(v.getResult());
	}

	/**
	 * Test the verification key of a ballot from a QR-Code.
	 */
	@Test
	public void testBallotVerificationKeyFromQR() throws UnsupportedEncodingException {
		File qrCodeFile = new File(this.getClass().getResource("/qrcodeGiu").getPath());
		QRCode qrCode = new QRCode(new Messenger());
		ElectionReceipt er = qrCode.decodeReceipt(qrCodeFile);
		VerificationResult v = piQR.vrfBallotVerificationKey(er.getVerificationKey());
		assertTrue(v.getResult());
	}

	/**
	 * Test the verification key of a ballot from a ballot.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	//@Test
    @Ignore
	public void testBallotVerificationKeyFromBallot() throws ElectionBoardServiceFault {
		Ballot b = ebp.getBallots().getBallot().get(0);
		VerificationResult v = pi.vrfBallotVerificationKey(b.getVerificationKey());
		assertTrue(v.getResult());
	}

	/**
	 * Test the votes.
	 */
	//@Test
    @Ignore
	public void testVotes() {
		VerificationResult v = pi.vrfVotes();
		assertTrue(v.getResult());
	}

	/**
	 * Test that the ElGamal P is prime.
	 */
	@Test
	public void testElGamalP() {
		VerificationResult v = pi.vrfPrime(elP, VerificationType.EL_SETUP_ELGAMAL_P);
		assertTrue(v.getResult());
	}

	/**
	 * Test that the ElGamal Q is prime.
	 */
	@Test
	public void testElGamalQ() {
		VerificationResult v = pi.vrfPrime(elQ, VerificationType.EL_SETUP_ELGAMAL_Q);
		assertTrue(v.getResult());
	}

	/**
	 * Test that the ElGamal G is a generator.
	 */
	@Test
	public void testElGamalG() {
		VerificationResult v = pi.vrfGenerator(elP, elQ, elG, VerificationType.EL_SETUP_ELGAMAL_G);
		assertTrue(v.getResult());
	}

	/**
	 * Test the length of ElGamal parameters length.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testElGamalParamLen() throws ElectionBoardServiceFault {
		VerificationResult v = pi.vrfElGamalParamLen(elP, ebp.getElectionDefinition().getKeyLength());
		assertTrue(v.getResult());
	}

	/**
	 * Test that the ElGamal P is safe prime.
	 */
	@Test
	public void testElGamalSafePrime() {
		VerificationResult v = pi.vrfSafePrime(elP, elQ, VerificationType.EL_SETUP_ELGAMAL_SAFE_PRIME);
		assertTrue(v.getResult());
	}

	/**
	 * Test the result of vrfMixedEncryptedVotes().
	 */
	//@Test
    @Ignore
	public void testMixedExncryptedVotes() {
		VerificationResult v = pi.vrfMixedEncryptedVotes();
		assertTrue(v.getResult());
	}

	/**
	 * Test the result of vrfBallotInSet() by using a vk from a ballot.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testBallotInSetFromBallot() throws ElectionBoardServiceFault {
		Ballot b = ebp.getBallots().getBallot().get(0);
		VerificationResult v = pi.vrfBallotInSet(b.getVerificationKey());
		assertTrue(v.getResult());
	}

	/**
	 * Test the result of vrfBallotInSet() by using a vk from a QR-Code.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testBallotInSetFromQRCode() throws ElectionBoardServiceFault {
		File qrCodeFile = new File(this.getClass().getResource("/qrcodeGiu").getPath());
		QRCode qrCode = new QRCode(new Messenger());
		ElectionReceipt er = qrCode.decodeReceipt(qrCodeFile);
		VerificationResult v = piQR.vrfBallotInSet(er.getVerificationKey());
		assertTrue(v.getResult());
	}
}
