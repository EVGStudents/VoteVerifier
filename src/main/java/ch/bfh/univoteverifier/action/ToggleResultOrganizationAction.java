/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.action;

import ch.bfh.univoteverifier.table.ResultTablePanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
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
        System.out.println("TOGGLE RESULT ORGANIZATOIN CALLED:" + name);
    }
}
