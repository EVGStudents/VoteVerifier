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
package ch.bfh.univoteverifier.filehandling;

import ch.bfh.univoteverifier.common.CryptoFunc;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.gui.MainGUI;
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prinstin
 */
public class QRCodeTest {

    /**
     * Test that the QR code can be read from a path on the computer.
     */
    @Test
    public void QRCodeDecodesElectionInfo() throws IOException {
        Messenger msgr = new Messenger();
        QRCode qr = new QRCode(msgr);
        File file = new File(MainGUI.class
                .getResource("/qrcodeGiu").getPath());
        String resultStr = qr.decode(file);
        String correctDecode = "{\"eID\":\"vsbfh-2013\",\"encVa\":\"72w86v8UbkIKBaoN6sPQPSP1a7dQf2ivaiU91L5Orindc4XqfVVznFRegKiK2G3P3tYqjijaLdBQjZEJSU0lXVkDDCbl_fGOt=DxlXNNGgdUn_IfhSefshyNHFzYnOHDPlFeZwOmO91zFt3woxWjSwMI0UhbnezFgvrpXp5fuYY\",\"encVb\":\"A38NYQfzQSA=73rLcStNKQCatMgXJyJ0sMa9wjPLkz92oGoQERk4ItanwOEXyyKEIfpu2nwqRhuGKpn9GU39h2_myjliO04Egz1DiS1ZaO7RrjU7fKSMlXU1hQbHOHvyDAZXX9EqY1gBVpT=u=eMVCf6TI0OFf8P86pDni=MB8Y\",\"vk\":\"4lfTbadkitS1NOXSiDhLKrTUh1J=gdOupOzFHgexC4LdYRuV9PIzblQgHfLYNtlEW8i6Tjr1FVv6HJZ902PHeyG8vrkTCJJEODOdZJ7TFzo8WCIywtyqXcFF52n_sg70FNRSLZcEBY6lCcRWWzBJxdGnbBLpl11QGcJ25cG6RKP\",\"pC\":\"2Qz1K9y14qGzBRQqJ7gAyezta3bi7pHY7307sHQxDYVlF6aQlwJDABY16gIYZNnTo121Lt8SXKpsn_rhGgkSqQMGmPnUkieepqyPg9ulkwEAvKBK80dlxzEFvNfaw9_a0SRR2aKIgWhuvAouPHlN9FMzLexlNApbKTTHNQ7dvR\",\"pR\":\"B4YMumm5bLYnT6iYhrQJT6fcMyCOtqQJOMsNxyhncpLCYIkhEVumA4tbB5HitapQU7nVXP_ZrS51VacXbkGU6LZ_BHy3je_iZoMY=5FjLNppoOWitzzkToFkL=bhlIsnMHQhJizXSNAGpRLPTvkI7bhGb=FxousxxCx0=RGYH3\",\"vSA\":\"6rQltQyKPrzIAPaZnCBp0x2F0N2AC4g5JprF_R_Qycs\",\"vSB\":\"3x73szdurz40HjfaYTBF42ecyL9mN56wFTZDGF8a3FN\",\"sSId\":\"electionmanager\",\"sT\":\"2013-03-25T10:58:11.000Z\",\"sV\":\"ShZKrcEYQnsA2Ks352oLIUABAaglxmNsCaYIfKzM_tfRARyYPgNSxNLtNcaQf10VqEq57DArGrhvjLjJwmU5mfip3FRUqZ1hGNFxkqdYM8bLC0uhoiALyHVFkLu4etQG4Dp8RuJ5dmp5UTs0QcEHEIIQcd8_=J7iIeqRLAlcU6\"}";
        assertTrue(0 == correctDecode.compareTo(resultStr));
    }

