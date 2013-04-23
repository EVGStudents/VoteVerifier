package ch.bfh.univoteverifier.common;

import ch.bfh.univoteverifier.gui.ElectionReceipt;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author prinstin
 */
public class QRCode {

    /**
     * instantiate the class
     */
    public QRCode() {
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
//        String str = "http://www.osgate.org/";
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
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filename)))));
            result = new MultiFormatReader().decode(binaryBitmap);
        } catch (NotFoundException | FileNotFoundException ex) {
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
    public ElectionReceipt decodeReceipt(File filename) {
        return new ElectionReceipt();
    }
}
