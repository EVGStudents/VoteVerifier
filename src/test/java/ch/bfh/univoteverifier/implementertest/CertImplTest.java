/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.implementertest;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of the CertificateImplementer.
 *
 * @author snake
 */
public class CertImplTest {

	CertificatesImplementer ci;
	File fBfh;
	File fQuoVadisG;
	File fQuoVadisRoot;
	ElectionBoardProxy ebp;
	File cacert;

	public CertImplTest() throws FileNotFoundException {
		ebp = new ElectionBoardProxy();
		ci = new CertificatesImplementer(ebp, RunnerName.UNSET);

		fBfh = new File(this.getClass().getResource("/www.bfh.ch").getPath());

		fQuoVadisG = new File(this.getClass().getResource("/QuoVadisGlobalSSLICA").getPath());

		fQuoVadisRoot = new File(this.getClass().getResource("/QuoVadisRootCA2").getPath());

		cacert = new File(this.getClass().getResource("/CACertificate.pem").getPath());
	}

	/**
	 * The the certificate chain of https://www.bfh.ch.
	 */
	@Test
	public void testCertificateChain() {
		List<X509Certificate> certList = new ArrayList<>();

		try {
			byte[] bBfh = Files.readAllBytes(fBfh.toPath());
			byte[] bQuoVadisG = Files.readAllBytes(fQuoVadisG.toPath());
			byte[] bQuoVadisRoot = Files.readAllBytes(fQuoVadisRoot.toPath());

			certList.add(CryptoFunc.getX509Certificate(bBfh, false));
			certList.add(CryptoFunc.getX509Certificate(bQuoVadisG, false));
			certList.add(CryptoFunc.getX509Certificate(bQuoVadisRoot, false));

			assertTrue(ci.vrfCert(certList));
		} catch (IOException | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | CertPathValidatorException ex) {
		}

	}

	/**
	 * Test the certificate chain without the Root CA. This must be throw an
	 * exception since we cannot verify the intermediate certificate.
	 *
	 * @throws CertPathValidatorException
	 */
	@Test(expected = CertPathValidatorException.class)
	public void testUnvalidCertificateChain() throws CertPathValidatorException {
		List<X509Certificate> certList = new ArrayList<>();

		try {
			byte[] bBfh = Files.readAllBytes(fBfh.toPath());
			byte[] bQuoVadisG = Files.readAllBytes(fQuoVadisG.toPath());

			certList.add(CryptoFunc.getX509Certificate(bBfh, false));
			certList.add(CryptoFunc.getX509Certificate(bQuoVadisG, false));

			assertTrue(ci.vrfCert(certList));
		} catch (IOException | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException ex) {
		}
	}

	/**
	 * Test the CA certificate.
	 *
	 * @throws ElectionBoardServiceFault
	 * @throws CertificateException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@Test
	public void testCaCertificate() throws ElectionBoardServiceFault, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
		VerificationResult vr = ci.vrfCACertificate();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the EA Certificate.
	 *
	 * @throws ElectionBoardServiceFault
	 * @throws CertificateException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void testEACertificate() throws ElectionBoardServiceFault, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
		VerificationResult vr = ci.vrfEACertificate();
		assertTrue(vr.getResult());
	}

	/**
	 * Test the EM Certificate.
	 *
	 * @throws ElectionBoardServiceFault
	 * @throws CertificateException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void testEMCertificate() throws ElectionBoardServiceFault, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
		VerificationResult vr = ci.vrfEMCertificate();
		assertTrue(vr.getResult());
	}
}