    /**
     * test that the QR code can be read from a path on the computer
     */
    @Test
    public void bracketsRemovedFromString() throws IOException {
        Messenger msgr = new Messenger();
        QRCode qr = new QRCode(msgr);
        File file = new File(MainGUI.class
                .getResource("/qrcodeGiu").getPath());
        String str = qr.decode(file);
        String expected = "\"eID\":\"vsbfh-2013\",\"encVa\":\"72w86v8UbkIKBaoN6sPQPSP1a7dQf2ivaiU91L5Orindc4XqfVVznFRegKiK2G3P3tYqjijaLdBQjZEJSU0lXVkDDCbl_fGOt=DxlXNNGgdUn_IfhSefshyNHFzYnOHDPlFeZwOmO91zFt3woxWjSwMI0UhbnezFgvrpXp5fuYY\",\"encVb\":\"A38NYQfzQSA=73rLcStNKQCatMgXJyJ0sMa9wjPLkz92oGoQERk4ItanwOEXyyKEIfpu2nwqRhuGKpn9GU39h2_myjliO04Egz1DiS1ZaO7RrjU7fKSMlXU1hQbHOHvyDAZXX9EqY1gBVpT=u=eMVCf6TI0OFf8P86pDni=MB8Y\",\"vk\":\"4lfTbadkitS1NOXSiDhLKrTUh1J=gdOupOzFHgexC4LdYRuV9PIzblQgHfLYNtlEW8i6Tjr1FVv6HJZ902PHeyG8vrkTCJJEODOdZJ7TFzo8WCIywtyqXcFF52n_sg70FNRSLZcEBY6lCcRWWzBJxdGnbBLpl11QGcJ25cG6RKP\",\"pC\":\"2Qz1K9y14qGzBRQqJ7gAyezta3bi7pHY7307sHQxDYVlF6aQlwJDABY16gIYZNnTo121Lt8SXKpsn_rhGgkSqQMGmPnUkieepqyPg9ulkwEAvKBK80dlxzEFvNfaw9_a0SRR2aKIgWhuvAouPHlN9FMzLexlNApbKTTHNQ7dvR\",\"pR\":\"B4YMumm5bLYnT6iYhrQJT6fcMyCOtqQJOMsNxyhncpLCYIkhEVumA4tbB5HitapQU7nVXP_ZrS51VacXbkGU6LZ_BHy3je_iZoMY=5FjLNppoOWitzzkToFkL=bhlIsnMHQhJizXSNAGpRLPTvkI7bhGb=FxousxxCx0=RGYH3\",\"vSA\":\"6rQltQyKPrzIAPaZnCBp0x2F0N2AC4g5JprF_R_Qycs\",\"vSB\":\"3x73szdurz40HjfaYTBF42ecyL9mN56wFTZDGF8a3FN\",\"sSId\":\"electionmanager\",\"sT\":\"2013-03-25T10:58:11.000Z\",\"sV\":\"ShZKrcEYQnsA2Ks352oLIUABAaglxmNsCaYIfKzM_tfRARyYPgNSxNLtNcaQf10VqEq57DArGrhvjLjJwmU5mfip3FRUqZ1hGNFxkqdYM8bLC0uhoiALyHVFkLu4etQG4Dp8RuJ5dmp5UTs0QcEHEIIQcd8_=J7iIeqRLAlcU6\"";
        String cleaned = qr.removeBrackets(str);
        assertTrue(0 == cleaned.compareTo(expected));
    }

