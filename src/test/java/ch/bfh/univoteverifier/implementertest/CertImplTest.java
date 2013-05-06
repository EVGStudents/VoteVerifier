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

import ch.bfh.univoteverifier.common.CryptoFunc;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.x500.X500Principal;
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

	public CertImplTest() {
		ci = new CertificatesImplementer(null);

		fBfh = new File(this.getClass().getResource("/www.bfh.ch").getPath());

		fQuoVadisG = new File(this.getClass().getResource("/QuoVadisGlobalSSLICA").getPath());

		fQuoVadisRoot = new File(this.getClass().getResource("/QuoVadisRootCA2").getPath());
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

			//
			String princ = certList.get(0).getSubjectX500Principal().getName();

			LdapName ldapDN;
			try {
				ldapDN = new LdapName(princ);
				for (Rdn rdn : ldapDN.getRdns()) {
					System.out.println(rdn.getType() + " -> " + rdn.getValue());
				}
			} catch (InvalidNameException ex) {
				Logger.getLogger(CertImplTest.class.getName()).log(Level.SEVERE, null, ex);
			}

			//

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
}
