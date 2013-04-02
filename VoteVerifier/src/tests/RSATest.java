package tests;


import static org.junit.Assert.*;
import java.math.BigInteger;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import utils.RSASignature;
import crypto_vrf.RSAVrf;


public class RSATest {



	/**
	 * Tests valid signature with large values and large random message
	 */
	@Test
	public void validSigLargValsRandomMsg(){
		RSAVrf rv = new RSAVrf();
		RSASignature s = new RSASignature();
		BigInteger m = new BigInteger("123456789012345678901234567890123456789012345678901234567890");
		s.setVarsLarge(m);
		s.sign(m);
		assertTrue(rv.verifySignature(s, m));
	}
	
	
	/**
	 * Tests valid signature with small values and static message
	 */
	@Test
	public void validSigSmallStaticVals(){
		RSAVrf rv = new RSAVrf();
		RSASignature s = new RSASignature();
		BigInteger m = new BigInteger("4");
		s.setStaticVars();
		s.sign(m);
		assertTrue(rv.verifySignature(s, m));
	}


	/**
	 * Tests valid signature with small values and random message 
	 * and random key values
	 */
	@Test
	public void validSigSmallValues(){
		Random rand = new Random();
		int r = rand.nextInt(33);
		int p = 3;
		int q = 11;
		RSAVrf rv = new RSAVrf();
		RSASignature s = new RSASignature();
		BigInteger m = BigInteger.valueOf(r);
		s.setVarsSmall(m,p,q);
		s.sign(m);
		assertTrue(rv.verifySignature(s, m));
	}

}