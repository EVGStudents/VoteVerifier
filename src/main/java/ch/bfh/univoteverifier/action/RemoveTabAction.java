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

import ch.bfh.univoteverifier.gui.ThreadManager;
import ch.bfh.univoteverifier.table.ResultTab;
import ch.bfh.univoteverifier.table.ResultTabbedPane;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;

/**
 * Action to remove a tab from the TabbedPane in the Verification panel (the
 * middle).
 *
 * @author prinstin
 */
public class RemoveTabAction extends AbstractAction {

    private ResultTabbedPane rtp;
    private ThreadManager tm;

    /**
     * Create an instance of this class.
     *
     * @param rtp JTabbedPane from which tabs will be removed.
     * @param tm ThreadManager to find the thread that will be stopped when a
     * tab is closed.
     */
    public RemoveTabAction(ResultTabbedPane rtp, ThreadManager tm) {
        this.rtp = rtp;
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
        String processID = ((JButton) e.getSource()).getName();
        tm.killThread(processID);

        ResultTab rt = rtp.getTabPaneByName(processID);
        if (rt != null) {
            int index = rtp.indexOfComponent(rt);
            rtp.remove(rt);
        } else {
            Logger.getLogger(RemoveTabAction.class.getName()).log(Level.SEVERE, "Could not find tab to remove with processID: {0}", processID);
        }
    }
}
