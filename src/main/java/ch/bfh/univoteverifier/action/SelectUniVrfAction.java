/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.action;

import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.gui.MiddlePanel;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;

/**
 * An Action Class that manages the action of clicking on the universal
 * verification button.
 *
 * @author Justin Springer
 */
public class SelectUniVrfAction extends AbstractAction {

    private ResourceBundle rb;
    private MiddlePanel middlePanel;

    /**
     * Create an instance of this Action class.
     *
     * @param panel the top panel of the GUI which is used to access references
     * to objects there.
     */
    public SelectUniVrfAction(MiddlePanel middlePanel) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        this.putValue(NAME, rb.getString("btnUni"));
        this.middlePanel = middlePanel;

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
        middlePanel.uniVrfSelected();
    }
}
