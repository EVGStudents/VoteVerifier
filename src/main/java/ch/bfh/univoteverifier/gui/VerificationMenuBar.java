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
import ch.bfh.univoteverifier.action.ShowConsoleAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author prinstin
 */
public class VerificationMenuBar extends JMenuBar {

   
    ResourceBundle rb;
    MainGUI mainGUI;

    public VerificationMenuBar(MainGUI mainGUI) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        createFileMenu();
        createViewMenu();
        createLanguageMenu();
        this.add(Box.createHorizontalGlue());
        createHelpMenu();
        this.mainGUI = mainGUI;
    }

    public void createFileMenu() {
        JMenu menu = new JMenu(rb.getString("file"));
        JMenuItem exitItem = new JMenuItem(rb.getString("exit"));
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menu.add(exitItem);
        this.add(menu);
    }
    
   public void createViewMenu() {
        JMenu menu = new JMenu(rb.getString("view"));
        JCheckBoxMenuItem consoleItem = new JCheckBoxMenuItem();
        consoleItem.setSelected(true);
        
        Action a = ActionManager.getInstance().getAction("showConsole");
        consoleItem.setAction(a);

        menu.add(consoleItem);
        this.add(menu);
    }

    public void createLanguageMenu() {
        JMenu menu = new JMenu(rb.getString("language"));
//        Action a = ActionManager.getInstance().getAction("changeLocale");

        String[] lang = {"EN", "DE", "FR"};

        for (int i = 0; i < lang.length; i++) {
            JMenuItem menuitem = new JMenuItem();
            menuitem.setText(rb.getString(lang[i])); //THIS SHOWS IN THE MENU
            menuitem.setName(lang[i]); //THIS CAN IDENTIFY THE OBJECT
//            menuitem.setAction(a);
            menuitem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    String name = ((JMenuItem) e.getSource()).getName();
                    GUIconstants.setLocale(name);
                    mainGUI.resetContentPanel();
                }
            });
            menu.add(menuitem);
        }
        this.add(menu);
    }
    
        public void createHelpMenu() {
        JMenu menu = new JMenu(rb.getString("help"));
        JMenuItem exitItem = new JMenuItem(rb.getString("about"));
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainGUI, GUIconstants.ABOUT_TEXT);
            }
        });

        menu.add(exitItem);
        this.add(menu);
    }
    
    
}
