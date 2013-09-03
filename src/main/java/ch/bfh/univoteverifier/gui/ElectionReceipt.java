/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.QRCode;
import java.math.BigInteger;
import java.util.Map;

/**
 * This is a helper class that contains the contents of an election receipt.
 *
 * @author Justin Springer
 */
public class ElectionReceipt {

    private String eID, sSId, sT;
    private BigInteger encVa, encVb, vk, pC, pR, vSA, vSB, sV;

    /**
     * Create an instance of this helper class.
     *
     * @param values a map of key/value pairss from an election receipt.
     */
    public ElectionReceipt(Map<String, String> values) {
        populateVariables(values);
    }

    /**
     * Fill the instance variables with the values from the values array.
     *
     * @param values An array with the values from an election receipt.
     */
    private void populateVariables(Map<String, String> values) {
        eID = values.get(QRCode.eID);
        encVa = CryptoFunc.decodeSpecialBase64(values.get(QRCode.encVa));
        encVb = CryptoFunc.decodeSpecialBase64(values.get(QRCode.encVb));
        vk = CryptoFunc.decodeSpecialBase64(values.get(QRCode.vk));
        pC = CryptoFunc.decodeSpecialBase64(values.get(QRCode.pC));
        pR = CryptoFunc.decodeSpecialBase64(values.get(QRCode.pR));
        vSA = CryptoFunc.decodeSpecialBase64(values.get(QRCode.vSA));
        vSB = CryptoFunc.decodeSpecialBase64(values.get(QRCode.vSB));
        sSId = values.get(QRCode.sSId);
        sT = values.get(QRCode.sT);
        sV = CryptoFunc.decodeSpecialBase64(values.get(QRCode.sV));
    }

    /**
     * Put the contents of this helper class into a string.
     *
     * @return String the content of the variables of this helper class in a
     * string.
     */
    @Override
    public String toString() {
        String s = "\n\tElection Results (likely from QR Code)"
                + " eID= " + this.eID + "\n"
                + " encVa= " + this.encVa + "\n"
                + " encVb= " + this.encVb + "\n"
                + " vk= " + this.vk + "\n"
                + " pC= " + this.pC + "\n"
                + " pR= " + this.pR + "\n"
                + " vSA= " + this.vSA + "\n"
                + " sSId= " + this.sSId + "\n"
                + " sT= " + this.sT + "\n"
                + " sV= " + this.sV + "\n"
                + " sV= " + this.sV + "\n";
        return s;
    }

    /**
     * Get the election ID from this Election Receipt.
     *
     * @return String of the election ID
     */
    public String getElectionID() {
        return eID;
    }

    /**
     * Get the 'a' value of this encryption pair E = Enc y (m, r) = (a, b) from
     * this Election Receipt.
     *
     * @return BigInteger of the 'a'-value of an encryption pair
     */
    public BigInteger getEncValueA() {
        return encVa;
    }

    /**
     * Get the 'b' value of this encryption pair E = Enc y (m, r) = (a, b) from
     * this Election Receipt.
     *
     * @return BigInteger of the 'b'-value of an encryption pair
     */
    public BigInteger getEncValueB() {
        return encVb;
    }

    /**
     * Get the verification key value from this Election Receipt.
     *
     * @return the BigInteger value of the verification key
     */
    public BigInteger getVerificationKey() {
        return vk;
    }

    /**
     * Get the commitment from the proof in this election receipt.
     *
     * @return BigInteger the commitment value of a proof
     */
    public BigInteger getProofCommitment() {
        return pC;
    }

    /**
     * Get the response from the proof in this election receipt.
     *
     * @return BigInteger of the response value of a proof
     */
    public BigInteger getProofResponse() {
        return pR;
    }

    /**
     * Get the 'a'-value from this schnorr signature in this election receipt. a
     * = H(m||g^r )
     *
     * @return BigInteger the 'a'-value of a schnorr signature
     */
    public BigInteger getSchnorrValueA() {
        return vSA;
    }

    /**
     * Get the 'b'-value from this schnorr signature in this election receipt. b
     * = r − a · sk
     *
     * @return BigInteger the 'b'-value of a schnorr signature
     */
    public BigInteger getSchnorrValueB() {
        return vSB;
    }

    /**
     * Get the signature issuer ID from this Election Receipt.
     *
     * @return
     */
    public String getSignatureIssuerID() {
        return sSId;
    }

    /**
     * Get the timestamp from this Election Receipt.
     *
     * @return the string representation of the timestamp in this Election
     * Receipt
     */
    public String getTimeStamp() {
        return sT;
    }

    /**
     * Get the signature value from this Election Receipt.
     *
     * @return the BigInteger value of this signature value in this Election
     * Receipt
     */
    public BigInteger getSignatureValue() {
        return sV;
    }
}
