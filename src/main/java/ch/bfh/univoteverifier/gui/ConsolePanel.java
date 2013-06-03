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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author prinstin
 *
 * Creates that status panel which is shown at the bottom of the GUI and
 * optionally contains a console-like message area
 *
 * @return a JPanel which is one of the three main container/structure panels
 */
public class ConsolePanel extends JPanel implements ChangeListener {

    private ResourceBundle rb;
    private HashMap textAreaText;
    private JScrollPane scrollPane;
    private static final Logger LOGGER = Logger.getLogger(ConsolePanel.class.getName());
    private JTextArea textArea;
    private String currentTextKey = "welcome";

    /**
     * Create a instance of this class.
     */
    public ConsolePanel() {
        textAreaText = new HashMap();
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());

        this.setLayout(new GridLayout(1, 1));
        this.setBackground(GUIconstants.DARK_GREY);
        this.setVisible(true);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        textArea = createStatusTextBox();

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 150));

        this.add(scrollPane);
    }

    /**
     * Create the textBox that is used as the console-like message area at the
     * bottom of the GUI
     *
     * @return JTextArea which is non editable.
     */
    private JTextArea createStatusTextBox() {
        String welcomeText = rb.getString("welcomeText1") + rb.getString("welcomeText2");
        JTextArea jta = new JTextArea();
        jta.setText(welcomeText);
        jta.setWrapStyleWord(true);
        jta.setLineWrap(true);
        jta.setEditable(false);
        jta.setFont(new Font("Monospaced", Font.PLAIN, 15));
        return jta;
    }

    /**
     * Append the text of the JTextArea containing text in a console-like
     * fashion.
     *
     * @param str The String to append in the console-like text area
     */
    public void appendToStatusText(String str, String processID) {
        if (!textAreaText.containsKey(processID)) {
            textAreaText.put(processID, str);
        } else {
            String currentText = (String) textAreaText.get(processID);
            String newText = currentText + "\n" + str;
            textAreaText.put(processID, newText);
            //If text must be displayed immediately
            if (0 == processID.compareTo(currentTextKey)) {
                textArea.setText(newText);
            }
        }
        scrollPane.revalidate();
        textArea.revalidate();
    }

    /**
     * Change the text area that is visible in the GUI.
     *
     * @param newTextAreaName
     */
    private void toggleVisibleTextArea(String newTextAreaName) {
        String newText = (String) textAreaText.get(newTextAreaName);
        textArea.setText(newText);
    }

    /**
     * Listens to the tabbed pane. When a tab is change this method find the
     * name of the tab that is active and calls a method to have that tab's text
     * area displayed.
     *
     * @param e Event from the tabbedPane.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        String eID = sourceTabbedPane.getTitleAt(index);
        LOGGER.log(Level.INFO, "The component name is :" + eID);
        toggleVisibleTextArea(eID);
    }
}
