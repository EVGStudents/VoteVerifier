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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author prinstin
 */
public class VerificationMenuBar extends JMenuBar {

    JMenu fileMenu;
    ResourceBundle rb;
    ConsolePanel consolePanel;

    public VerificationMenuBar(ConsolePanel consolePanel) {
        rb = ResourceBundle.getBundle("error", Locale.ENGLISH);
        this.consolePanel = consolePanel;
        createFileMenu();
        createLanguageMenu();

    }

    public void createFileMenu() {
        fileMenu = new JMenu("file");
        JMenuItem exitItem = new JMenuItem("exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileMenu.add(exitItem);
        this.add(fileMenu);
    }

    public void createLanguageMenu() {
        JMenu langMenu = new JMenu("Language");
        Action a = ActionManager.getInstance().getAction("changeLocale");
        
        String[] lang = {"English", "German", "French"};
        
        for (int i = 0; i < lang.length; i++) {
            JMenuItem menuitem = new JMenuItem(lang[i]);
            menuitem.setName(lang[i]);
            menuitem.setAction(a);
            langMenu.add(menuitem);
        }
        this.add(langMenu);
    }
}
