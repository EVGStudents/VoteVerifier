package utils;

import java.math.BigInteger;
import java.util.Random;

import common.Config;

public class ProofDiscreteLog {

	//Proof parameters
	public String name;
	public BigInteger t;
	public BigInteger c;
	public BigInteger s;

	//public parameters
	public BigInteger vk;	
	public BigInteger q;
	public BigInteger p;
	public BigInteger g;

	public ProofDiscreteLog(){}
	
	public ProofDiscreteLog(String s) {
		this.name =s;
	}

	@Override
	public String toString(){
		String s="\n\tProof t= "+this.t+"    c= "+ this.c +"    s= "+this.s  +"    vk= "+this.vk;
		s+="\n\tParameters p= "+this.p+"    q= "+ this.q +"    g= "+this.g;
		if (name!=null)
			s="\t"+this.name+s;
		return s;
	}

	/**
	 * Generate a mock Proof object to use to test the verification method
	 * the values in the proof are small 
	 */
	public ProofDiscreteLog getProofSmall(){
		//set public parameters
		 q = new BigInteger("11");
		 p = new BigInteger("23");
		 g = new BigInteger("2");
		
		//set private and public verification key
		 BigInteger sk = new BigInteger("4");
		 vk = g.modPow(sk, p);

		//calculate the proof
//		Random r = new Random();
		//BigInteger w= new BigInteger (String.valueOf(r.nextInt()));
		BigInteger w = new BigInteger("3");

		this.name="Mock Proof with small values for testing";
		//Compute t = g ^ w mod p
		this.t = g.modPow(w, p);
		// the concatenation is currently rudimentary
		BigInteger[] concatB = {vk,t};
		this.c = CryptoUtils.concatArrayContents(concatB);
		this.s = w.add(this.c.multiply(sk).mod(q));
		System.out.println(this);
		return this;
	}
	
	/**
	 * Generate a mock Proof object to use to test the verification method
	 * the values in the proof are small 
	 */
	public ProofDiscreteLog getProofLarge(){
		//set public parameters
		 q = Config.q;
		 p = Config.q;
		 g = Config.g;
		
		//set private and public verification key
		 BigInteger sk = new BigInteger("4");
		 vk = g.modPow(sk, p);

		//calculate the proof
//		Random r = new Random();
		//BigInteger w= new BigInteger (String.valueOf(r.nextInt()));
		BigInteger w = new BigInteger("3");

		this.name="Mock Proof with real values for testing";
		//Compute t = g ^ w mod p
		this.t = g.modPow(w, p);
		
		BigInteger[] concatB = {vk,t};
		this.c = CryptoUtils.concatArrayContents(concatB);

		this.s = w.add(this.c.multiply(sk).mod(q));
		System.out.println(this);
		return this;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigInteger getT() {
		return t;
	}
	public void setT(BigInteger t) {
		this.t = t;
	}
	public BigInteger getC() {
		return c;
	}
	public void setC(BigInteger c) {
		this.c = c;
	}
	public BigInteger getS() {
		return s;
	}
	public void setS(BigInteger s) {
		this.s = s;
	}

	public BigInteger getVk() {
		return vk;
	}

	public BigInteger getQ() {
		return q;
	}

	public BigInteger getP() {
		return p;
	}

	public BigInteger getG() {
		return g;
	}
}
