package crypto_vrf;

import java.math.BigInteger;
import java.util.Random;
import common.Config;

public class RSAvrf {


	Random r = new Random();

	BigInteger p ;
	BigInteger q ;
	BigInteger n ;
	BigInteger phi ;
	BigInteger m ;
	BigInteger e ;
	BigInteger d ;

	public RSAvrf(){
		//setVarsSmall();
		setVarsRealistic();
	}

	public void setVarsSmall(){
		p = new BigInteger("3");
		q = new BigInteger("11");
		n = new BigInteger("33");
		m = new BigInteger("4");
		e = new BigInteger("3");
		d = new BigInteger("7");
	}

	public void setVarsRealistic() {
		p = Config.p;
		q = Config.q;
		n = p.multiply(q);
		phi = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
		int moduloLen = n.bitCount();
		m = new BigInteger(moduloLen, r);
		boolean foundIversible= false;
		boolean error = false;

		//try new values of the public key until an inverse is successfully found
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
	 * @param args
	 */
	public static void main(String[] args) {
		RSAvrf rsa = new RSAvrf();
		rsa.start();
	}


	public void start(){
		BigInteger sig = sign(m);
		boolean result = verifySignature(sig, m);
		System.out.println(result);

	}

	public BigInteger sign(BigInteger msgInput){
		BigInteger sig= msgInput.modPow(d, n);
		System.out.println("msg input data: " + msgInput + "  signature: " + sig);
		return sig;
	}

	
	public boolean verifySignature(BigInteger s, BigInteger mIn){
		BigInteger ver = s.modPow(e, n);
		System.out.println("\tm (vrf): " + ver + "\n\tm (Orig): " + mIn);
		//		return 0==ver.compareTo(data);
		return ver.equals(mIn);
	}

}