    @Test
    public void splitAtCommas() throws IOException {
        Messenger msgr = new Messenger();
        QRCode qr = new QRCode(msgr);
        File file = new File(MainGUI.class
                .getResource("/qrcodeGiu").getPath());
        String correctDecode = "{\"eID\":\"vsbfh-2013\",\"encVa\":\"72w86v8UbkIKBaoN6sPQPSP1a7dQf2ivaiU91L5Orindc4XqfVVznFRegKiK2G3P3tYqjijaLdBQjZEJSU0lXVkDDCbl_fGOt=DxlXNNGgdUn_IfhSefshyNHFzYnOHDPlFeZwOmO91zFt3woxWjSwMI0UhbnezFgvrpXp5fuYY\",\"encVb\":\"A38NYQfzQSA=73rLcStNKQCatMgXJyJ0sMa9wjPLkz92oGoQERk4ItanwOEXyyKEIfpu2nwqRhuGKpn9GU39h2_myjliO04Egz1DiS1ZaO7RrjU7fKSMlXU1hQbHOHvyDAZXX9EqY1gBVpT=u=eMVCf6TI0OFf8P86pDni=MB8Y\",\"vk\":\"4lfTbadkitS1NOXSiDhLKrTUh1J=gdOupOzFHgexC4LdYRuV9PIzblQgHfLYNtlEW8i6Tjr1FVv6HJZ902PHeyG8vrkTCJJEODOdZJ7TFzo8WCIywtyqXcFF52n_sg70FNRSLZcEBY6lCcRWWzBJxdGnbBLpl11QGcJ25cG6RKP\",\"pC\":\"2Qz1K9y14qGzBRQqJ7gAyezta3bi7pHY7307sHQxDYVlF6aQlwJDABY16gIYZNnTo121Lt8SXKpsn_rhGgkSqQMGmPnUkieepqyPg9ulkwEAvKBK80dlxzEFvNfaw9_a0SRR2aKIgWhuvAouPHlN9FMzLexlNApbKTTHNQ7dvR\",\"pR\":\"B4YMumm5bLYnT6iYhrQJT6fcMyCOtqQJOMsNxyhncpLCYIkhEVumA4tbB5HitapQU7nVXP_ZrS51VacXbkGU6LZ_BHy3je_iZoMY=5FjLNppoOWitzzkToFkL=bhlIsnMHQhJizXSNAGpRLPTvkI7bhGb=FxousxxCx0=RGYH3\",\"vSA\":\"6rQltQyKPrzIAPaZnCBp0x2F0N2AC4g5JprF_R_Qycs\",\"vSB\":\"3x73szdurz40HjfaYTBF42ecyL9mN56wFTZDGF8a3FN\",\"sSId\":\"electionmanager\",\"sT\":\"2013-03-25T10:58:11.000Z\",\"sV\":\"ShZKrcEYQnsA2Ks352oLIUABAaglxmNsCaYIfKzM_tfRARyYPgNSxNLtNcaQf10VqEq57DArGrhvjLjJwmU5mfip3FRUqZ1hGNFxkqdYM8bLC0uhoiALyHVFkLu4etQG4Dp8RuJ5dmp5UTs0QcEHEIIQcd8_=J7iIeqRLAlcU6\"}";
        String[] grouped = qr.groupReceiptData(correctDecode);

        String expected1 = "{\"eID\":\"vsbfh-2013\"";
        String expected2 = "\"encVa\":\"72w86v8UbkIKBaoN6sPQPSP1a7dQf2ivaiU91L5Orindc4XqfVVznFRegKiK2G3P3tYqjijaLdBQjZEJSU0lXVkDDCbl_fGOt=DxlXNNGgdUn_IfhSefshyNHFzYnOHDPlFeZwOmO91zFt3woxWjSwMI0UhbnezFgvrpXp5fuYY\"";
        String expected3 = "\"encVb\":\"A38NYQfzQSA=73rLcStNKQCatMgXJyJ0sMa9wjPLkz92oGoQERk4ItanwOEXyyKEIfpu2nwqRhuGKpn9GU39h2_myjliO04Egz1DiS1ZaO7RrjU7fKSMlXU1hQbHOHvyDAZXX9EqY1gBVpT=u=eMVCf6TI0OFf8P86pDni=MB8Y\"";
        String expected4 = "\"vk\":\"4lfTbadkitS1NOXSiDhLKrTUh1J=gdOupOzFHgexC4LdYRuV9PIzblQgHfLYNtlEW8i6Tjr1FVv6HJZ902PHeyG8vrkTCJJEODOdZJ7TFzo8WCIywtyqXcFF52n_sg70FNRSLZcEBY6lCcRWWzBJxdGnbBLpl11QGcJ25cG6RKP\"";
        String expected5 = "\"pC\":\"2Qz1K9y14qGzBRQqJ7gAyezta3bi7pHY7307sHQxDYVlF6aQlwJDABY16gIYZNnTo121Lt8SXKpsn_rhGgkSqQMGmPnUkieepqyPg9ulkwEAvKBK80dlxzEFvNfaw9_a0SRR2aKIgWhuvAouPHlN9FMzLexlNApbKTTHNQ7dvR\"";
        String expected6 = "\"pR\":\"B4YMumm5bLYnT6iYhrQJT6fcMyCOtqQJOMsNxyhncpLCYIkhEVumA4tbB5HitapQU7nVXP_ZrS51VacXbkGU6LZ_BHy3je_iZoMY=5FjLNppoOWitzzkToFkL=bhlIsnMHQhJizXSNAGpRLPTvkI7bhGb=FxousxxCx0=RGYH3\"";
        String expected7 = "\"vSA\":\"6rQltQyKPrzIAPaZnCBp0x2F0N2AC4g5JprF_R_Qycs\"";
        String expected8 = "\"vSB\":\"3x73szdurz40HjfaYTBF42ecyL9mN56wFTZDGF8a3FN\"";
        String expected9 = "\"sSId\":\"electionmanager\"";
        String expected10 = "\"sT\":\"2013-03-25T10:58:11.000Z\"";
        String expected11 = "\"sV\":\"ShZKrcEYQnsA2Ks352oLIUABAaglxmNsCaYIfKzM_tfRARyYPgNSxNLtNcaQf10VqEq57DArGrhvjLjJwmU5mfip3FRUqZ1hGNFxkqdYM8bLC0uhoiALyHVFkLu4etQG4Dp8RuJ5dmp5UTs0QcEHEIIQcd8_=J7iIeqRLAlcU6\"}";
        assertTrue(0 == grouped[0].compareTo(expected1));
        assertTrue(0 == grouped[1].compareTo(expected2));
        assertTrue(0 == grouped[2].compareTo(expected3));
        assertTrue(0 == grouped[3].compareTo(expected4));
        assertTrue(0 == grouped[4].compareTo(expected5));
        assertTrue(0 == grouped[5].compareTo(expected6));
        assertTrue(0 == grouped[6].compareTo(expected7));
        assertTrue(0 == grouped[7].compareTo(expected8));
        assertTrue(0 == grouped[8].compareTo(expected9));
        assertTrue(0 == grouped[9].compareTo(expected10));
        assertTrue(0 == grouped[10].compareTo(expected11));

    }

