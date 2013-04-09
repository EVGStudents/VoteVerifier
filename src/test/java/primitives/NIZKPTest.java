package primitives;

import ch.bfh.univoteverifier.utils.ProofDiscreteLog;
import ch.bfh.univoteverifier.verification.PrimitivesVerifier;
import static org.junit.Assert.*;
import org.junit.Test;


public class NIZKPTest {

	PrimitivesVerifier prfVrf = new PrimitivesVerifier();
	

	/**
	 * Tests valid proof with small values
	 */
	@Test
	public void validProofSmallValues(){	
		ProofDiscreteLog prfDL = new ProofDiscreteLog();
		ProofDiscreteLog prf = prfDL.getProofSmall();
		assertTrue(prfVrf.vrfNIZKP(prf));
	}
	
	/**
	 * Tests valid proof with large values
	 */
	@Test
	public void validProofLargeValues(){	
		ProofDiscreteLog prfDL = new ProofDiscreteLog();
		ProofDiscreteLog prf = prfDL.getProofLarge();
		assertTrue(prfVrf.vrfNIZKP(prf));
	}
}