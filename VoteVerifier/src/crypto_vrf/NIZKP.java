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
		
		String name;
		BigInteger t;
		BigInteger c;
		BigInteger s;
		
		public Proof(){}
		public Proof(String s){
			this.name =s;
		}
	
		@Override
		public String toString(){
			return "\t"+this.name+"\n\tProof t= "+this.t+"   Proof c= "+ this.c +"   Proof s= "+this.s;
		}
	}

	public void start(){
		Proof pIn = generateProofMock();
		boolean validProof = verifyProof(pIn);
		System.out.println("The proof is valid : " + validProof);
	}

	/**
	 * Generate a mock Proof object to use to test the verification method
	 */
	public Proof generateProofMock(){
		Proof prfMock = new Proof("Mock Proof for testing");
		//Compute t = g ^ w mod p
		prfMock.t = g.modPow(w, p);
		// here the addition is imitating the hash value that will come
		prfMock.c = vk.add(prfMock.t).mod(q);
		prfMock.s = w.add(prfMock.c.multiply(sk).mod(q));
		System.out.println(prfMock);
		return prfMock;
	}
	
	/**
	 * verify a Non-Interactive Zero Knowledge Proof
	 * @param proof and helper class object with fields t,c,s corresponding to a ZKP
	 * @return true if the proof is correct
	 */
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
	


}
