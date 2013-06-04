/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.filehandling;

import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Report;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.gui.ResultProcessor;
import ch.bfh.univoteverifier.table.ResultSet;
import ch.bfh.univoteverifier.table.ResultTabbedPane;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.logging.Logger;

/**
 *
 * @author prinstin
 */
public class ResultProcessorTest {

    private static final Logger LOGGER = Logger.getLogger(ResultProcessorTest.class.getName());

    @Test
    public void warnWithExceptionYesImpl() {
        //Situation: Exception Present and implemented       Expected: warn image
        Exception ex = new Exception();
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = true;
        Report report = new Report(ex);
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT, vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(true);
        vr.setReport(report);

        ImageIcon img = rp.getWarnImage();
        ImageIcon imgGot = rp.getImage(vr);

        ResultSet rs = new ResultSet("textForVrfResult", img, vr, processID);

//        LOGGER.log(Level.OFF, "AlgorithmGottenImage:{0}", imgGot.getDescription());
//        LOGGER.log(Level.OFF, "Expected:{0}", img.getDescription());
        assertTrue(img == imgGot);
    }

    @Test
    public void warnWithExceptionNoImpl() {
        //Situation: Exception Present but not implemented       Expected: warn image
        Exception ex = new Exception();
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = true;
        Report report = new Report(ex);
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT, vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(false);
        vr.setReport(report);

        ImageIcon img = rp.getWarnImage();
        ImageIcon imgGot = rp.getImage(vr);

        ResultSet rs = new ResultSet("textForVrfResult", img, vr, processID);

//        LOGGER.log(Level.OFF, "AlgorithmGottenImage:{0}", imgGot.getDescription());
//        LOGGER.log(Level.OFF, "Expected:{0}", img.getDescription());
        assertTrue(img == imgGot);
    }

    @Test
    public void failWithFalseYesImpl() {
        //Situation: VrfResult is true but not implemented       Expected: fail image
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = false;
        Report report = new Report(FailureCode.BALLOT_NOT_IN_SET);
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT, vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(true);
        vr.setReport(report);

        ImageIcon imgExpected = rp.getFailImage();
        ImageIcon imgGot = rp.getImage(vr);

        ResultSet rs = new ResultSet("textForVrfResult", imgExpected, vr, processID);

//        LOGGER.log(Level.OFF, "AlgorithmGottenImage:{0}", imgGot.getDescription());
//        LOGGER.log(Level.OFF, "Expected:{0}", img.getDescription());
        assertTrue(imgExpected == imgGot);
    }

    @Test
    public void noImplWithFalseNoImpl() {
        //Situation: VrfResult is false but not implemented      Expected: nonImplemented image
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = false;
        Report report = new Report(FailureCode.BALLOT_NOT_IN_SET);
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT, vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(false);
        vr.setReport(report);


        ImageIcon imgExpected = rp.getImplImage();
        ImageIcon imgGot = rp.getImage(vr);

        ResultSet rs = new ResultSet("textForVrfResult", imgExpected, vr, processID);

//        LOGGER.log(Level.OFF, "AlgorithmGottenImage:{0}", imgGot.getDescription());
//        LOGGER.log(Level.OFF, "Expected:{0}", img.getDescription());
        assertTrue(imgExpected == imgGot);
    }

    @Test
    public void noImplWithTrueNoImpl() {
        //Situation: VrfResult is true but not implemented       Expected: nonImplemented image
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = true;
        Report report = new Report(FailureCode.BALLOT_NOT_IN_SET);
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT, vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(false);
        vr.setReport(report);


        ImageIcon imgExpected = rp.getImplImage();
        ImageIcon imgGot = rp.getImage(vr);

        ResultSet rs = new ResultSet("textForVrfResult", imgExpected, vr, processID);

//        LOGGER.log(Level.OFF, "AlgorithmGottenImage:{0}", imgGot.getDescription());
//        LOGGER.log(Level.OFF, "Expected:{0}", img.getDescription());
        assertTrue(imgExpected == imgGot);
    }

    @Test
    public void passWithTrueYesImplemented() {
        //Situation: VrfResult is true but not implemented       Expected: nonImplemented image
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = true;
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT, vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(true);


        ImageIcon imgExpected = rp.getPassImage();
        ImageIcon imgGot = rp.getImage(vr);

        ResultSet rs = new ResultSet("textForVrfResult", imgExpected, vr, processID);

//        LOGGER.log(Level.OFF, "AlgorithmGottenImage:{0}", imgGot.getDescription());
//        LOGGER.log(Level.OFF, "Expected:{0}", img.getDescription());
        assertTrue(imgExpected == imgGot);
    }
}
