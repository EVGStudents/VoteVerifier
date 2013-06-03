/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.filehandling;

import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Report;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.gui.ResultProcessor;
import ch.bfh.univoteverifier.table.ResultSet;
import ch.bfh.univoteverifier.table.ResultTabbedPane;
import ch.bfh.univoteverifier.verification.VerificationResult;
import javax.swing.ImageIcon;
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
public class ResultProcessorTest {
//    @Test
//    public void warnImageShows() {
//        Exception ex = new Exception();
//        Report report = new Report(ex);
//        String processID = "VSBFH1010101010";
//        ResultProcessor rp = new ResultProcessor("TESTS");
//        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT, true, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
//        vr.setReport(report);
//        ImageIcon img = rp.getPassImage();
//        ResultSet rs = new ResultSet("textForVrfResult", img, vr, processID);
//        ImageIcon imgGot = rp.getImage(vr);
//        assertTrue(img == imgGot);
//    }
}
