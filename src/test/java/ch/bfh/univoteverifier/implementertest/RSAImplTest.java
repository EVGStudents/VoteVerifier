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
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.naming.InvalidNameException;
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

	public RSAImplTest() throws FileNotFoundException, CertificateException, ElectionBoardServiceFault, InvalidNameException {
		ebp = new ElectionBoardProxy();
		ri = new RSAImplementer(ebp, RunnerName.UNSET);
	}

	/**
	 * Test the result of vrfEACertIDSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws CertificateException if the specified instance for the
	 * certificate factory used in this verification cannot be found.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testEACertId() throws ElectionBoardServiceFault, CertificateException, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfEACertIDSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfBasicParamSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testBasicParam() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfBasicParamSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfTMCertsSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testTMCert() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfTMCertsSign();
		assertTrue(vr.getResult());
		assertTrue(false);
	}

	/**
	 * Test the result of vrfElGamalParamSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testElGamal() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfElGamalParamSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfDistributedKeyBySign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testDistributedKeyBy() throws ElectionBoardServiceFault, UnsupportedEncodingException, NoSuchAlgorithmException {
		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = ri.vrfDistributedKeyBySign(tName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfDistributedKeySign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testDistributedKey() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfDistributedKeySign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElectionGeneratorBySign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testElectionGeneratorBy() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = ri.vrfElectionGeneratorBySign(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfElectionGeneratorSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testElectionGenerator() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfElectionGeneratorSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElectionOptionsSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testElectionOptions() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfElectionOptionsSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElectionDataSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testElectionData() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfElectionDataSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfElectoralRollSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testElectoralRoll() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfElectoralRollSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfVotersCertIDSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testVoterCertID() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfVotersCertIDSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfMixedVerificationKeysBySign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testMixedVerificationKeysBySign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = ri.vrfMixedVerificationKeysBySign(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfMixedVerificationKeysSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testMixedVerificationKeysSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfMixedVerificationKeysSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfLatelyRegisteredVotersCertificateSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testLatelyRegisteredVotersCertSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfLatelyRegisteredVotersCertificateSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfLatelyVerificationKeysBySign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testLatelyRegisteredVotersKeysBySign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = ri.vrfLatelyVerificationKeysBySign(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfLatelyVerificationKeysSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testLatelyRegisteredVotersKeysSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfLatelyVerificationKeysSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfShuffledEncryptedVotesBySign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testShuffledEncVotesBySign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {

			VerificationResult vr = ri.vrfMixedEncryptedVotesBySign(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfMixedEncryptedVotesSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testShuffledEncVotesSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfMixedEncryptedVotesSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfDecryptedVotesBySign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testDecryptedVotesBySign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		for (String tName : ebp.getElectionDefinition().getTallierId()) {

			VerificationResult vr = ri.vrfDecryptedVotesBySign(tName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the result of vrfPlaintextVotesSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testPlaintextVotesSign() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfPlaintextVotesSign();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the result of vrfSingleBallotSign().
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 * @throws NoSuchAlgorithmException if the hash algorithm function used
	 * in this verification cannot find the hash algorithm.
	 * @throws UnsupportedEncodingException if the hash algorithm function
	 * used in this verification cannot find the encoding.
	 */
	@Test
	public void testSingleBallot() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		File qrCodeFile = new File(this.getClass().getResource("/qrcodeGiu").getPath());
		QRCode qrCode = new QRCode(new Messenger());
		ElectionReceipt er = qrCode.decodeReceipt(qrCodeFile);

		VerificationResult vr = ri.vrfSingleBallotSign(er);
		assertTrue(vr.getResult());
	}
}
