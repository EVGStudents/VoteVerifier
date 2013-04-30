/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.gui;

/**
 *
 * @author prinstin
 */
public class ElectionReceipt {

    String eID, encVa, encVb, vk, pC, pR, vSA, vSB, sSId, sT, sV;

    public ElectionReceipt(String[][] values) {
        populateVariables(values);
    }

    private void populateVariables(String[][] values) {
        eID = values[0][1];
        encVa = values[1][1];
        encVb = values[2][1];
        vk = values[3][1];
        pC = values[4][1];
        pR = values[5][1];
        vSA = values[6][1];
        vSB = values[7][1];
        sSId = values[8][1];
        sT = values[9][1];
        sV = values[10][1];
    }

    @Override
    public String toString() {
        String s = "\n\tElection Results (likely from QR Code)"
                + " eID= " + this.eID
                + " encVa= " + this.encVa
                + " encVb= " + this.encVb
                + " vk= " + this.vk
                + " pC= " + this.pC
                + " pR= " + this.pR
                + " vSA= " + this.vSA
                + " sSId= " + this.sSId
                + " sT= " + this.sT
                + " sV= " + this.sV
                + " sV= " + this.sV;
        return s;
    }

    /**
     * Get the election ID from this Election Receipt.
     *
     * @return String of the election ID
     */
    public String geteID() {
        return eID;
    }

    /**
     * Get the 'a' value of this encryption pair E = Enc y (m, r) = (a, b) from
     * this Election Receipt.
     *
     * @return String of a value of an encryption pair
     */
    public String getEncVa() {
        return encVa;
    }

    /**
     * Get the 'b' value of this encryption pair E = Enc y (m, r) = (a, b) from
     * this Election Receipt.
     *
     * @return String of b value of an encryption pair
     */
    public String getEncVb() {
        return encVb;
    }

    /**
     * Get the election ID from this Election Receipt.
     *
     * @return
     */
    public String getVk() {
        return vk;
    }

    /**
     * Get the commitment from the proof in this election receipt.
     *
     * @return String the commitment value of a proof
     */
    public String getpC() {
        return pC;
    }

    /**
     * Get the response from the proof in this election receipt.
     *
     * @return String the response value of a proof
     */
    public String getpR() {
        return pR;
    }

    /**
     * Get the a value from this schnorr signature in this election receipt. a =
     * H(m||g^r )
     *
     * @return String the 'a' value of a schnorr signature
     */
    public String getvSA() {
        return vSA;
    }

    /**
     * Get the a value from this schnorr signature in this election receipt. b =
     * r − a · sk
     *
     * @return String the 'b' value of a schnorr signature
     */
    public String getvSB() {
        return vSB;
    }

    /**
     * Get the signature issuer ID from this Election Receipt.
     *
     * @return
     */
    public String getsSId() {
        return sSId;
    }

    /**
     * Get the timestamp from this Election Receipt.
     *
     * @return the string representation of the timestamp in this Election
     * Receipt
     */
    public String getsT() {
        return sT;
    }

    /**
     * Get the signature value from this Election Receipt.
     *
     * @return the String value of this signature value in this Election Receipt
     */
    public String getsV() {
        return sV;
    }
}
