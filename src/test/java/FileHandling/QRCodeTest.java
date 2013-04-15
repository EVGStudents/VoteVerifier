/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FileHandling;

import ch.bfh.univoteverifier.gui.MainGUI;
import ch.bfh.univoteverifier.common.QRCode;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prinstin
 */
public class QRCodeTest {

    public QRCodeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void QRCodeDecodesFromFile() {
        QRCode qr = new QRCode();
        File file;
        String resultStr = "nothing";
        try {
            file = new File("/home/prinstin/bfh/BacherlorArbeit/Images/OSGateQRCode.jpeg");
            resultStr = qr.decode(file);
        } catch (IOException ex) {
            Logger.getLogger(QRCodeTest.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
        Logger.getLogger(QRCodeTest.class.getName()).log(Level.INFO, "From path name OSGateQRCode{0}", resultStr);
        assertTrue(0 == "http://www.osgate.org/".compareTo(resultStr));
    }

    @Test
    public void QRCodeDecodesElectionInfo() {
        QRCode qr = new QRCode();
        File file = new File(MainGUI.class
                .getResource("/qrcodeGiu").getPath());
        String resultStr = "nothing";
        try {
            resultStr = qr.decode(file);
        } catch (IOException ex) {
            Logger.getLogger(QRCodeTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        String correctDecode = "{\"eID\":\"vsbfh-2013\",\"encVa\":\"72w86v8UbkIKBaoN6sPQPSP1a7dQf2ivaiU91L5Orindc4XqfVVznFRegKiK2G3P3tYqjijaLdBQjZEJSU0lXVkDDCbl_fGOt=DxlXNNGgdUn_IfhSefshyNHFzYnOHDPlFeZwOmO91zFt3woxWjSwMI0UhbnezFgvrpXp5fuYY\",\"encVb\":\"A38NYQfzQSA=73rLcStNKQCatMgXJyJ0sMa9wjPLkz92oGoQERk4ItanwOEXyyKEIfpu2nwqRhuGKpn9GU39h2_myjliO04Egz1DiS1ZaO7RrjU7fKSMlXU1hQbHOHvyDAZXX9EqY1gBVpT=u=eMVCf6TI0OFf8P86pDni=MB8Y\",\"vk\":\"4lfTbadkitS1NOXSiDhLKrTUh1J=gdOupOzFHgexC4LdYRuV9PIzblQgHfLYNtlEW8i6Tjr1FVv6HJZ902PHeyG8vrkTCJJEODOdZJ7TFzo8WCIywtyqXcFF52n_sg70FNRSLZcEBY6lCcRWWzBJxdGnbBLpl11QGcJ25cG6RKP\",\"pC\":\"2Qz1K9y14qGzBRQqJ7gAyezta3bi7pHY7307sHQxDYVlF6aQlwJDABY16gIYZNnTo121Lt8SXKpsn_rhGgkSqQMGmPnUkieepqyPg9ulkwEAvKBK80dlxzEFvNfaw9_a0SRR2aKIgWhuvAouPHlN9FMzLexlNApbKTTHNQ7dvR\",\"pR\":\"B4YMumm5bLYnT6iYhrQJT6fcMyCOtqQJOMsNxyhncpLCYIkhEVumA4tbB5HitapQU7nVXP_ZrS51VacXbkGU6LZ_BHy3je_iZoMY=5FjLNppoOWitzzkToFkL=bhlIsnMHQhJizXSNAGpRLPTvkI7bhGb=FxousxxCx0=RGYH3\",\"vSA\":\"6rQltQyKPrzIAPaZnCBp0x2F0N2AC4g5JprF_R_Qycs\",\"vSB\":\"3x73szdurz40HjfaYTBF42ecyL9mN56wFTZDGF8a3FN\",\"sSId\":\"electionmanager\",\"sT\":\"2013-03-25T10:58:11.000Z\",\"sV\":\"ShZKrcEYQnsA2Ks352oLIUABAaglxmNsCaYIfKzM_tfRARyYPgNSxNLtNcaQf10VqEq57DArGrhvjLjJwmU5mfip3FRUqZ1hGNFxkqdYM8bLC0uhoiALyHVFkLu4etQG4Dp8RuJ5dmp5UTs0QcEHEIIQcd8_=J7iIeqRLAlcU6\"}";
        Logger.getLogger(QRCodeTest.class.getName()).log(Level.SEVERE, "Path of file: {0}", file.getPath());
        Logger.getLogger(QRCodeTest.class.getName()).log(Level.SEVERE, "QRCode decoded to: {0}", resultStr);
        assertTrue(0 == correctDecode.compareTo(resultStr));
    }
    

}
