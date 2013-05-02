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

import ch.bfh.univoteverifier.common.Messenger;
import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.gui.ElectionReceipt;
import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.gui.MainGUI;
import ch.bfh.univoteverifier.verification.IndividualVerification;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.verification.VerificationThread;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author prinstin
 */
public class StartAction extends AbstractAction {

	Messenger msgr;
	JPanel innerPanel;
	ResourceBundle rb;
	MainGUI mainGUI;
	JComboBox comboBox;
	ButtonGroup btnGrp;
	File qrCodeFile;
	private static final Logger LOGGER = Logger.getLogger(StartAction.class.getName());

	public StartAction(Messenger msgr, MainGUI mainGUI, JPanel innerPanel, JComboBox comboBox, ButtonGroup btnGroup, File qrCodeFile) {
		rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
		this.innerPanel = innerPanel;
		this.msgr = msgr;
		this.btnGrp = btnGroup;
		this.comboBox = comboBox;
		this.mainGUI = mainGUI;
		this.qrCodeFile = qrCodeFile;
		putValue(NAME, rb.getString("start"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		innerPanel.removeAll();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
		innerPanel.setBackground(GUIconstants.GREY);
		innerPanel.repaint();

		ElectionReceipt er = getElectionReceipt(qrCodeFile, msgr);
		String msg = "";
		String eID = comboBox.getSelectedItem().toString();
		msg = msg + rb.getString("forElectionId") + eID;
		mainGUI.appendToConsole(msg);
		VerificationThread vt = new VerificationThread(msgr, eID);
		vt.start();
	}

	/**
	 *
	 * @param buttonGroup
	 * @return
	 */
	public String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			LOGGER.log(Level.INFO, "BTN IN BTN GROUP:{0}", button.getText());
			if (button.isSelected()) {
				return button.getText();
			}
		}

		return null;
	}

	public ElectionReceipt getElectionReceipt(File qrCodeFile, Messenger msgr) {
		QRCode qr = new QRCode(msgr);
		ElectionReceipt er = null;
		try {
			er = qr.decodeReceipt(qrCodeFile);
		} catch (IOException ex) {
			Logger.getLogger(IndividualVerification.class.getName()).log(Level.SEVERE, "An error occured while processing the file", ex);
		}
		return er;

	}
}
