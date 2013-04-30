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
package action;

import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.gui.ConsolePanel;
import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.verification.VerificationSwingWorker;
import ch.bfh.univoteverifier.verification.VerificationThread;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author prinstin
 */
public class StartAction extends AbstractAction {

    Messenger msgr;
    ConsolePanel consolePanel;
    JPanel innerPanel;
    ResourceBundle rb;

    public StartAction(Messenger msgr, ConsolePanel consolePanel, JPanel innerPanel) {
        rb = ResourceBundle.getBundle("error", Locale.ENGLISH);
        this.innerPanel = innerPanel;
        this.consolePanel = consolePanel;
        this.msgr = msgr;
        putValue(NAME, "START");
        putValue(SHORT_DESCRIPTION, "THIS IS A DESCRIPTION");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        innerPanel.removeAll();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        innerPanel.setBackground(GUIconstants.GREY);
        innerPanel.repaint();
//        btnInd.setEnabled(false);
//        btnUni.setEnabled(false);
//        String msg = "starting verification";
//        consolePanel.setStatusText(msg);
//        mc.universalVerification(eID);
//        mc.getStatusSubject().addListener(sl);
        VerificationThread vt = new VerificationThread(msgr, "vsbfh-2013");
        vt.start();
//        VerificationSwingWorker vsw = new VerificationSwingWorker(msgr, "vsbfh-2013");
//        vsw.execute();
    }
}
