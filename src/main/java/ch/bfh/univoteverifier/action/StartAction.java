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
package ch.bfh.univoteverifier.action;

import ch.bfh.univoteverifier.common.IFileManager;
import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.gui.ThreadManager;
import ch.bfh.univoteverifier.verification.IndividualVerification;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.verification.VerificationThread;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import static javax.swing.Action.NAME;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * An Action Class that manages the action of clicking on the start button.
 *
 * @author prinstin
 */
public class StartAction extends AbstractAction {

    Messenger msgr;
    JPanel innerPanel;
    ResourceBundle rb;
    JComboBox comboBox;
    ButtonGroup btnGrp;
    IFileManager fm;
    ElectionReceipt er;
    VerificationThread vt;
    ThreadManager tm;
    private static final Logger LOGGER = Logger.getLogger(StartAction.class.getName());

    /**
     * Create an instance of this Action class.
     *
     * @param msgr Messenger object used to send messages
     * @param innerPanel
     * @param comboBox
     * @param btnGroup
     * @param qrCodeFile
     */
    public StartAction(Messenger msgr, JPanel innerPanel, JComboBox comboBox, ButtonGroup btnGroup, IFileManager fm, ThreadManager tm) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        this.innerPanel = innerPanel;
        this.msgr = msgr;
        this.btnGrp = btnGroup;
        this.comboBox = comboBox;
        this.fm = fm;
        putValue(NAME, rb.getString("start"));
        this.tm = tm;
    }

    /**
     * This method is called when an action is performed on a component to which
     * this Action has been registered
     *
     * @param e ActionEvent generated by the component on which an action was
     * performed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        startVerification();
    }

    public void startVerification() {
        String btnTxt = getSelectedButtonText(btnGrp);
        LOGGER.log(Level.INFO, "BTN TEXT RETURNED:" + btnTxt);
        String msg = "";
        if (0 == btnTxt.compareTo("btnUni")) {
            String eID = comboBox.getSelectedItem().toString();
            msg = rb.getString("beginningVrfFor") + " " + rb.getString("forElectionId") + " " + eID;
            msgr.sendSetupError(msg);

            vt = new VerificationThread(msgr, eID);
            vt.setName(eID);
            tm.addThread(vt);
            vt.start();
        } else {
            if (fm.getFile() != null) {

                if (fileProvidedIsValid()) {
                    msg += rb.getString("ballotProvided");

                    vt = new VerificationThread(msgr, er);
                    vt.setName(er.geteID());
                    vt.start();
                    tm.addThread(vt);
                }
            } else {
                msgr.sendSetupError(rb.getString("pleaseSelectFile"));
            }
        }

    }

    /**
     * Check that we have a valid QR Code before proceeding with verification
     * steps.
     *
     * @return True if a QR Code has been detected.
     */
    public boolean fileProvidedIsValid() {
        er = getElectionReceipt(fm.getFile(), msgr);
        return (er != null);
    }

    /**
     * Get the name ID of the verification button that is selected.
     *
     * @param buttonGroup
     * @return the name ID of the button selected.
     */
    public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getName();
            }
        }
        return null;
    }

    /**
     * Translate a file with the path to a QRCode into an ElectionReceipt object
     *
     * @param qrCodeFile the File to a QRcode
     * @param msgr Messenger which transports error messages, etc.
     * @return the ElectionReceipt helper class containing getter methods for
     * the variables.
     */
    public ElectionReceipt getElectionReceipt(File qrCodeFile, Messenger msgr) {
        QRCode qr = new QRCode(msgr);
        return qr.decodeReceipt(qrCodeFile);
    }
}
