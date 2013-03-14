package tests;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.BeforeClass;
import org.junit.Test;

import crypto_vrf.SchnorrSignature;
import crypto_vrf.SchnorrVrf;

import utils.SchnorrGenerator;

public class SchnorrTest {

	SchnorrGenerator sg = new SchnorrGenerator();
	SchnorrVrf sv;
	
	
	@Test
	public void singleSignatureVerification() {
		//BFH encoded as 66 | 70 | 72
		String bfh = "667072";
		BigInteger message = new BigInteger(bfh);
		
		//generate the signature
		SchnorrSignature ss = sg.signatureGeneration(message);
		
		//Verify signature
		sv = new SchnorrVrf(ss, message);
		
		assertTrue(sv.isSchnorrVerified());
	}

	
	@Test
	public void multipleSignatureVerification(){
		int numberOfVerifications = 100;
		
		
		for(int i = 0; i < numberOfVerifications ; i++){
			String bfh = "667072";
			BigInteger message = new BigInteger(bfh);
			
			//generate the signature
			SchnorrSignature ss = sg.signatureGeneration(message);
			
			//Verify signature
			sv = new SchnorrVrf(ss, message);
			
			assertTrue(sv.isSchnorrVerified());
		}
		
	}
	

}
