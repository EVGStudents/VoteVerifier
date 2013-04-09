package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.common.Config;
import static ch.bfh.univoteverifier.common.Config.CONFIG;
import ch.bfh.univoteverifier.common.MainController;
import ch.bfh.univoteverifier.verification.Verification;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class MainGUI {

    JFrame frame;
    JPanel northPanel, southPanel, masterPanel;
    VrfPanel sysSetupPanel, electSetupPanel, elecPrepPanel, elecPeriodPanel, mixerTallierPanel;
    JTextArea statusText;
    Color grey, darkGrey;
    MainController mc;
    StatusListener sl;
    private String descDefault = "Please select the type of verification to make";
    JLabel vrfDescLabel;
    VrfButton btnInd, btnUni;
    VrfButton[] btns={btnInd,btnUni};
    JButton btnStart;

    /**
     * @param args
     */
    public static void main(String[] args) {
        MainGUI gui = new MainGUI();
        gui.start();
    }

    public void start() {
        mc = new MainController();
        sl = new StatusUpdate();

        grey = new Color(190, 190, 190);
        darkGrey = new Color(140, 140, 140);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(696, 400));
        masterPanel = createUI();
        masterPanel.setOpaque(true); //content panes must be opaque
        frame.setContentPane(masterPanel);

        frame.setTitle("Independent UniVote Verifier");
        frame.pack();
        frame.setVisible(true);
        mc.getUniversalStatusSubject().addListener(sl);
        mc.getIndividualStatusSubject().addListener(sl);
    }

    public JPanel createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(getNorthPanel());
        panel.add(getVrfPanel());
        panel.add(getStatusPanel());
        return panel;
    }

    public JPanel getNorthPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        //title panel with white background and image
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(1, 1));
        titlePanel.setBackground(Color.white);
        titlePanel.add(getTitleImage());
        titlePanel.setBorder(new EmptyBorder(0, 0, 0, 0));


        //button panel with two buttons and grey background
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(grey);

        JButton btnUniVrf = createUniVrfButton();
        JButton btnIndVrf = createIndVrfButton();
        JButton btnStart = createStartButton();

        buttonPanel.add(btnUniVrf);
        buttonPanel.add(btnIndVrf);
        buttonPanel.add(btnStart);


        //description panel.  button in above panel changes text in this panel
        //contains button to start verification
        JPanel vrfDescPanel = new JPanel();
        vrfDescPanel.setLayout(new GridLayout(1, 1));
        vrfDescPanel.setBackground(darkGrey);
        vrfDescLabel = new JLabel(descDefault);
        vrfDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        vrfDescPanel.add(vrfDescLabel);

        panel.add(titlePanel);
        panel.add(buttonPanel);
        panel.add(vrfDescPanel);
        return panel;
    }

    public JPanel getVrfPanel() {
        JPanel panel = new JPanel();
//       panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setLayout(new GridLayout(1, 1));
        panel.setBackground(Color.WHITE);

        panel.setPreferredSize(new Dimension(696, 450));
        panel.setBorder(new EmptyBorder(10, 30, 10, 30)); //top left bottom right

        JPanel innerPanel = new JPanel();
//        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setLayout(new GridLayout(5, 1));

        innerPanel.setBackground(grey);
        innerPanel.setPreferredSize(new Dimension(500, 300));
//        innerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        sysSetupPanel = new VrfPanel("System Setup");
        innerPanel.add(sysSetupPanel);
        electSetupPanel = new VrfPanel("Election Setup");
        innerPanel.add(electSetupPanel);
        elecPrepPanel = new VrfPanel("Election Preparation");
        innerPanel.add(elecPrepPanel);
        elecPeriodPanel = new VrfPanel("Election Period Parameters");
        innerPanel.add(elecPeriodPanel);
        mixerTallierPanel = new VrfPanel("Mixer and Tallier Parameters");
        innerPanel.add(mixerTallierPanel);

        JScrollPane vrfScrollPanel = new JScrollPane(innerPanel);
        vrfScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(vrfScrollPanel);
        return panel;
    }

    public JPanel getStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        panel.setBackground(darkGrey);
        panel.setVisible(true);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        statusText = getStatusTextBox();

        JScrollPane scrollPane = new JScrollPane(statusText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 150));

        panel.add(scrollPane);
        return panel;
    }

    public JTextArea getStatusTextBox() {
        statusText = new JTextArea();
        statusText.setWrapStyleWord(true);
        statusText.setLineWrap(true);
        statusText.setEditable(false);
        statusText.setFont(new Font("Monospaced", Font.PLAIN, 15));
        statusText.setText("Welcome to the Independent UniVote Verifier.");
        String nextText = statusText.getText() + "\nPlease select a choice from the menu above.";
        statusText.setText(nextText);
        return statusText;
    }

    public class VrfPanel extends JPanel {

        String name;
        JPanel titlePanel, contentPanel, dummyEllipsePanel;
        JLabel label;

        VrfPanel(String s) {
            super();
            name = s;
//            dummyEllipsePanel = createDummyEllipsePanel();
            generatePanel();
        }

        public void generatePanel() {
            this.setPreferredSize(new Dimension(600, 100));
            this.setBorder(new EmptyBorder(10, 10, 10, 10));
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//            this.setBorder
            label = new JLabel(name);
            label.setFont(new Font("Serif", Font.PLAIN, 16));

            titlePanel = getBoxPanel();
            titlePanel.add(label);


            contentPanel = getBoxPanel();
            contentPanel.add(createDummyResultPanel());
            contentPanel.add(createDummyResultPanel());

            this.add(titlePanel);
            this.add(contentPanel);
        }

        public JPanel getContentPanel() {
            return this.contentPanel;
        }

        public JPanel createDummyResultPanel() {
            JPanel panel = getBoxPanel();
            panel.setBorder(new EmptyBorder(2, 20, 2, 10));
            JLabel ellipseContent = new JLabel("Some criteria........................................... TRUE");
            ellipseContent.setFont(new Font("Serif", Font.PLAIN, 12));
            panel.add(ellipseContent);
            return panel;
        }

        public void addResultPanel(String s, Boolean b) {
            JLabel vrfResults = new JLabel(s + "........................................... " + b);
            vrfResults.setFont(new Font("Serif", Font.PLAIN, 12));
            contentPanel.add(vrfResults);
        }

        public JPanel getBoxPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            return panel;
        }
    }

    public JButton createUniVrfButton() {

        final String descUni = "Verify the results of an entire election.  Data is downloaded from a public election board.";
        btnUni = new VrfButton("Universal Verification", descUni);
        
        btnUni.addMouseListener(
                new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mc.testObserverPattern();
                descDefault = descUni;
                btnInd.depress();
                btnUni.press();
                statusText.setText("Universal Verification");
                statusText.setFont(new Font("Serif", Font.PLAIN, 32));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                vrfDescLabel.setText(descUni);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                vrfDescLabel.setText(descDefault);
            }
        });

        return btnUni;
    }

    public JButton createIndVrfButton() {


        final String descInd = "Verify that a given ballot has been received and the certificate is valid.  A QR Code is required.";
        btnInd = new VrfButton("Individual Verification", descInd);
      
        btnInd.addMouseListener(
                new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mc.testObserverPattern();
                descDefault = descInd;
                btnUni.depress();
                btnInd.press();
                statusText.setText("Individual Verification");
                statusText.setFont(new Font("Serif", Font.PLAIN, 32));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                vrfDescLabel.setText(descInd);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                vrfDescLabel.setText(descDefault);
            }
        });

        return btnInd;
    }

    public JButton createStartButton() {

        btnStart = new JButton("START");
        btnStart.setBackground(new Color(110, 110, 254));
        btnStart.addMouseListener(
                new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
                statusText.setText("Beginning verification...");
                statusText.setFont(new Font("Monospaced", Font.PLAIN, 15));
                mc.universalVerification("vsbfh-2013");;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        return btnStart;
    }

    private class VrfButton extends JButton {

        String description;

        public VrfButton(String name, String description) {
            super(name);
            this.setBackground(grey);
            this.setFocusPainted(false);
        }

        public void depress() {
            this.setBackground(grey);
        }

        public void press() {
            this.setBackground(darkGrey);
        }
    }

    /**
     * Draw the panel with the image
     *
     * @return a JPanel title image
     */
    private JPanel getTitleImage() {
        JPanel imgPanel = new JPanel();
        java.net.URL img = MainGUI.class
                .getResource("/ch/bfh/univoteverifier/resources/univoteTitle.jpeg");
        if (img != null) {
            ImageIcon logo = new ImageIcon(img);
            JLabel imgLab = new JLabel(logo);
            imgPanel.setMaximumSize(new Dimension(300, 114));
            imgPanel.add(imgLab);
            imgPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        } else {
            System.out.println("IMAGE NOT FOUND");
        }
        return imgPanel;
    }

    /**
     * This inner class represent the implementation of the observer pattern for
     * the status messages for the console and verification parts of the gui
     *
     * @author prinstin
     */
    class StatusUpdate implements StatusListener {

        @Override
        public void updateStatus(StatusEvent se) {


            switch (se.getStatusMessage()) {
                case VRF_RESULT:
                    ArrayList<VerificationResult> results = (ArrayList<VerificationResult>) se.getVerificationResult();
                    for (VerificationResult e : results) {

                        //Add to console
                        Boolean result = e.getResult();
                        int code = e.getVerification().getID();
                        String vrfType = getTextFromVrfCode(code);
                        statusText.append("\n" + vrfType + " ............. " + result);

                        //add to GUI verification area
                        sysSetupPanel.addResultPanel(vrfType, result);

                    }
                    break;
                case VRF_STATUS:
                    statusText.append("\n" + se.message);
                    statusText.setCaretPosition(statusText.getText().length());
                    break;

            }
        }
    }
    private final Properties prop = new Properties();

    public String getTextFromVrfCode(int code) {
        try {
            prop.load(new FileInputStream("src/main/java/ch/bfh/univoteverifier/resources/messages.properties"));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (String) prop.getProperty(String.valueOf(code));
    }
}