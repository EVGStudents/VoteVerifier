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

import ch.bfh.univoteverifier.gui.ThreadManager;
import ch.bfh.univoteverifier.table.ResultTabbedPane;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTabbedPane;

/**
 * Action to remove a tab from the TabbedPane in the Verification panel (the
 * middle).
 *
 * @author prinstin
 */
public class RemoveTabAction extends AbstractAction {

    private JTabbedPane jtp;
    private ThreadManager tm;

    /**
     * Create an instance of this class.
     *
     * @param jtp JTabbedPane from which tabs will be removed.
     * @param tm ThreadManager to find the thread that will be stopped when a
     * tab is closed.
     */
    public RemoveTabAction(JTabbedPane jtp, ThreadManager tm) {
        this.jtp = jtp;
        this.tm = tm;
    }

    /**
     * Defines what action should be taken when an action is performed on the
     * component with which this action is registered.
     *
     * @param e The ActionEvent from the component.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Component selected = jtp.getSelectedComponent();

        if (selected != null) {
            jtp.remove(selected);
        }

        JButton btn = (JButton) e.getSource();
        String name = btn.getName();
        Logger.getLogger(RemoveTabAction.class.getName()).log(Level.INFO, "Name of table to remove{0}", name);
        if (name != null) {
            ((ResultTabbedPane) jtp).removeTabPaneByName(name);
        }
        tm.killThread(name);

    }
}
