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
import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.gui.MainGUI;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 *
 * @author prinstin
 */
public class FileChooserAction extends AbstractAction {

    ResourceBundle rb;
    JPanel innerPanel;
    File qrCodeFile;
    MainGUI mainGUI;

    public FileChooserAction(JPanel innerPanel, MainGUI mainGUI, File qrCodeFile) {
        this.innerPanel = innerPanel;
        this.mainGUI = mainGUI;
        this.qrCodeFile = qrCodeFile;
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        this.putValue(NAME, rb.getString("selectFile"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {



        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showDialog(innerPanel, rb.getString("selectFile"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            qrCodeFile = fc.getSelectedFile();
            if (qrCodeFile == null) {
                mainGUI.appendToConsole(rb.getString("invalidFile"));
            } else {
                String path = "\n" + qrCodeFile.getPath();
                mainGUI.appendToConsole(path);
            }
        }
    }
}
