import java.math.BigInteger;
import java.util.Random;


public class NIZKP {

	BigInteger q = new BigInteger("11");
	BigInteger p = new BigInteger("23");
	BigInteger g = new BigInteger("2");
//	BigInteger sk = new BigInteger("8OB6dJsxNeRphDa21h2JO=x43Rwx0AI9OTbZz0QdntT");
	BigInteger sk = new BigInteger("4");
	BigInteger w;
	
	
	class Proof{
		BigInteger t;
		BigInteger c;
		BigInteger s;
	}
	
	public void test(){
		System.out.println(sk);
	}
	Random r = new Random();
	
	Proof pIn = new Proof();
	
	//Compute t = g ^ w mod p
	public void start(){
		
		w= new BigInteger (String.valueOf(r.nextInt()));
		pIn.t = g.modPow(w, p);
		pIn.c = new BigInteger(mockHash(pIn.t));
		pIn.s = w.add(pIn.c.multiply(sk).mod(q));
	}

	public String mockHash(BigInteger h){
		
		return "t";
	}
}
