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
 * An Action that manages the action of clicking on the individual verification
 * button.
 *
 * @author prinstin
 */
public class SelectIndVrfAction extends AbstractAction {

    ResourceBundle rb;
    TopPanel topPanel;

    /**
     * Create an instance of this Action class.
     *
     * @param topPanel the top panel of the GUI which is used to access
     * references to objects there.
     */
    public SelectIndVrfAction(TopPanel topPanel) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        this.putValue(NAME, rb.getString("btnInd"));
        this.topPanel = topPanel;

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
        topPanel.indVrfSelected();

    }
}
