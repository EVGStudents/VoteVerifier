/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.listener.VerificationEvent;
import ch.bfh.univoteverifier.table.ResultSet;
import ch.bfh.univoteverifier.table.ResultTabbedPane;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author prinstin
 */
public class ResultProcessor {

    ConsolePanel consolePanel;
    ResultTabbedPane resultPanelManager;
    ImageIcon pass, fail, noImpl;

    public ResultProcessor(ConsolePanel consolePanel, ResultTabbedPane resultPanelManager) {
        this.consolePanel = consolePanel;
        this.resultPanelManager = resultPanelManager;

        pass = new ImageIcon(UniVoteVerifierRunner.class.getResource("/check.png"));
        fail = new ImageIcon(UniVoteVerifierRunner.class.getResource("/fail.png"));
        noImpl = new ImageIcon(UniVoteVerifierRunner.class.getResource("/noImpl.png"));
    }

    public void showResult(VerificationEvent ve) {
    }

    /**
     * Display the incoming verification result information in the GUI
     *
     * @param ve VerificationResult helper class containing verification
     * information.
     */
    public void showResultInGUI(VerificationEvent ve) {
        VerificationResult vr = ve.getVr();
        Boolean result = vr.getResult();
        int code = vr.getVerificationType().getID();
        String vrfType = GUIconstants.getTextFromVrfCode(code);
        ImageIcon img = getImage(vr);
        ResultSet rs = new ResultSet(vrfType, img, vr);
        resultPanelManager.addData(rs);
        String outputText = "\n" + vrfType + " ............. " + result;
        consolePanel.appendToStatusText(outputText, ve.getEID());
    }

    public ImageIcon getImage(VerificationResult vr) {

        ImageIcon img = null;
        if (vr.isImplemented()) {
            if (vr.getResult()) {
                img = pass;
            } else {
                img = fail;
            }
        } else {
            img = noImpl;
        }
        return img;
    }
}
