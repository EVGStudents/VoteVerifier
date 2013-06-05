/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.common.Report;
import ch.bfh.univoteverifier.listener.VerificationEvent;
import ch.bfh.univoteverifier.listener.VerificationMessage;
import ch.bfh.univoteverifier.table.CandidateResultSet;
import ch.bfh.univoteverifier.table.ResultDescriber;
import ch.bfh.univoteverifier.table.ResultSet;
import ch.bfh.univoteverifier.table.ResultTabbedPane;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 * This class handles the processing of the results that are received after a
 * verification is completed.
 *
 * @author prinstin
 */
public class ResultProcessor {

    private ConsolePanel consolePanel;
    private ResultTabbedPane resultPanelManager;
    private ImageIcon pass, fail, noImpl, warn;
    private static final Logger LOGGER = Logger.getLogger(ResultProcessor.class.toString());

    /**
     * Create a partial object for tests
     */
    public ResultProcessor(String dummyVariable) {
        pass = new ImageIcon(VoteVerifier.class.getResource("/check.png"));
        fail = new ImageIcon(VoteVerifier.class.getResource("/fail.png"));
        noImpl = new ImageIcon(VoteVerifier.class.getResource("/noImpl.png"));
        warn = new ImageIcon(VoteVerifier.class.getResource("/warning.png"));
    }

    /**
     * Create a new instance of this class.
     *
     * @param consolePanel Necessary to be able to print the output text in the
     * in-GUI text area.
     * @param resultPanelManager Manages the various elections that are being
     * verified.
     */
    public ResultProcessor(ConsolePanel consolePanel, ResultTabbedPane resultPanelManager) {
        this.consolePanel = consolePanel;
        this.resultPanelManager = resultPanelManager;

        pass = new ImageIcon(VoteVerifier.class.getResource("/check.png"));
        pass.setDescription("Pass Image");
        fail = new ImageIcon(VoteVerifier.class.getResource("/fail.png"));
        fail.setDescription("Fail Image");
        noImpl = new ImageIcon(VoteVerifier.class.getResource("/noImpl.png"));
        noImpl.setDescription("No Implementation Image");
        warn = new ImageIcon(VoteVerifier.class.getResource("/warning.png"));
        warn.setDescription("Warn Image");
    }

    /**
     * Display the incoming verification result information in the GUI
     *
     * @param ve VerificationEvent helper class containing verification
     * information.
     */
    public void showResultInGUI(VerificationEvent ve) {
        if (ve.getVm() == VerificationMessage.ELECTION_RESULTS) {
            CandidateResultSet crs = new CandidateResultSet(ve.getEID(), ve.getElectionResults(), ve.getProcessID());
            LOGGER.log(Level.INFO, "ELECTION RESULTS RECEIVED BY PROCESSOR: ProcessID: " + ve.getProcessID());
            resultPanelManager.addElectionResults(crs);
        } else if (ve.getVm() == VerificationMessage.RESULT) {
            VerificationResult vr = ve.getVr();
            Boolean result = vr.getResult();
            int code = vr.getVerificationType().getID();
            ResultDescriber rd = new ResultDescriber();
            String vrfType = rd.getTextFromVrfCode(code);
            ImageIcon img = getImage(vr);
            LOGGER.log(Level.INFO, "ShowResultsINGui: VerificationEVent contains TabID: " + ve.getProcessID());
            ResultSet rs = new ResultSet(vrfType, img, vr, ve.getProcessID());
            resultPanelManager.addData(rs);
            String outputText = "\n" + vrfType + " ............. " + result;
            consolePanel.appendToStatusText(outputText, ve.getEID());
        }
    }

    /**
     * Get the appropriate image to be displayed for this result. Can be green
     * check, red x, or an orange question mark.
     *
     * @param vr The result set to find an image for.
     * @return An image.
     */
    public ImageIcon getImage(VerificationResult vr) {

        ImageIcon img = null;

        if (vr.getReport() != null && vr.getReport().getException() != null) {
            //If there is an exception
            img = warn;
        } else if (!vr.isImplemented() && vr.getReport() != null && vr.getReport().getFailureCode() != null) {
            //if not implemented and there is a failure code
            img = noImpl;
        } else if (vr.getResult() && vr.isImplemented()) {
            //if result true and it is implemented
            img = pass;
        } else if (!vr.getResult() && vr.isImplemented()) {
            //if result false and it is implemented
            img = fail;
        }
        return img;
    }

    /**
     * Get the image to test the method getImage.
     *
     * @return warn image.
     */
    public ImageIcon getWarnImage() {
        return warn;
    }

    /**
     * Get the image to test the method getImage.
     *
     * @return fail image.
     */
    public ImageIcon getFailImage() {
        return fail;
    }

    /**
     * Get the image to test the method getImage.
     *
     * @return pass image.
     */
    public ImageIcon getPassImage() {
        return pass;
    }

    /**
     * Get the image to test the method getImage.
     *
     * @return noImpl image.
     */
    public ImageIcon getImplImage() {
        return noImpl;
    }
}
