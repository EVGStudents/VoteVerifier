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

import ch.bfh.univoteverifier.table.ResultTab;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;

/**
 * This class is responsible for changing which results are shown in the GUI.
 *
 * @author prinstin
 */
public class ToggleResultOrganizationAction implements ActionListener {

    private ResultTab rtp;

    /*
     * Create an instance of this class with a give ResultTab.
     */
    public ToggleResultOrganizationAction(ResultTab rtp) {
        this.rtp = rtp;
    }

    /**
     * Designates what should be down when a given action is performed.
     *
     * @param e The object that has triggered this event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = ((JComponent) e.getSource()).getName();
        if (name.equals("btnEntity")) {
            rtp.showPanelEntity();
        } else if (name.equals("btnSpec")) {
            rtp.showPanelSpec();
        } else if (name.equals("btnType")) {
            rtp.showPanelType();
        } else if (name.equals("btnViewResults")) {
            rtp.showCandidateResults();
        } else if (name.equals("btnBack")) {
            rtp.toggleMainPanel();
        }
    }
}
