/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.action;

import ch.bfh.univoteverifier.gui.GUIconstants;
import ch.bfh.univoteverifier.gui.MainGUI;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;

/**
 *
 * @author prinstin
 */
public class ShowConsoleAction extends AbstractAction {
MainGUI mainGUI;
    public ShowConsoleAction(MainGUI mainGUI){
        ResourceBundle rb= ResourceBundle.getBundle("error", GUIconstants.getLocale());
        this.putValue(NAME, rb.getString("showConsole"));
        this.mainGUI=mainGUI;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBoxMenuItem mItem = (JCheckBoxMenuItem)e.getSource();
       boolean selected = mItem.getState();
       mainGUI.showConsole(selected);
    }
    
    
}
