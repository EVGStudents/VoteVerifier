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

import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.gui.TopPanel;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;

/**
 *
 * @author prinstin
 */
public class SelectUniVrfAction extends AbstractAction {

    ResourceBundle rb;
    TopPanel topPanel;

    public SelectUniVrfAction(TopPanel topPanel) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        this.putValue(NAME, rb.getString("btnUni"));
        this.topPanel=topPanel;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        topPanel.uniVrfSelected();
    }
}
