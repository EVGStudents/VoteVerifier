package utils;

import java.math.BigInteger;
import java.util.Random;

import common.Config;


/**
 * @author prinstin
 * contains and generates parameters and contents of an RSA signature
 */
public class RSASignature {


	Random r = new Random();

	public BigInteger p ;
	public BigInteger q ;
	public BigInteger n ;
	public BigInteger phi ;
	public BigInteger m ;
	public BigInteger e ;
	public BigInteger d ;
	public  BigInteger sig;
	
	/**
	 * sets the class variables to small static parameters for 
	 * testings the RSA methods
	 */
	public void setStaticVars(){
		p = new BigInteger("3");
		q = new BigInteger("11");
		n = new BigInteger("33");
		m = new BigInteger("4");
		e = new BigInteger("3");
		d = new BigInteger("7");
	}
	
	/**
	 * sets the class variables to small parameters for 
	 * testings the RSA methods
	 */
	public void setVarsSmall(BigInteger mIn, int p, int q){
		this.p = BigInteger.valueOf(p);
		this.q = BigInteger.valueOf(q);
		n = this.p.multiply(this.q);
		phi = this.p.subtract(new BigInteger("1")).multiply(this.q.subtract(new BigInteger("1")));
		m = mIn;
		setKeyPair();
	}


	/**
	 * sets the class's variables to large "real world" parameters for 
	 * testings the RSA methods
	 * @param mIn the message to verify against the signature
	 */
	public void setVarsLarge(BigInteger mIn) {
		p = Config.p;
		q = Config.q;
		n = p.multiply(q);
		phi = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
		
//			int moduloLen = n.bitCount();
//			m = new BigInteger(moduloLen, r);
//		
			m = mIn;
			setKeyPair();
	}

	/**
	 * Generates random values until a multiplicative inverse can be found
	 * sets then values d and e, the private and public key for a signature
	 */
	public void setKeyPair(){
		boolean foundIversible= false;
		boolean error = false;

		//try new values of the public key until an inverse is successfully found
		//some random values have no multiplicative inverse
		while(!foundIversible){
			error=false;
			int bitLen = phi.bitCount();
			System.out.println("bitLen :" + bitLen +"  phi: " + phi);
			e = new BigInteger(bitLen,r);
			try {
				d = e.modInverse(phi);
			} catch (ArithmeticException e) {
				error= true;
				//e.printStackTrace();
			}
			if (!error) {foundIversible=true;
			System.out.println("public key e: " + e + "\nprivate key d: "+d);
			}
		}
	}

	/**
	 * signs a message with the parameters that should first be generated by this class
	 * @param msgInput the message to be signed, BigInteger between 1 and phi or 1 and n?
	 * @return BigInteger signature value of the given msgInput
	 */
	public void sign(BigInteger msgInput){
		sig= msgInput.modPow(d, n);
		System.out.println("msg input data: " + msgInput + "  signature: " + sig);
	}

	
	
	@Override
	public String toString(){
		String s="\n\t n= "+this.n+"    p= "+ this.p +"    q= "+this.q  +"    phi= "+this.phi;
		s+="\n\t m= "+this.m+"    sig= "+ this.sig +"    d= "+this.d +"    e= "+this.e;
		return s;
	}
	
}
