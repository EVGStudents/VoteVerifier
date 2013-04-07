package ch.bfh.univoteverifier.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class MainGUI {

    JFrame frame;
    JPanel northPanel, southPanel, masterPanel;
    JTextArea statusText;
    Color grey, darkGrey;

    /**
     * @param args
     */
    public static void main(String[] args) {
        MainGUI gui = new MainGUI();
        gui.start();
    }

    public void start() {
        grey = new Color(190, 190, 190);
        darkGrey = new Color(140, 140, 140);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        masterPanel = createUI();
        masterPanel.setOpaque(true); //content panes must be opaque
        frame.setContentPane(masterPanel);

        frame.setTitle("Independent UniVote Verifier");
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
//        masterPanel.add(northPanel, BorderLayout.NORTH);
//        masterPanel.add(southPanel, BorderLayout.SOUTH);
        panel.add(getNorthPanel());

//        masterPanel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(Box.createVerticalStrut(5));
        panel.add(getStatusPanel());


        return panel;
    }

    public JPanel getStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        panel.setBackground(darkGrey);
        panel.setVisible(true);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        Dimension frameSize = new Dimension((int) (screenSize.width / 2), (int) (screenSize.height / 2));
//        int x = (int) (frameSize.width / 32);
//        int y = (int) (frameSize.height / 16);

        //Get the text box
        statusText = getStatusTextBox();

        JScrollPane scrollPane = new JScrollPane(statusText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(400, 150));

        panel.add(scrollPane);
        return panel;
    }

    public JTextArea getStatusTextBox() {
        statusText = new JTextArea();
//        statusText.setWrapStyleWord(true);
//        statusText.setLineWrap(true);
        statusText.setEditable(false);
        statusText.setFont(new Font("Monospaced", Font.PLAIN, 15));
//        statusText.append("Welcome to the Independent UniVote Verifier.");
        statusText.setText("Welcome to the Independent UniVote Verifier.");
        String nextText = statusText.getText() + "\nPlease select a choice from the menu above.";
        statusText.setText(nextText);
//        statusText.append("\nPlease select a choice from the menu above.");
        return statusText;
    }

    public JPanel getNorthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JLabel titleLabel = new JLabel("Independent UniVote Verifier");
        panel.add(titleLabel);

        JPanel whiteHR = new JPanel();
        JPanel greyHR = new JPanel();
        JPanel darkGreyHR = new JPanel();

        whiteHR.setBackground(Color.white);
        whiteHR.add(getTitleImage());
        greyHR.setBackground(grey);
        darkGreyHR.setBackground(darkGrey);

        panel.add(whiteHR);
        panel.add(greyHR);
        panel.add(darkGreyHR);
        return panel;
    }

    /**
     * Draw the panel with the image
     *
     * @return a JPanel with the logo of monopoly
     */
    private JPanel getTitleImage() {
        JPanel img = new JPanel();
        java.net.URL urlImg = getClass().getResource("/ch/bfh/univoteverifier/resources/univoteTitle.jpeg");
        if (urlImg != null) {
            ImageIcon logo = new ImageIcon(urlImg);
            JLabel imgLab = new JLabel(logo);
            img.setMaximumSize(new Dimension(300, 114));
            img.add(imgLab);
            img.setAlignmentX(Component.LEFT_ALIGNMENT);
        } else {
            System.out.println("IMAGE NOT FOUND");
        }
        return img;
    }

    class ResizableTextField extends JTextField implements ComponentListener {

        float iw, ih, sw, sh, weight;
        boolean first;

        public void TextField() {
            first = true;
            weight = 8.0f;
            addComponentListener(this);
        }

        @Override
        public void componentResized(ComponentEvent e) {
            if (first) {
                iw = getWidth();
                ih = getHeight();
                first = false;
            }

            sw = (getWidth() / iw) * weight;
            if (sw == 0f) {
                sw = 1f;
            }
            sh = (getHeight() / ih) * weight;
            if (sh == 0f) {
                sh = 1f;
            }
            AffineTransform trans = AffineTransform.getScaleInstance(sw, sh);
            setFont(getFont().deriveFont(trans));
        }

        public void componentShown(ComponentEvent e) {
        }

        public void componentHidden(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
        }
    }
}
