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

import ch.bfh.univoteverifier.gui.ConsolePanel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 *
 * @author prinstin
 */
public class FileChooserAction extends AbstractAction {

    ResourceBundle rb;
    JPanel innerPanel;
    ConsolePanel consolePanel;
    File qrCodeFile;

    public FileChooserAction(JPanel innerPanel, ConsolePanel consolePanel, File qrCodeFile) {
        this.innerPanel = innerPanel;
        this.consolePanel = consolePanel;
        this.qrCodeFile = qrCodeFile;
        this.putValue(NAME, "select");
        rb = ResourceBundle.getBundle("error", Locale.ENGLISH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {



        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showDialog(innerPanel, rb.getString("selectFile"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            qrCodeFile = fc.getSelectedFile();
            if (qrCodeFile == null) {
                consolePanel.appendToStatusText(rb.getString("invalidFile"));
            } else {
                String path = "\n" + qrCodeFile.getPath();
                consolePanel.appendToStatusText(path);

            }
        }
    }
}