    @Test
    public void dataGroupedAndCleaned() throws IOException {
        Messenger msgr = new Messenger();
        QRCode qr = new QRCode(msgr);
        File file = new File(MainGUI.class
                .getResource("/qrcodeGiu").getPath());
        String decoded = qr.decode(file);
        String[] groupedCleaned = qr.groupAndCleanDecode(decoded);
        String[] expected = new String[11];

        expected[0] = "eID:vsbfh-2013";
        expected[1] = "encVa:72w86v8UbkIKBaoN6sPQPSP1a7dQf2ivaiU91L5Orindc4XqfVVznFRegKiK2G3P3tYqjijaLdBQjZEJSU0lXVkDDCbl_fGOt=DxlXNNGgdUn_IfhSefshyNHFzYnOHDPlFeZwOmO91zFt3woxWjSwMI0UhbnezFgvrpXp5fuYY";
        expected[2] = "encVb:A38NYQfzQSA=73rLcStNKQCatMgXJyJ0sMa9wjPLkz92oGoQERk4ItanwOEXyyKEIfpu2nwqRhuGKpn9GU39h2_myjliO04Egz1DiS1ZaO7RrjU7fKSMlXU1hQbHOHvyDAZXX9EqY1gBVpT=u=eMVCf6TI0OFf8P86pDni=MB8Y";
        expected[3] = "vk:4lfTbadkitS1NOXSiDhLKrTUh1J=gdOupOzFHgexC4LdYRuV9PIzblQgHfLYNtlEW8i6Tjr1FVv6HJZ902PHeyG8vrkTCJJEODOdZJ7TFzo8WCIywtyqXcFF52n_sg70FNRSLZcEBY6lCcRWWzBJxdGnbBLpl11QGcJ25cG6RKP";
        expected[4] = "pC:2Qz1K9y14qGzBRQqJ7gAyezta3bi7pHY7307sHQxDYVlF6aQlwJDABY16gIYZNnTo121Lt8SXKpsn_rhGgkSqQMGmPnUkieepqyPg9ulkwEAvKBK80dlxzEFvNfaw9_a0SRR2aKIgWhuvAouPHlN9FMzLexlNApbKTTHNQ7dvR";
        expected[5] = "pR:B4YMumm5bLYnT6iYhrQJT6fcMyCOtqQJOMsNxyhncpLCYIkhEVumA4tbB5HitapQU7nVXP_ZrS51VacXbkGU6LZ_BHy3je_iZoMY=5FjLNppoOWitzzkToFkL=bhlIsnMHQhJizXSNAGpRLPTvkI7bhGb=FxousxxCx0=RGYH3";
        expected[6] = "vSA:6rQltQyKPrzIAPaZnCBp0x2F0N2AC4g5JprF_R_Qycs";
        expected[7] = "vSB:3x73szdurz40HjfaYTBF42ecyL9mN56wFTZDGF8a3FN";
        expected[8] = "sSId:electionmanager";
        expected[9] = "sT:2013-03-25T10:58:11.000Z";
        expected[10] = "sV:ShZKrcEYQnsA2Ks352oLIUABAaglxmNsCaYIfKzM_tfRARyYPgNSxNLtNcaQf10VqEq57DArGrhvjLjJwmU5mfip3FRUqZ1hGNFxkqdYM8bLC0uhoiALyHVFkLu4etQG4Dp8RuJ5dmp5UTs0QcEHEIIQcd8_=J7iIeqRLAlcU6";

        assertTrue(0 == groupedCleaned[0].compareTo(expected[0]));
        assertTrue(0 == groupedCleaned[1].compareTo(expected[1]));
        assertTrue(0 == groupedCleaned[2].compareTo(expected[2]));
        assertTrue(0 == groupedCleaned[3].compareTo(expected[3]));
        assertTrue(0 == groupedCleaned[4].compareTo(expected[4]));
        assertTrue(0 == groupedCleaned[5].compareTo(expected[5]));
        assertTrue(0 == groupedCleaned[6].compareTo(expected[6]));
        assertTrue(0 == groupedCleaned[7].compareTo(expected[7]));
        assertTrue(0 == groupedCleaned[8].compareTo(expected[8]));
        assertTrue(0 == groupedCleaned[9].compareTo(expected[9]));
        assertTrue(0 == groupedCleaned[10].compareTo(expected[10]));
    }

