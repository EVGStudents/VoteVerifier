package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import crypto_vrf.PrimitivesVrf;

public class PrimitivesTest {
	
	
	PrimitivesVrf pVrf = new PrimitivesVrf();
	
	
	@Test
	public void primeP(){
		
		assertTrue(pVrf.isSchnorrPPrime());
	}
	
	@Test
	public void primeQ(){
		assertTrue(pVrf.isSchnorrQPrime());
	}

	@Test
	public void paramLength(){
		assertTrue(pVrf.areParametersLength());
	}
	
	@Test
	public void pSafePrime(){
		assertTrue(pVrf.isPSafePrime());
	}
	
}
