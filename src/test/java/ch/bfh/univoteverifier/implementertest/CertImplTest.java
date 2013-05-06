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
import java.io.File;
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
 * This class test the behavior of the CertificateImplementer
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

	public CertImplTest() {
		ebp = new ElectionBoardProxy("vsbfh-2013");
		ci = new CertificatesImplementer(ebp, RunnerName.UNSET);

		fBfh = new File(this.getClass().getResource("/www.bfh.ch").getPath());

		fQuoVadisG = new File(this.getClass().getResource("/QuoVadisGlobalSSLICA").getPath());

		fQuoVadisRoot = new File(this.getClass().getResource("/QuoVadisRootCA2").getPath());

		cacert = new File(this.getClass().getResource("/CACertificate.pem").getPath());
	}

	/**
	 * The the certificate chain of https://www.bfh.ch
	 */
	@Test
	public void testCertificateChain() {
		List<X509Certificate> certList = new ArrayList<>();

		try {
			byte[] bBfh = Files.readAllBytes(fBfh.toPath());
			byte[] bQuoVadisG = Files.readAllBytes(fQuoVadisG.toPath());
			byte[] bQuoVadisRoot = Files.readAllBytes(fQuoVadisRoot.toPath());

			certList.add(CryptoFunc.getX509Certificate(bBfh));
			certList.add(CryptoFunc.getX509Certificate(bQuoVadisG));
			certList.add(CryptoFunc.getX509Certificate(bQuoVadisRoot));

			assertTrue(ci.vrfCert(certList));
		} catch (IOException | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | CertPathValidatorException ex) {
		}

	}

	/**
	 * Test the certificate chain without the Root CA. This must be throw an
	 * exception since we cannot verify the intermediate certificate
	 *
	 * @throws CertPathValidatorException
	 */
	@Test(expected = CertPathValidatorException.class)
	public void testUnvalidCertificateChain() throws CertPathValidatorException {
		List<X509Certificate> certList = new ArrayList<>();

		try {
			byte[] bBfh = Files.readAllBytes(fBfh.toPath());
			byte[] bQuoVadisG = Files.readAllBytes(fQuoVadisG.toPath());

			certList.add(CryptoFunc.getX509Certificate(bBfh));
			certList.add(CryptoFunc.getX509Certificate(bQuoVadisG));

			assertTrue(ci.vrfCert(certList));
		} catch (IOException | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException ex) {
		}
	}

	@Test
	public void testCaCertificate() throws ElectionBoardServiceFault, CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
		//VerificationResult vr = ci.vrfCACertificate();
		//assertTrue(vr.getResult());

		byte[] bb = ebp.getElectionSystemInfo().getElectionAdministration().getValue();
		System.out.println(new String(bb));
		String s = "-----BEGIN CERTIFICATE-----\n"
			+ "MIIDIDCCAomgAwIBAgIENd70zzANBgkqhkiG9w0BAQUFADBOMQswCQYDVQQGEwJV\n"
			+ "UzEQMA4GA1UEChMHRXF1aWZheDEtMCsGA1UECxMkRXF1aWZheCBTZWN1cmUgQ2Vy\n"
			+ "dGlmaWNhdGUgQXV0aG9yaXR5MB4XDTk4MDgyMjE2NDE1MVoXDTE4MDgyMjE2NDE1\n"
			+ "MVowTjELMAkGA1UEBhMCVVMxEDAOBgNVBAoTB0VxdWlmYXgxLTArBgNVBAsTJEVx\n"
			+ "dWlmYXggU2VjdXJlIENlcnRpZmljYXRlIEF1dGhvcml0eTCBnzANBgkqhkiG9w0B\n"
			+ "AQEFAAOBjQAwgYkCgYEAwV2xWGcIYu6gmi0fCG2RFGiYCh7+2gRvE4RiIcPRfM6f\n"
			+ "BeC4AfBONOziipUEZKzxa1NfBbPLZ4C/QgKO/t0BCezhABRP/PvwDN1Dulsr4R+A\n"
			+ "cJkVV5MW8Q+XarfCaCMczE1ZMKxRHjuvK9buY0V7xdlfUNLjUA86iOe/FP3gx7kC\n"
			+ "AwEAAaOCAQkwggEFMHAGA1UdHwRpMGcwZaBjoGGkXzBdMQswCQYDVQQGEwJVUzEQ\n"
			+ "MA4GA1UEChMHRXF1aWZheDEtMCsGA1UECxMkRXF1aWZheCBTZWN1cmUgQ2VydGlm\n"
			+ "aWNhdGUgQXV0aG9yaXR5MQ0wCwYDVQQDEwRDUkwxMBoGA1UdEAQTMBGBDzIwMTgw\n"
			+ "ODIyMTY0MTUxWjALBgNVHQ8EBAMCAQYwHwYDVR0jBBgwFoAUSOZo+SvSspXXR9gj\n"
			+ "IBBPM5iQn9QwHQYDVR0OBBYEFEjmaPkr0rKV10fYIyAQTzOYkJ/UMAwGA1UdEwQF\n"
			+ "MAMBAf8wGgYJKoZIhvZ9B0EABA0wCxsFVjMuMGMDAgbAMA0GCSqGSIb3DQEBBQUA\n"
			+ "A4GBAFjOKer89961zgK5F7WF0bnj4JXMJTENAKaSbn+2kmOeUJXRmm/kEd5jhW6Y\n"
			+ "7qj/WsjTVbJmcVfewCHrPSqnI0kBBIZCe/zuf6IWUrVnZ9NA2zsmWLIodz2uFHdh\n"
			+ "1voqZiegDfqnc1zqcPGUIWVEX/r87yloqaKHee9570+sB3c4\n"
			+ "-----END CERTIFICATE-----";
		X509Certificate x = CryptoFunc.getX509Certificate(s.getBytes());

//		byte[] b2 = ebp.getElectionSystemInfo().getElectionAdministration().getValue();
//		System.out.println(new String(b2));
//		X509Certificate c2 = CryptoFunc.getX509Certificate(b2);
//		System.out.println(c2);
//
//		byte[] b3 = ebp.getElectionSystemInfo().getCertificateAuthority().getValue();
//		X509Certificate c3 = CryptoFunc.getX509Certificate(b3);
//		System.out.println(c3);

	}
}
