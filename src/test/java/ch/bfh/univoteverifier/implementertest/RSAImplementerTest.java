/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.implementertest;

import ch.bfh.univote.election.ElectionBoardServiceFault;
import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test the behavior of the CertificateImplementer.
 *
 * @author snake
 */
public class RSAImplementerTest {

	RSAImplementer ri;

	public RSAImplementerTest() throws FileNotFoundException, CertificateException, ElectionBoardServiceFault {
		ElectionBoardProxy ebp = new ElectionBoardProxy();
		ri = new RSAImplementer(ebp, RunnerName.UNSET);
	}

	/**
	 * Test the vrfBasicParamSign() method.
	 *
	 * @throws ElectionBoardServiceFault
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testVerifyBasicParam() throws ElectionBoardServiceFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		VerificationResult vr = ri.vrfBasicParamSign();
		assertTrue(vr.getResult());
	}
}
