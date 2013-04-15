package primitives;

import ch.bfh.univoteverifier.utils.ProofDiscreteLog;
import ch.bfh.univoteverifier.verification.PrimitivesVerifier;
import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.*;
import org.junit.Test;


public class NIZKPTest {

	PrimitivesVerifier prfVrf = new PrimitivesVerifier();
	

	/**
	 * Tests valid proof with small values
	 */
	@Test
	public void validProofSmallValues() throws NoSuchAlgorithmException{	
		ProofDiscreteLog prfDL = new ProofDiscreteLog();
		ProofDiscreteLog prf = prfDL.getProofSmall();
		assertTrue(prfVrf.vrfNIZKP(prf));
	}
	
	/**
	 * Tests valid proof with large values
	 */
	@Test
	public void validProofLargeValues() throws NoSuchAlgorithmException{	
		ProofDiscreteLog prfDL = new ProofDiscreteLog();
		ProofDiscreteLog prf = prfDL.getProofLarge();
		assertTrue(prfVrf.vrfNIZKP(prf));
	}
}
