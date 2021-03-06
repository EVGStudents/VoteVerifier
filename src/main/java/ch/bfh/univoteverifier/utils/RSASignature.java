/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 * 
* Project independent VoteVerifier.
 * 
*/
package ch.bfh.univoteverifier.utils;

import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.gui.MainGUI;
import java.math.BigInteger;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Justin Springer contains and generates parameters and contents of an RSA
 * signature
 */
public class RSASignature {

    Random r = new Random();
    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger phi;
    private BigInteger m;
    private BigInteger e;
    private BigInteger d;
    private BigInteger sig;

    /**
     * sets the class variables to small static parameters for testings the RSA
     * methods
     */
    public void setStaticVars() {
        p = new BigInteger("3");
        q = new BigInteger("11");
        n = new BigInteger("33");
        m = new BigInteger("4");
        e = new BigInteger("3");
        d = new BigInteger("7");
        phi = calculatePhi();
    }

    /**
     * sets the class variables to small parameters for testings the RSA methods
     *
     * @param mIn the message to be signed
     * @param p int p : a prime number to be used to calculate the parameters
     * @param q int q : a prime number to be used to calculate the parameters
     */
    public void setVarsSmall(BigInteger mIn, int p, int q) {
        this.p = BigInteger.valueOf(p);
        this.q = BigInteger.valueOf(q);
        n = this.p.multiply(this.q);
        phi = calculatePhi();
        m = mIn;
        setKeyPair();
    }

    /**
     * sets the class's variables to large "real world" parameters for testings
     * the RSA methods
     *
     * @param mIn the message to verify against the signature
     */
    public void setVarsLarge(BigInteger mIn) {
        p = Config.p;
        q = Config.q;
        n = p.multiply(q);
        phi = calculatePhi();

        //int moduloLen = n.bitCount();
        //m = new BigInteger(moduloLen, r);

        m = mIn;
        setKeyPair();
    }

    /**
     * Generates random values until a multiplicative inverse can be found sets
     * then values d and e, the private and public key for a signature
     */
    public void setKeyPair() {
        boolean foundIversible = false;
        boolean error = false;

        //try new values of the public key until an inverse is successfully found
        //some random values have no multiplicative inverse
        while (!foundIversible) {
            error = false;
            int bitLen = phi.bitCount();
            LOGGER.log(Level.INFO, "bitLen :{0}  phi: {1}", new Object[]{bitLen, phi});
            e = new BigInteger(bitLen, r);
            try {
                d = e.modInverse(phi);
            } catch (ArithmeticException e) {
                error = true;
                //e.printStackTrace();
            }
            if (!error) {
                foundIversible = true;
                LOGGER.log(Level.INFO, "public key e: {0}\nprivate key d: {1}", new Object[]{e, d});
            }
        }
    }

    /**
     * calculate the euler phi function
     *
     * @return BigInteger : the quantity of numbers smaller than n=p*q that a
     * coprime to n
     */
    public BigInteger calculatePhi() {
        return p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
    }

    /**
     * signs a message with the parameters that should first be generated by
     * this class
     *
     * @param msgInput the message to be signed, BigInteger between 1 and phi or
     * 1 and n?
     */
    public void sign(BigInteger msgInput) {
        sig = msgInput.modPow(d, n);
    }

    /**
     * get the modulo used for this signature
     *
     * @return BigInteger n: the modulo used for this signature
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * get the public key that corresponds to the private key used to sign this
     * message
     *
     * @return BigInteger e the public key
     */
    public BigInteger getE() {
        return e;
    }

    /**
     * get the private key used to sign this message this method would only be
     * used in the case of testing and that this is a mock object
     *
     * @return BigInteger d the private key
     */
    public BigInteger getD() {
        return d;
    }

    /**
     * get the signature
     *
     * @return BigInteger : the signature
     */
    public BigInteger getSig() {
        return sig;
    }

    @Override
    public String toString() {
        String s = "\n\t n= " + this.n + "    p= " + this.p + "    q= " + this.q + "    phi= " + this.phi;
        s += "\n\t m= " + this.m + "    sig= " + this.sig + "    d= " + this.d + "    e= " + this.e;
        return s;
    }
}
