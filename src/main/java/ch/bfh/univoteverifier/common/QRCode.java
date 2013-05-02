/**
*
*  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
*   Bern University of Applied Sciences, Engineering and Information Technology,
*   Research Institute for Security in the Information Society, E-Voting Group,
*   Biel, Switzerland.
*
*   Project independent UniVoteVerifier.
*
*/
package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.gui.GUIconstants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.*;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author prinstin
 */
public class QRCode {

    private Messenger msgr;
    ResourceBundle rb;
    /**
     * instantiate the class
     */
    public QRCode(Messenger msgr) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        this.msgr=msgr;
    }

    
    /**
     * Creates the QRCode for a given text.  This method is used for testing purposes
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
     * @param filename the File object containing the path to the QRcode image
     * @return a string of the value represented by the QRCode
     * @throws IOException if the file cannot be opened, an exception is thrown
     */
    public String decode(File filename) throws IOException {
        Result result = null;

        FileInputStream fis = new FileInputStream(filename);
        BufferedImage image = ImageIO.read(fis);
        BufferedImageLuminanceSource bils = null;
        try {
            bils = new BufferedImageLuminanceSource(image);
        } catch (NullPointerException ex) {
            msgr.sendFatalErrorMsg(rb.getString("fileReadError"));
            throw new RuntimeException(rb.getString("fileReadError"));
        }

        HybridBinarizer hb = new HybridBinarizer(bils);
        BinaryBitmap binaryBitmap = new BinaryBitmap(hb);

        try {
            result = new MultiFormatReader().decode(binaryBitmap);
        } catch (NotFoundException ex) {
            Logger.getLogger(QRCode.class.getName()).log(Level.SEVERE, null, ex);
        }

        String returnStr = "error";
        if (result != null) {
            returnStr = result.toString();
        }
        return returnStr;
    }

    /**
     * decode the QRCode from an election receipt and pack the info in to a helper class
     * @param filename the File object containing the path to the QRcode image
     * @return the ElectionReceipt helper class with the information packed into it
     */
    public ElectionReceipt decodeReceipt(File filename) throws IOException{
        
        String decoded = decode(filename);
        String[] groupedCleaned = groupAndCleanDecode(decoded);
        String[][] results = separateDataPairs(groupedCleaned);
        return new ElectionReceipt(results);
    }
    
    public String removeBrackets(String str ){
        str = str.replace("{", "");
        str = str.replace("}", "");
        return str;
    }
    
    public String removeSlashAndQuotes(String str ){
        str = str.replace("\"", "");
        return str;
    }
        
    public String[] groupReceiptData(String str){
        Pattern pattern = Pattern.compile(",");
        String[] grouped = pattern.split(str);
        return grouped;
    }
    
    public String[] groupAndCleanDecode(String str){
        str = removeBrackets(str);
        str = removeSlashAndQuotes(str);
        return groupReceiptData(str);
    }
    
    public String[][] separateDataPairs(String[] pairs){
        String[][] separated = new String[pairs.length][2];
        Pattern pattern = Pattern.compile(":");
        for (int i =0; i<pairs.length;i++){
            String[] split  = pattern.split(pairs[i],2);
            String str0 = split[0];
            separated[i][0] = str0;
            String str1 = split[1];
            separated[i][1] = str1;
        }
        return separated;
    }
}
