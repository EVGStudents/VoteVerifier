/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

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
        fail = new ImageIcon(VoteVerifier.class.getResource("/fail.png"));
        noImpl = new ImageIcon(VoteVerifier.class.getResource("/noImpl.png"));
        warn = new ImageIcon(VoteVerifier.class.getResource("/warning.png"));
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
            LOGGER.log(Level.OFF, "ELECTION RESULTS RECEIVED BY PROCESSOR: ProcessID: " + ve.getProcessID());
            resultPanelManager.addElectionResults(crs);
        } else {
            VerificationResult vr = ve.getVr();
            Boolean result = vr.getResult();
            int code = vr.getVerificationType().getID();
            ResultDescriber rd = new ResultDescriber();
            String vrfType = rd.getTextFromVrfCode(code);
            ImageIcon img = getImage(vr);
            LOGGER.log(Level.OFF, "ShowResultsINGui: VerificationEVent contains TabID: " + ve.getProcessID());
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
        if (!vr.isImplemented()) {
            img = noImpl;
        } else if (vr.getResult()) {
            img = pass;
        } else {
            if (vr.getReport().getFailureCode() != null) {
                img = fail;
            } else if (vr.getReport().getException() != null) {
                img = warn;
            }
        }
        return img;
    }
}
