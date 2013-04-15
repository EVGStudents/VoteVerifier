package primitives;

import ch.bfh.univoteverifier.verification.PrimitivesVerifier;
import ch.bfh.univoteverifier.utils.SchnorrGenerator;
import ch.bfh.univoteverifier.utils.SchnorrSignature;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;


import org.junit.Test;


public class SchnorrTest {

	SchnorrGenerator sg = new SchnorrGenerator();
	PrimitivesVerifier sv = new PrimitivesVerifier();
	
	@Test
	public void singleSignatureVerification() throws NoSuchAlgorithmException {
		//BFH encoded as 66 | 70 | 72
		String bfh = "667072";
		BigInteger message = new BigInteger(bfh);
		
		//generate the signature
		SchnorrSignature ss = sg.signatureGeneration(message);
		
		//Verify signature		
		assertTrue(sv.vrfSchnorrSign(ss, message, SchnorrGenerator.publicKey));
	}
	
	@Test
	public void multipleSignatureVerification() throws NoSuchAlgorithmException{
		int numberOfVerifications = 1000;
		
		for(int i = 0; i < numberOfVerifications ; i++){
			String bfh = "667072";
			BigInteger message = new BigInteger(bfh);
			
			//generate the signature
			SchnorrSignature ss = sg.signatureGeneration(message);
						
			assertTrue(sv.vrfSchnorrSign(ss, message, SchnorrGenerator.publicKey));
		}
		
	}
	
	@Test
	public void incorrectSignature() throws NoSuchAlgorithmException{
		//BFH encoded as 66 | 70 | 72
		String bfh = "667072";
		BigInteger message = new BigInteger(bfh);
		
		BigInteger falseMessage = new BigInteger("12345");
		
		//generate the signature
		SchnorrSignature ss = sg.signatureGeneration(message);
		
		//Verify signature		
		assertFalse(sv.vrfSchnorrSign(ss, falseMessage, SchnorrGenerator.publicKey));
	}

}