    @Test
    public void removeSlashAndQuotes() throws IOException {
        Messenger msgr = new Messenger();
        QRCode qr = new QRCode(msgr);
        File file = new File(MainGUI.class
                .getResource("/qrcodeGiu").getPath());
        String str = qr.decode(file);
        String expected = "{eID:vsbfh-2013,encVa:72w86v8UbkIKBaoN6sPQPSP1a7dQf2ivaiU91L5Orindc4XqfVVznFRegKiK2G3P3tYqjijaLdBQjZEJSU0lXVkDDCbl_fGOt=DxlXNNGgdUn_IfhSefshyNHFzYnOHDPlFeZwOmO91zFt3woxWjSwMI0UhbnezFgvrpXp5fuYY,encVb:A38NYQfzQSA=73rLcStNKQCatMgXJyJ0sMa9wjPLkz92oGoQERk4ItanwOEXyyKEIfpu2nwqRhuGKpn9GU39h2_myjliO04Egz1DiS1ZaO7RrjU7fKSMlXU1hQbHOHvyDAZXX9EqY1gBVpT=u=eMVCf6TI0OFf8P86pDni=MB8Y,vk:4lfTbadkitS1NOXSiDhLKrTUh1J=gdOupOzFHgexC4LdYRuV9PIzblQgHfLYNtlEW8i6Tjr1FVv6HJZ902PHeyG8vrkTCJJEODOdZJ7TFzo8WCIywtyqXcFF52n_sg70FNRSLZcEBY6lCcRWWzBJxdGnbBLpl11QGcJ25cG6RKP,pC:2Qz1K9y14qGzBRQqJ7gAyezta3bi7pHY7307sHQxDYVlF6aQlwJDABY16gIYZNnTo121Lt8SXKpsn_rhGgkSqQMGmPnUkieepqyPg9ulkwEAvKBK80dlxzEFvNfaw9_a0SRR2aKIgWhuvAouPHlN9FMzLexlNApbKTTHNQ7dvR,pR:B4YMumm5bLYnT6iYhrQJT6fcMyCOtqQJOMsNxyhncpLCYIkhEVumA4tbB5HitapQU7nVXP_ZrS51VacXbkGU6LZ_BHy3je_iZoMY=5FjLNppoOWitzzkToFkL=bhlIsnMHQhJizXSNAGpRLPTvkI7bhGb=FxousxxCx0=RGYH3,vSA:6rQltQyKPrzIAPaZnCBp0x2F0N2AC4g5JprF_R_Qycs,vSB:3x73szdurz40HjfaYTBF42ecyL9mN56wFTZDGF8a3FN,sSId:electionmanager,sT:2013-03-25T10:58:11.000Z,sV:ShZKrcEYQnsA2Ks352oLIUABAaglxmNsCaYIfKzM_tfRARyYPgNSxNLtNcaQf10VqEq57DArGrhvjLjJwmU5mfip3FRUqZ1hGNFxkqdYM8bLC0uhoiALyHVFkLu4etQG4Dp8RuJ5dmp5UTs0QcEHEIIQcd8_=J7iIeqRLAlcU6}";
        String cleaned = qr.removeSlashAndQuotes(str);
        assertTrue(0 == cleaned.compareTo(expected));
    }

