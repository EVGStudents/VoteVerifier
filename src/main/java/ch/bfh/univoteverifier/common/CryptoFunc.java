package ch.bfh.univoteverifier.common;

import ch.bfh.univote.common.Certificate;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;


/**
 * This class contains some functions used by other classes
 * @author snake
 */
public class CryptoFunc {
	
	private static final Logger LOGGER = Logger.getLogger(CryptoFunc.class.getName());
	
	
	/**
	 * Compute the hash of a number and then put it into a BigInteger.
	 * Change the value in the configuration file to chose the appropriate hash algorithm
	 * @param val BigInteger the value used to compute the hash
	 * @return BigInteger the hash as BigInteger representation
	 * @throws NoSuchAlgorithmException
	 */
	public static BigInteger sha(BigInteger val) throws NoSuchAlgorithmException{
		BigInteger result;
		
		MessageDigest md = MessageDigest.getInstance(Config.hashAlgorithm);
		
		md.update(val.toByteArray());
		
		result = new BigInteger(md.digest()).mod(Config.q);
		
		return result;
	}
	
	/**
	 * Decode a base64 encoded string
	 * @param encodedBase64 the encoded string
	 * @return the byte array of the decoded string, can be used in BigInteger constructur
	 */
	public static byte[] decodeBase64(String encodedBase64){
		return DatatypeConverter.parseBase64Binary(encodedBase64);
	}

	/**
	 * Get an X509 certificate from a byte array
	 * @param b the byte array
	 * @return an X509 certificate
	 * @throws CertificateException 
	 */
	public static X509Certificate getX509Certificate(byte[] b) throws CertificateException{
		InputStream is = new ByteArrayInputStream(b);
		X509Certificate cert = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(is);
		return cert;
	}
	
	
	
	/**
	 * Get the RSAPublic key from a certificate
	 * @param b the byte array representing the certificate
	 * @return the RSA public key
	 * @throws CertificateException
	 */
	public static RSAPublicKey getRSAPublicKeyFromCert(byte[] b) throws CertificateException{
		RSAPublicKey pubKey= (RSAPublicKey) getX509Certificate(b).getPublicKey();
		return pubKey;
	}
	
	/**
	 * ToDO - probably useless
	 * Concatenates n given BigInteger values into a string and pads them with
	 * the arbitrary string 001100
	 * @param c an array of BigInteger values
	 * @return and String value of the concatenated contents of the array
	 */
	public static BigInteger concatArrayContents(BigInteger[] c) throws NoSuchAlgorithmException{
		//TODO the calls to this method from NIZKP need to also send the Vi Voter ID?
		//		BigInteger bigConcat=  BigInteger.ZERO;
		String concat="";
		for(BigInteger ci:c){
			//001100 similates padding
			concat += ci.toString() +"001100";
		}
		return CryptoFunc.sha(new BigInteger(concat));
		
	}
	
	
}
