package ch.bfh.univoteverifier.gui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.*;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
            bitMatrix = writer.encode("HTTP://WWW.GOOGLE.COM", BarcodeFormat.QR_CODE, 300, 300);
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
}
