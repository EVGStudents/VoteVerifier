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
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.action.ActionManager;
import ch.bfh.univoteverifier.action.ChangeLocaleAction;
import ch.bfh.univoteverifier.action.FileChooserAction;
import ch.bfh.univoteverifier.action.StartAction;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author prinstin
 */
public class I18nButtonPanel extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());
    
    /**
     * create the panel which hold the button with flag icons representing the
     * different languages possible for the program
     *
     * @return JPanel the panel which contains the flag buttons
     */
    public I18nButtonPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        Action a = ActionManager.getInstance().getAction("changeLocale");
        
        JButton btnDe = new JButton(new ImageIcon(MainGUI.class
                .getResource("/DE.png").getPath()));
        btnDe.setBorder(BorderFactory.createEmptyBorder());
        btnDe.setContentAreaFilled(false);
        btnDe.setName("btnDe");
//        btnDe.setAction(a);

        JButton btnEn = new JButton(new ImageIcon(MainGUI.class
                .getResource("/EN.png").getPath()));
        btnEn.setBorder(BorderFactory.createEmptyBorder());
        btnEn.setContentAreaFilled(false);
        btnEn.setName("btnEn");
        btnEn.setHideActionText(true);
        btnEn.setAction(a);

        JButton btnFr = new JButton(new ImageIcon(MainGUI.class
                .getResource("/FR.png").getPath()));
        btnFr.setBorder(BorderFactory.createEmptyBorder());
        btnFr.setContentAreaFilled(false);
        btnFr.setName("btnFr");
        btnFr.setAction(a);
        
        this.add(btnDe);
        this.add(btnFr);
        this.add(btnEn);

    }
}
