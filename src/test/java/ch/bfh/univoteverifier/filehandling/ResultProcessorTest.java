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
package ch.bfh.univoteverifier.filehandling;

import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.FailureCode;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Report;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.gui.ResultProcessor;
import ch.bfh.univoteverifier.verification.VerificationResult;
import javax.swing.ImageIcon;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Justin Springer
 */
public class ResultProcessorTest {

    @Test
    public void warnWithExceptionYesImpl() {
        //Situation: Exception Present and implemented       Expected: warn image
        Exception ex = new Exception();
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = true;
        Report report = new Report(ex);
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT,
            vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(true);
        vr.setReport(report);

        ImageIcon img = rp.getWarnImage();
        ImageIcon imgGot = rp.getImage(vr);

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
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT,
            vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(false);
        vr.setReport(report);

        ImageIcon img = rp.getWarnImage();
        ImageIcon imgGot = rp.getImage(vr);

        assertTrue(img == imgGot);
    }

    @Test
    public void failWithFalseYesImpl() {
        //Situation: VrfResult is true but not implemented       Expected: fail image
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = false;
        Report report = new Report(FailureCode.BALLOT_NOT_IN_SET);
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT,
            vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(true);
        vr.setReport(report);

        ImageIcon imgExpected = rp.getFailImage();
        ImageIcon imgGot = rp.getImage(vr);

        assertTrue(imgExpected == imgGot);
    }

    @Ignore
    public void noImplWithFalseNoImpl() {
        //Situation: VrfResult is false but not implemented      Expected: fail image
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = false;
        Report report = new Report(FailureCode.BALLOT_NOT_IN_SET);
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT,
            vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(false);
        vr.setReport(report);


        ImageIcon imgExpected = rp.getFailImage();
        ImageIcon imgGot = rp.getImage(vr);

        assertTrue(imgExpected == imgGot);
    }

    @Test
    public void noImplWithTrueNoImpl() {
        //Situation: VrfResult is true but not implemented       Expected: nonImplemented image
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = true;
        Report report = new Report(FailureCode.NOT_YET_IMPLEMENTED);
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT,
            vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(false);
        vr.setReport(report);


        ImageIcon imgExpected = rp.getImplImage();
        ImageIcon imgGot = rp.getImage(vr);

        assertTrue(imgExpected == imgGot);
    }

    @Test
    public void passWithTrueYesImplemented() {
        //Situation: VrfResult is true but not implemented       Expected: nonImplemented image
        String processID = "VSBFH1010101010";
        ResultProcessor rp = new ResultProcessor("TESTS");
        boolean vrfResult = true;
        VerificationResult vr = new VerificationResult(VerificationType.SETUP_EM_CERT,
            vrfResult, processID, RunnerName.UNSET, ImplementerType.RSA, EntityType.CA);
        vr.setImplemented(true);


        ImageIcon imgExpected = rp.getPassImage();
        ImageIcon imgGot = rp.getImage(vr);

        assertTrue(imgExpected == imgGot);
    }
}
