/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.gui.ElectionReceipt;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * The QRCode class creates and decodes QR codes and interprets the data stored
 * in an election receipt from the UniVote eVoting system.
 *
 * @author Justin Springer
 */
public class QRCode {
    // TODO Add reference to document that specifies these keys.
    public final static String eID = "eID";
    public final static String encVa = "encVa";
    public final static String encVb = "encVb";
    public final static String rB = "rB";
    public final static String vk = "vk";
    public final static String pC = "pC";
    public final static String pR = "pR";
    public final static String vSA = "vSA";
    public final static String vSB = "vSB";
    public final static String sSId = "sSId";
    public final static String sT = "sT";
    public final static String sV = "sV";

    private Messenger msgr;
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    /**
     * Create an instance of the QRCode class.
     *
     * @param msgr a Messenger object to allow error messages to be shared with
     * the GUI or Console.
     */
    public QRCode(Messenger msgr) {
        this.msgr = msgr;
    }

    /**
     * Creates the QRCode for a given text. This method is used for testing
     * purposes
     *
     * @param str the string from which a QRCode shall be created
     * @return the image of a QRcode
     */
    public ImageIcon createQRCode(String str) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        BufferedImage img = null;
        try {
            bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, 300, 300);
            img = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        ImageIcon imgIcn = new ImageIcon(img);
        return imgIcn;
    }

    /**
     * decodes a QRCode into a String value
     *
     * @param filename the File object containing the path to the QRcode image
     * @return a string of the value represented by the QRCode
     * @throws IOException if the file cannot be opened, an exception is thrown
     */
    public String decode(File filename) {
        String returnStr = "";
        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedImage image = ImageIO.read(fis);
            BufferedImageLuminanceSource bils = null;
            bils = new BufferedImageLuminanceSource(image);
            HybridBinarizer hb = new HybridBinarizer(bils);
            BinaryBitmap binaryBitmap = new BinaryBitmap(hb);
            Result result = new MultiFormatReader().decode(binaryBitmap);
            returnStr = result.toString();
        } catch (NullPointerException ex) {
            msgr.sendSetupError("This file is not a valid QR Code");
        } catch (FileNotFoundException ex) {
            msgr.sendSetupError("This file could not be read");
        } catch (IOException ex) {
            msgr.sendSetupError("This file is not a valid QR Code");
        } catch (Exception ex) {
            msgr.sendSetupError("There is an error reading the file");
        }
        return returnStr;
    }

    /**
     * Decode the QRCode from an election receipt and pack the info in to a
     * helper class.
     *
     * @param filename the File object containing the path to the QRcode image
     * @return the ElectionReceipt helper class with the information packed into
     * it
     */
    public ElectionReceipt decodeReceipt(File filename) throws RuntimeException {
        String decoded = decode(filename);
        if (decoded.length() > 0) {
            String[] groupedCleaned = groupAndCleanDecode(decoded);
            Map<String, String> results = separateDataPairs(groupedCleaned);
            return new ElectionReceipt(results);
        } else {
            throw new RuntimeException("An error occured while processing this file");
        }
    }

    /**
     * Parse out the brackets from a string.
     *
     * @param str the String from which to remove brackets.
     * @return the String with the brackets removed.
     */
    public String removeBrackets(String str) {
        str = str.replace("{", "");
        str = str.replace("}", "");
        return str;
    }

    /**
     * Parse out slashes and quotes from a string.
     *
     * @param str The String from which to remove slashes and quotes.
     * @return the String with the slashes and quotes removed.
     */
    public String removeSlashAndQuotes(String str) {
        str = str.replace("\"", "");
        return str;
    }

    /**
     * Separate by commas the data in a string containing a decoded QRCode from
     * the UniVote eVoting system.
     *
     * @param str the string to split up by commas
     * @return an array of strings which are pairs of data, in a for of "Key and
     * Value".
     */
    public String[] groupReceiptData(String str) {
        Pattern pattern = Pattern.compile(",");
        String[] grouped = pattern.split(str);
        return grouped;
    }

    /**
     * Group and remove excess characters from a decoded QRCode of an Election
     * Receipt from the UniVote eVoting system.
     *
     * @param str the String to remove characters from and to be split up
     * @return an array of strings which are pairs of data, in a for of "Key and
     * Value".
     */
    public String[] groupAndCleanDecode(String str) {
        str = removeBrackets(str);
        str = removeSlashAndQuotes(str);
        return groupReceiptData(str);
    }

    /**
     * Takes strings of pairs of data from the UniVote eVoting system and splits
     * them into separate strings and places them into a map of key/value pairs.
     *
     * @param pairs string of pairs to be split
     * @return map of key/value pairs from an election receipt from the UniVote eVoting
     * system.
     */
    public Map<String, String> separateDataPairs(String[] pairs) {
        Map<String, String> map = new HashMap<String, String>();
        Pattern pattern = Pattern.compile(":");
        for (int i = 0; i < pairs.length; i++) {
            String[] split = pattern.split(pairs[i], 2);
            String str0 = split[0];
            map.put(str0, str0);
            String str1 = split[1];
            map.put(str0, str1);
        }
        return map;
    }
}
