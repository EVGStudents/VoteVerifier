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

import ch.bfh.univoteverifier.table.ResultTablePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;

/**
 *
 * @author prinstin
 */
public class ToggleResultOrganizationAction implements ActionListener {

    ResultTablePanel rtp;

    public ToggleResultOrganizationAction(ResultTablePanel rtp) {
        this.rtp = rtp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = ((JComponent) e.getSource()).getName();
//        rtp.showPanel(name);

        if (0 == name.compareTo("btnEntity")) {
            rtp.showPanelEntity();
        }
        if (0 == name.compareTo("btnSpec")) {
            rtp.showPanelSpec();
        }
        if (0 == name.compareTo("btnType")) {
            rtp.showPanelType();
        }
    }
}
