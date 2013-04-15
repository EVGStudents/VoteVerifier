package ch.bfh.univoteverifier.gui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.*;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.*;
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

    public QRCode() {
    }

    public ImageIcon testQRCode() {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        BufferedImage img = null;
        try {
            bitMatrix = writer.encode("http://www.osgate.org/", BarcodeFormat.QR_CODE, 300, 300);
//            MatrixToImageWriter.writeToFile(bitMatrix, "gif", new File("C:\\output.gif"));
            img = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
        }
        ImageIcon imgIcn = new ImageIcon(img);
        return imgIcn;
    }

    public String decode(File filename) throws IOException {
        Result result = null;
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filename)))));
            result = new MultiFormatReader().decode(binaryBitmap);
        } catch (NotFoundException | FileNotFoundException ex) {
            Logger.getLogger(QRCode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String returnStr="error";
        if (result != null) 
            returnStr = result.toString();
        return returnStr;
    }
    
    public ElectionReceipt decodeReceipt(){
    
    }
}
