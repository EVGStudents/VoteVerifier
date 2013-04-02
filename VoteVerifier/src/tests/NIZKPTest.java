package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import utils.ProofDiscreteLog;
import crypto_vrf.NIZKP;


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
	 * Tests valid proof with large values
	 */
	@Test
	public void validProofLargeValues(){	
		ProofDiscreteLog prfDL = new ProofDiscreteLog();
		ProofDiscreteLog prf = prfDL.getProofLarge();
//		smPrfDL.toString();
		assertTrue(prfVrf.verifyProofDiscreteLog(prf));
	}
}
