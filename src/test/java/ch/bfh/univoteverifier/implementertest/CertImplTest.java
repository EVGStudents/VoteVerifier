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

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.File;
import java.io.FileNotFoundException;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

/* import java.nio.file.Files; */

/**
 * This class test the behavior of the CertificateImplementer.
 *
 * @author Scalzi Giuseppe
 */
public class CertImplTest {

	private CertificatesImplementer ci;
	private File fBfh;
	private File fQuoVadisG;
	private File fQuoVadisRoot;
	private ElectionBoardProxy ebp;

	public CertImplTest() throws FileNotFoundException {
		ebp = new ElectionBoardProxy("risis-2013-1", true);
		ci = new CertificatesImplementer(ebp, RunnerName.UNSET);

		fBfh = new File(this.getClass().getResource("/www.bfh.ch").getPath());
		fQuoVadisG = new File(this.getClass().getResource("/QuoVadisGlobalSSLICA").getPath());
		fQuoVadisRoot = new File(this.getClass().getResource("/QuoVadisRootCA2").getPath());
	}

	/**
	 * The the certificate chain of https://www.bfh.ch.
	 */
//	@Test
//	public void testCertificateChain() {
//		List<X509Certificate> certList = new ArrayList<X509Certificate>();
//
//		try {
//			byte[] bBfh = Files.readAllBytes(fBfh.toPath());
//			byte[] bQuoVadisG = Files.readAllBytes(fQuoVadisG.toPath());
//			byte[] bQuoVadisRoot = Files.readAllBytes(fQuoVadisRoot.toPath());
//
//			certList.add(CryptoFunc.getX509Certificate(bBfh));
//			certList.add(CryptoFunc.getX509Certificate(bQuoVadisG));
//			certList.add(CryptoFunc.getX509Certificate(bQuoVadisRoot));
//
//			assertTrue(ci.vrfCert(certList));
//		} catch (Exception ex) {
//            fail("Unexpected exception: " + ex.getMessage());
//		}
//	}

	/**
	 * Test the certificate chain without the Root CA. This must be throw an
	 * exception since we cannot verify the intermediate certificate.
	 */
//	@Test(expected = CertPathValidatorException.class)
//    public void testUnvalidCertificateChain() throws Exception {
//        List<X509Certificate> certList = new ArrayList<X509Certificate>();
//
//        byte[] bBfh = Files.readAllBytes(fBfh.toPath());
//        byte[] bQuoVadisG = Files.readAllBytes(fQuoVadisG.toPath());
//
//        certList.add(CryptoFunc.getX509Certificate(bBfh));
//        certList.add(CryptoFunc.getX509Certificate(bQuoVadisG));
//
//        assertFalse(ci.vrfCert(certList));
//    }

	/**
	 * Test the CA certificate.
	 */
	@Test
	public void testCACertificate() {
		VerificationResult vr = ci.vrfCACertificate();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the EA Certificate.
	 */
	@Test
	public void testEACertificate() {
		VerificationResult vr = ci.vrfEACertificate();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the EM Certificate.
	 */
	@Test
	public void testEMCertificate() {
		VerificationResult vr = ci.vrfEMCertificate();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the Mixers certificates.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testMixersCertificate() throws ElectionBoardServiceFault {
		for (String mName : ebp.getElectionDefinition().getMixerId()) {
			VerificationResult vr = ci.vrfMixerCertificate(mName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the Talliers certificates.
	 *
	 * @throws ElectionBoardServiceFault if there is problem with the public
	 * board, such as a wrong parameter or a network connection problem.
	 */
	@Test
	public void testTalliersCertificate() throws ElectionBoardServiceFault {
		for (String tName : ebp.getElectionDefinition().getTallierId()) {
			VerificationResult vr = ci.vrfTallierCertificate(tName);
			assertTrue(vr.getResult());
		}
	}

	/**
	 * Test the Voters certificates.
	 */
	//@Test
    @Ignore
	public void testVotersCert() {
		VerificationResult vr = ci.vrfVotersCertificate();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the Lately registered voters certificates.
	 */
	//@Test
    @Ignore
	public void testLatelyVotersCerts() {
		VerificationResult vr = ci.vrfLatelyRegisteredVotersCertificate();
		assertTrue(vr.getResult());
	}
}
