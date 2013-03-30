package tests;

import static org.junit.Assert.*;

import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;

import utils.ProofDiscreteLog;

import common.Config;

import crypto_vrf.NIZKP;
import crypto_vrf.PrimitivesVrf;

public class NIZKPTest {

	NIZKP prfVrf = new NIZKP();
	@Before
	public void setup(){
//		System.out.println("Testing Non-interactive zero knowledge proof verifier");
	}
	

	/**
	 * Tests valid proof with small values
	 */
	@Test
	public void validProofSmallValues(){	
		ProofDiscreteLog prfDL = new ProofDiscreteLog();
		ProofDiscreteLog prf = prfDL.getProofSmall();
//		smPrfDL.toString();
		assertTrue(prfVrf.verifyProofDiscreteLog(prf));
	}
	
	/**
	 * Tests valid proof with small values
	 */
	@Test
	public void validProofLargeValues(){	
		ProofDiscreteLog prfDL = new ProofDiscreteLog();
		ProofDiscreteLog prf = prfDL.getProofLarge();
//		smPrfDL.toString();
		assertTrue(prfVrf.verifyProofDiscreteLog(prf));
	}
}
