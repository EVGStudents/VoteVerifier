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
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.action.ActionManager;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Create a menu bar for the only frame for this program.
 *
 * @author prinstin
 */
public class VerificationMenuBar extends JMenuBar {

    private ResourceBundle rb;
    private MainGUI mainGUI;

    /**
     * Create an instance of this class.
     *
     * @param mainGUI
     */
    public VerificationMenuBar(MainGUI mainGUI) {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());
        createFileMenu();
        createViewMenu();
        createLanguageMenu();
        this.add(Box.createHorizontalGlue());
        createHelpMenu();
        this.mainGUI = mainGUI;
    }

    /**
     * Create the file menu.
     */
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

    /**
     * Create the view menu.
     */
    public void createViewMenu() {
        JMenu menu = new JMenu(rb.getString("view"));
        JCheckBoxMenuItem consoleItem = new JCheckBoxMenuItem();
        consoleItem.setSelected(false);

        Action a = ActionManager.getInstance().getAction("showConsole");
        consoleItem.setAction(a);

        menu.add(consoleItem);
        this.add(menu);
    }

    /**
     * Create the language menu.
     */
    public void createLanguageMenu() {
        JMenu menu = new JMenu(rb.getString("language"));

        String[] lang = {"EN", "DE", "FR"};

        for (int i = 0; i < lang.length; i++) {
            JMenuItem menuitem = new JMenuItem();
            menuitem.setText(rb.getString(lang[i]));
            menuitem.setName(lang[i]);
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

    /**
     * Create the help menu.
     */
    public void createHelpMenu() {
        final String ABOUT_I18N = rb.getString("help");
        JMenu menu = new JMenu(ABOUT_I18N);
        JMenuItem exitItem = new JMenuItem(rb.getString("about"));
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File myFile = new File("src/main/resources/iconVoteVerifier.jpg");
                final ImageIcon icon = new ImageIcon(myFile.getPath());
                JOptionPane.showMessageDialog(mainGUI, GUIconstants.ABOUT_TEXT, ABOUT_I18N, JOptionPane.INFORMATION_MESSAGE, icon);
            }
        });
        menu.add(exitItem);


        JMenuItem manualItem = new JMenuItem(rb.getString("manual"));
        manualItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (Desktop.isDesktopSupported()) {
                    try {
                        File file = new File("src/main/resources/UserManual.pdf");
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
            }
        });
        menu.add(manualItem);

        this.add(menu);
    }
}
