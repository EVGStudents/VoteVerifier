package primitives;

import ch.bfh.univoteverifier.implementer.ProofImplementer;
import ch.bfh.univoteverifier.utils.ProofDiscreteLog;
import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.*;
import org.junit.Test;


public class NIZKPTest {

	ProofImplementer prfVrf = new ProofImplementer();
	

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
