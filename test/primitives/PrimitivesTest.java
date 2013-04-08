package primitives;

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.primitives.PrimitivesVerifier;
import java.math.BigInteger;
import static org.junit.Assert.*;
import org.junit.Test;



public class PrimitivesTest {
	
	
	PrimitivesVerifier pVrf = new PrimitivesVerifier();
	
	
	/**
	 * Tests used to check the class with good  values
	 */
	
	@Test
	public void primeP(){	
		assertTrue(pVrf.vrfPrimeNumber(Config.p));
	}
	
	@Test
	public void primeQ(){
		assertTrue(pVrf.vrfPrimeNumber(Config.q));
	}

	@Test
	public void paramLength(){
		assertTrue(pVrf.vrfParamLen(Config.p, Config.q, Config.g, Config.pLength, Config.qLength, Config.gLength));
	}
	
	@Test
	public void pSafePrime(){		
		assertTrue(pVrf.vrfSafePrime(Config.p, Config.q));
	}
	
	@Test
	public void generator(){		
		assertTrue(pVrf.vrfGenerator(Config.g, Config.p, Config.q));
	}
	
	/**
	 * Tests used to check that we obtain the correct results
	 * with false values	
	 */
	
	@Test
	public void notPrimeP(){	
		assertFalse(pVrf.vrfPrimeNumber(Config.p.add(new BigInteger("1"))));
	}
	
	@Test
	public void notPrimeQ(){
		assertFalse(pVrf.vrfPrimeNumber(Config.q.add(new BigInteger("1"))));
	}
	
	@Test
	public void notSafePrime(){
		assertFalse(pVrf.vrfSafePrime(Config.p.add(new BigInteger("1")),Config.q.add(new BigInteger("1"))));
	}
	
	@Test
	public void notGenerator(){
		assertFalse(pVrf.vrfGenerator(Config.g.add(new BigInteger("1")), Config.p, Config.q));
	}

	@Test
	public void notParamLength(){
		assertFalse(pVrf.vrfParamLen(Config.p, Config.q, Config.g, 1025, 257, 1023));
	}
	
}
