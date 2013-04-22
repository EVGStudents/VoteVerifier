package primitives;


import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.implementer.RSAImplementer;
import ch.bfh.univoteverifier.utils.RSASignature;
import static org.junit.Assert.*;
import java.math.BigInteger;
import java.util.Random;
import org.junit.Test;


public class RSATest {

	RSAImplementer prfVrf;
	
	
	public RSATest(){
		prfVrf = new RSAImplementer(new ElectionBoardProxy("sub-2013"));
	}

	/**
	 * Tests valid signature with large values and large random message
	 */
	@Test
	public void validSigLargValsRandomMsg(){
		RSASignature s = new RSASignature();
		BigInteger m = new BigInteger("123456789012345678901234567890123456789012345678901234567890");
		s.setVarsLarge(m);
		s.sign(m);
		assertTrue(prfVrf.vrfRSASign(s, m));
	}
	
	
	/**
	 * Tests valid signature with small values and static message
	 */
	@Test
	public void validSigSmallStaticVals(){
		RSASignature s = new RSASignature();
		BigInteger m = new BigInteger("4");
		s.setStaticVars();
		s.sign(m);
		assertTrue(prfVrf.vrfRSASign(s, m));
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
		RSASignature s = new RSASignature();
		BigInteger m = BigInteger.valueOf(r);
		s.setVarsSmall(m,p,q);
		s.sign(m);
		assertTrue(prfVrf.vrfRSASign(s, m));
	}

}