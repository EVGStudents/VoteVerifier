/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.implementertest;

import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.implementer.CertificatesImplementer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the certificates verificator
 * @author snake
 */
public class CertificateValidationTest {
	
	CertificatesImplementer ci;
	CertificateFactory cf;
	X509Certificate c1,c2,c3;


	/**
	 * Construct a certificate validation test
	 * @throws FileNotFoundException
	 * @throws CertificateException 
	 */
	public CertificateValidationTest() throws FileNotFoundException, CertificateException, IOException {
		List mylist = new ArrayList<>();
		ci = new CertificatesImplementer();
		cf = CertificateFactory.getInstance("X.509");
		
		File f = new File(CertificateValidationTest.class.getResource("/accounts.google.com").getPath());
		File f2 = new File(CertificateValidationTest.class.getResource("/GoogleInternetAuthority").getPath());
		File f3 = new File(CertificateValidationTest.class.getResource("/EquifaxSecureCA").getPath());
		
		BufferedInputStream b1 = new BufferedInputStream(new FileInputStream(f));
		BufferedInputStream b2 = new BufferedInputStream(new FileInputStream(f2));
		BufferedInputStream b3 = new BufferedInputStream(new FileInputStream(f3));

		c1 = CryptoFunc.getX509Certificate(Files.readAllBytes(f.toPath()));
		c2 = CryptoFunc.getX509Certificate(Files.readAllBytes(f2.toPath()));
		c3 = CryptoFunc.getX509Certificate(Files.readAllBytes(f3.toPath()));
	}

	/**
	 * Test if the certificate path is valid
	 * @throws CertificateException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchAlgorithmException
	 * @throws CertPathValidatorException 
	 */
	@Test
	public void validateCertificate() throws CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CertPathValidatorException {
		List mylist = new ArrayList<>();
		mylist.add(c1);
		mylist.add(c2);
		mylist.add(c3);
		
		assertTrue(ci.vrfCert(mylist));
	}

	/**
	 * Test if the certificate path is valid, without passing the CA
	 * @throws CertificateException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchAlgorithmException
	 * @throws CertPathValidatorException 
	 */
	@Test(expected=CertPathValidatorException.class)
	public void invalideCertificatePath() throws CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CertPathValidatorException{
		
		List mylist = new ArrayList<>();
		
		mylist.add(c1);
		mylist.add(c2);

		ci.vrfCert(mylist);
	}
}