package ch.bfh.univoteverifier.primitives;

import ch.bfh.univoteverifier.utils.RSASignature;
import java.math.BigInteger;


public class RSAVrf {


	public boolean verifySignature(RSASignature s, BigInteger mIn){
//		System.out.println(s);
		BigInteger ver = s.sig.modPow(s.e, s.n);
//		System.out.println("\tm (vrf): " + ver + "\n\tm (Orig): " + mIn);
		boolean result = ver.equals(mIn);
		System.out.println(result);
		return result;
	}

}