    /**
     * test that the data pairs are separated appropriately by the dividing
     * semicolon
     *
     * @throws IOException
     */
    @Test
    public void separateDataPairs() throws IOException {
        Messenger msgr = new Messenger();
        QRCode qr = new QRCode(msgr);
        File file = new File(MainGUI.class
                .getResource("/qrcodeGiu").getPath());
        String decoded = qr.decode(file);
        String[] groupedCleaned = qr.groupAndCleanDecode(decoded);
        String[][] results = qr.separateDataPairs(groupedCleaned);

        assertTrue(0 == results[0][0].compareTo("eID"));
        assertTrue(0 == results[0][1].compareTo("vsbfh-2013"));
        assertTrue(0 == results[1][0].compareTo("encVa"));
        assertTrue(0 == results[1][1].compareTo("72w86v8UbkIKBaoN6sPQPSP1a7dQf2ivaiU91L5Orindc4XqfVVznFRegKiK2G3P3tYqjijaLdBQjZEJSU0lXVkDDCbl_fGOt=DxlXNNGgdUn_IfhSefshyNHFzYnOHDPlFeZwOmO91zFt3woxWjSwMI0UhbnezFgvrpXp5fuYY"));
        assertTrue(0 == results[2][0].compareTo("encVb"));
        assertTrue(0 == results[2][1].compareTo("A38NYQfzQSA=73rLcStNKQCatMgXJyJ0sMa9wjPLkz92oGoQERk4ItanwOEXyyKEIfpu2nwqRhuGKpn9GU39h2_myjliO04Egz1DiS1ZaO7RrjU7fKSMlXU1hQbHOHvyDAZXX9EqY1gBVpT=u=eMVCf6TI0OFf8P86pDni=MB8Y"));
        assertTrue(0 == results[3][0].compareTo("vk"));
        assertTrue(0 == results[3][1].compareTo("4lfTbadkitS1NOXSiDhLKrTUh1J=gdOupOzFHgexC4LdYRuV9PIzblQgHfLYNtlEW8i6Tjr1FVv6HJZ902PHeyG8vrkTCJJEODOdZJ7TFzo8WCIywtyqXcFF52n_sg70FNRSLZcEBY6lCcRWWzBJxdGnbBLpl11QGcJ25cG6RKP"));
        assertTrue(0 == results[4][0].compareTo("pC"));
        assertTrue(0 == results[4][1].compareTo("2Qz1K9y14qGzBRQqJ7gAyezta3bi7pHY7307sHQxDYVlF6aQlwJDABY16gIYZNnTo121Lt8SXKpsn_rhGgkSqQMGmPnUkieepqyPg9ulkwEAvKBK80dlxzEFvNfaw9_a0SRR2aKIgWhuvAouPHlN9FMzLexlNApbKTTHNQ7dvR"));
        assertTrue(0 == results[5][0].compareTo("pR"));
        assertTrue(0 == results[5][1].compareTo("B4YMumm5bLYnT6iYhrQJT6fcMyCOtqQJOMsNxyhncpLCYIkhEVumA4tbB5HitapQU7nVXP_ZrS51VacXbkGU6LZ_BHy3je_iZoMY=5FjLNppoOWitzzkToFkL=bhlIsnMHQhJizXSNAGpRLPTvkI7bhGb=FxousxxCx0=RGYH3"));
        assertTrue(0 == results[6][0].compareTo("vSA"));
        assertTrue(0 == results[6][1].compareTo("6rQltQyKPrzIAPaZnCBp0x2F0N2AC4g5JprF_R_Qycs"));
        assertTrue(0 == results[7][0].compareTo("vSB"));
        assertTrue(0 == results[7][1].compareTo("3x73szdurz40HjfaYTBF42ecyL9mN56wFTZDGF8a3FN"));
        assertTrue(0 == results[8][0].compareTo("sSId"));
        assertTrue(0 == results[8][1].compareTo("electionmanager"));
        assertTrue(0 == results[9][0].compareTo("sT"));
        assertTrue(0 == results[9][1].compareTo("2013-03-25T10:58:11.000Z"));
        assertTrue(0 == results[10][0].compareTo("sV"));
        assertTrue(0 == results[10][1].compareTo("ShZKrcEYQnsA2Ks352oLIUABAaglxmNsCaYIfKzM_tfRARyYPgNSxNLtNcaQf10VqEq57DArGrhvjLjJwmU5mfip3FRUqZ1hGNFxkqdYM8bLC0uhoiALyHVFkLu4etQG4Dp8RuJ5dmp5UTs0QcEHEIIQcd8_=J7iIeqRLAlcU6"));
    }

