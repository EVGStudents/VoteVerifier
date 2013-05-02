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
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author prinstin
 *
 * creates that status panel which is shown at the bottom of hte GUI and
 * contains a console-like message area
 *
 * @return a JPanel which is one of the three main container/structure panels
 */
public class ConsolePanel extends JPanel {

    JTextArea statusText;
    ResourceBundle rb;

    public ConsolePanel() {
        rb = ResourceBundle.getBundle("error", GUIconstants.getLocale());

        this.setLayout(new GridLayout(1, 1));
        this.setBackground(GUIconstants.DARK_GREY);
        this.setVisible(true);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        statusText = createStatusTextBox();

        JScrollPane scrollPane = new JScrollPane(statusText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 150));

        this.add(scrollPane);

    }

    /**
     * create the textBox that is used as the console-like message area at the
     * bottom of the GUI
     *
     * @return a JTextArea with scroll bar, non editable
     */
    private JTextArea createStatusTextBox() {
        statusText = new JTextArea();
        statusText.setWrapStyleWord(true);
        statusText.setLineWrap(true);
        statusText.setEditable(false);
        statusText.setFont(new Font("Monospaced", Font.PLAIN, 15));
        statusText.setText(rb.getString("welcomeText1"));
        String nextText = statusText.getText() + rb.getString("welcomeText2");
        statusText.setText(nextText);
        return statusText;
    }

    public JTextArea getStatusBox() {
        return this.statusText;
    }
    
    public void setStatusText(String str){
        statusText.setText(str);
    }
    public void appendToStatusText(String str){
        statusText.append(str);
    }
}
