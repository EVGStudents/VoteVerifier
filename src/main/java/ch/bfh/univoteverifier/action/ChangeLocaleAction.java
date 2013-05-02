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
import ch.bfh.univoteverifier.gui.MainGUI;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenuItem;

/**
 *
 * @author prinstin
 */
public class ChangeLocaleAction extends AbstractAction {

    ConsolePanel consolePanel;

    public ChangeLocaleAction(ConsolePanel consolePanel) {
        this.consolePanel = consolePanel;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = ((JMenuItem)e.getSource()).getName();
        String text = "";
        if (0 == name.compareTo("btnEn")) {
            text = "locale changed to english";
        } else if (0 == name.compareTo("btnFr")) {
            text = "locale changed to french";
        } else if (0 == name.compareTo("btnDe")) {
            text = "locale changed to german";
        }
        consolePanel.appendToStatusText(text);
    }
}
