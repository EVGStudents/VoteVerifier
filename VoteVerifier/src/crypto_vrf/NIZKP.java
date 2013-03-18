package crypto_vrf;
import java.math.BigInteger;
import java.util.Random;


public class NIZKP {
	
	Random r = new Random();
	
	BigInteger q = new BigInteger("11");
	BigInteger p = new BigInteger("23");
	BigInteger g = new BigInteger("2");
//	BigInteger sk = new BigInteger("8OB6dJsxNeRphDa21h2JO=x43Rwx0AI9OTbZz0QdntT");
	BigInteger sk = new BigInteger("4");
	BigInteger vk = g.modPow(sk, p);
//	use this random value after testing is complete
	BigInteger w= new BigInteger (String.valueOf(r.nextInt()));
//	BigInteger w = new BigInteger("3");
	
	
	class Proof{
		BigInteger t;
		BigInteger c;
		BigInteger s;
	}

	public void start(){
		Proof pIn = generateProofMock();
		boolean validProof = verifyProof(pIn);
		System.out.println("The proof is valid : " + validProof);
	}

	public Proof generateProofMock(){
		Proof prfMock = new Proof();
		//Compute t = g ^ w mod p
		prfMock.t = g.modPow(w, p);
		// here the addition is imitating the hash value that will come
		prfMock.c = vk.add(prfMock.t).mod(q);
		prfMock.s = w.add(prfMock.c.multiply(sk).mod(q));
		printProof(prfMock, "Mock Proof Generated");
		return prfMock;
	}
	
	public boolean verifyProof(Proof prf){
//		check that c = H(VKi||t||Vi) mod q
//		Compute v = g^s mod p
//		compute w = (t * VKi^c) mod p
//		check that v = w
		
		BigInteger c2;
		
		int validProof = 0;

		c2 = vk.add(prf.t).mod(q);
		validProof += c2.compareTo(prf.c);
		if (validProof!=0) System.out.println("FAILED: prf.c == vk.add(prf.t).mod(p)");
		
		BigInteger v = g.modPow(prf.s, p);
		BigInteger w = (prf.t.multiply(vk.pow(prf.c.intValue()))).mod(p);
//		System.out.println("v: "+v+ "  w "+w);
		validProof  += (v.compareTo(w));
		if (validProof!=0)System.out.println("FAILED: Second Part");
		
		return 0==validProof;
	}
	
	public void printProof(Proof p, String s){
		System.out.println(s);
		System.out.println("Proof t= "+p.t+"   Proof c= "+ p.c +"   Proof s= "+p.s);
	}

}
