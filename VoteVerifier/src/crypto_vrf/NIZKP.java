package crypto_vrf;

import java.math.BigInteger;
import java.util.Random;
import utils.ProofDiscreteLog;


public class NIZKP {
		
	/**
	 * verify a Non-Interactive Zero Knowledge Proof of Discrete Logs
	 * @param proof and helper class object with fields t,c,s corresponding to a ZKP
	 * @return true if the proof is correct
	 */
	public boolean verifyProofDiscreteLog(ProofDiscreteLog prf){
//		check that c = H(VKi||t||Vi) mod q
//		Compute v = g^s mod p
//		compute w = (t * VKi^c) mod p
//		check that v = w
		
		BigInteger c2;
		
		int validProof = 0;

		c2 = prf.vk.add(prf.t).mod(prf.q);
		validProof += c2.compareTo(prf.c);
		if (validProof!=0) System.out.println("FAILED: prf.c == vk.add(prf.t).mod(p)");
		
		BigInteger v = prf.g.modPow(prf.s, prf.p);
		BigInteger w = (prf.t.multiply(prf.vk.modPow(prf.c,prf.p))).mod(prf.p);
//		System.out.println("v: "+v+ "  w "+w);
		validProof  += (v.compareTo(w));
		if (validProof!=0)System.out.println("FAILED: Second Part");
		
		boolean results = 0==validProof;
		System.out.println("The proof is valid : " + results);
		return results;
	}
	


}