    /**
     * Test that the election receipt object is instantiated with proper values
     *
     * @throws IOException
     */
    @Test
    public void electionReceiptHasProperValues() throws IOException {
        Messenger msgr = new Messenger();
        QRCode qr = new QRCode(msgr);
        File file = new File(QRCodeTest.class
                .getResource("/qrcodeGiu").getPath());
        ElectionReceipt ercp = qr.decodeReceipt(file);

        BigInteger actual, expected;
        String str = ercp.getElectionID();
        assertTrue(0 == str.compareTo("vsbfh-2013"));

        actual = ercp.getEncValueA();
        expected = CryptoFunc.decodeSpecialBase64("72w86v8UbkIKBaoN6sPQPSP1a7dQf2ivaiU91L5Orindc4XqfVVznFRegKiK2G3P3tYqjijaLdBQjZEJSU0lXVkDDCbl_fGOt=DxlXNNGgdUn_IfhSefshyNHFzYnOHDPlFeZwOmO91zFt3woxWjSwMI0UhbnezFgvrpXp5fuYY");
        assertTrue(0 == expected.compareTo(actual));

        actual = ercp.getEncValueB();
        expected = CryptoFunc.decodeSpecialBase64("A38NYQfzQSA=73rLcStNKQCatMgXJyJ0sMa9wjPLkz92oGoQERk4ItanwOEXyyKEIfpu2nwqRhuGKpn9GU39h2_myjliO04Egz1DiS1ZaO7RrjU7fKSMlXU1hQbHOHvyDAZXX9EqY1gBVpT=u=eMVCf6TI0OFf8P86pDni=MB8Y");
        assertTrue(0 == expected.compareTo(actual));

        actual = ercp.getVerificationKey();

        expected = CryptoFunc.decodeSpecialBase64("4lfTbadkitS1NOXSiDhLKrTUh1J=gdOupOzFHgexC4LdYRuV9PIzblQgHfLYNtlEW8i6Tjr1FVv6HJZ902PHeyG8vrkTCJJEODOdZJ7TFzo8WCIywtyqXcFF52n_sg70FNRSLZcEBY6lCcRWWzBJxdGnbBLpl11QGcJ25cG6RKP");
        assertTrue(0 == expected.compareTo(actual));

        actual = ercp.getProofCommitment();
        expected = CryptoFunc.decodeSpecialBase64("2Qz1K9y14qGzBRQqJ7gAyezta3bi7pHY7307sHQxDYVlF6aQlwJDABY16gIYZNnTo121Lt8SXKpsn_rhGgkSqQMGmPnUkieepqyPg9ulkwEAvKBK80dlxzEFvNfaw9_a0SRR2aKIgWhuvAouPHlN9FMzLexlNApbKTTHNQ7dvR");
        assertTrue(0 == expected.compareTo(actual));


        actual = ercp.getProofResponse();
        expected = CryptoFunc.decodeSpecialBase64("B4YMumm5bLYnT6iYhrQJT6fcMyCOtqQJOMsNxyhncpLCYIkhEVumA4tbB5HitapQU7nVXP_ZrS51VacXbkGU6LZ_BHy3je_iZoMY=5FjLNppoOWitzzkToFkL=bhlIsnMHQhJizXSNAGpRLPTvkI7bhGb=FxousxxCx0=RGYH3");
        assertTrue(0 == expected.compareTo(actual));

        actual = ercp.getSchnorrValueA();
        expected = CryptoFunc.decodeSpecialBase64("6rQltQyKPrzIAPaZnCBp0x2F0N2AC4g5JprF_R_Qycs");
        assertTrue(0 == expected.compareTo(actual));

        actual = ercp.getSchnorrValueB();
        expected = CryptoFunc.decodeSpecialBase64("3x73szdurz40HjfaYTBF42ecyL9mN56wFTZDGF8a3FN");
        assertTrue(0 == expected.compareTo(actual));

        str = ercp.getSignatureIssuerID();
        assertTrue(0 == str.compareTo("electionmanager"));

        str = ercp.getTimeStamp();
        assertTrue(0 == str.compareTo("2013-03-25T10:58:11.000Z"));

        actual = ercp.getSignatureValue();
        expected = CryptoFunc.decodeSpecialBase64("ShZKrcEYQnsA2Ks352oLIUABAaglxmNsCaYIfKzM_tfRARyYPgNSxNLtNcaQf10VqEq57DArGrhvjLjJwmU5mfip3FRUqZ1hGNFxkqdYM8bLC0uhoiALyHVFkLu4etQG4Dp8RuJ5dmp5UTs0QcEHEIIQcd8_=J7iIeqRLAlcU6");
        assertTrue(0 == expected.compareTo(actual));
    }
}
