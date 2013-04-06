package primitives;

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.primitives.PrimitivesVrf;
import static org.junit.Assert.*;


import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;



public class PrimitivesTest {
	
	
	PrimitivesVrf pVrf;
	PrimitivesVrf pVrfTwo;
	
	
	@Before
	public void setup(){
		//Used to control the class PrimitivesVrf with good values
		pVrf = new PrimitivesVrf(Config.p, Config.q, Config.g);
		
		//Used to control that the class PrimitivesVrf does a good job with erroneous numbers
		pVrfTwo = new PrimitivesVrf(Config.p.add(BigInteger.valueOf(1)), 
				Config.q.add(BigInteger.valueOf(1)), Config.g);
	}
	

	/**
	 * Tests used to check the class with good  values
	 */
	
	@Test
	public void primeP(){	
		assertTrue(pVrf.isPPrime());
	}
	
	@Test
	public void primeQ(){
		assertTrue(pVrf.isQPrime());
	}

	@Test
	public void paramLength(){
		assertTrue(pVrf.areParametersLength(Config.pLength, Config.qLength, Config.gLength));
	}
	
	@Test
	public void pSafePrime(){		
		assertTrue(pVrf.isPSafePrime());
	}
	
	@Test
	public void generator(){		
		assertTrue(pVrf.isGenerator());
	}
	
	/**
	 * Tests used to check that we obtain the correct results
	 * with false values	
	 */
	
	@Test
	public void notPrimeP(){	
		assertFalse(pVrfTwo.isPPrime());
	}
	
	@Test
	public void notPrimeQ(){
		assertFalse(pVrfTwo.isQPrime());
	}
	
	@Test
	public void notSafePrime(){
		assertFalse(pVrfTwo.isPSafePrime());
	}
	
	@Test
	public void notGenerator(){
		assertFalse(pVrfTwo.isGenerator());
	}
	
	public void notParamLength(){
		assertFalse(pVrfTwo.areParametersLength(1025, 257, 1023));
	}
	
}
