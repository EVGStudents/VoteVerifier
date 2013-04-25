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

import ch.bfh.univoteverifier.common.QRCode;
import ch.bfh.univoteverifier.common.Config;
import ch.bfh.univoteverifier.common.MainController;
import ch.bfh.univoteverifier.verification.VerificationEnum;
import ch.bfh.univoteverifier.verification.VerificationEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Creates the main window of the GUI and generates the components needed to see
 * the GUI and operate the program
 *
 * @author prinstin
 */
public class MainGUI {

    JFrame frame;
    JPanel northPanel, southPanel, masterPanel, vrfDescPanel, dynamicChoicePanel, innerPanel;
    VrfPanel sysSetupPanel, electSetupPanel, elecPrepPanel, elecPeriodPanel, mixerTallierPanel, activeVrfPanel;
    JTextArea statusText;
    Color grey, darkGrey;
    MainController mc;
    VerificationListener sl;
    private String descDefault = "Please select the type of verification to make";
    JLabel vrfDescLabel, choiceDescLabel;
    VrfButton btnInd, btnUni;
    VrfButton[] btns = {btnInd, btnUni};
    JButton btnStart, btnFileSelector;
    boolean uniVrfSelected = false, selectionMade = false;
    JComboBox comboBox;
    String[] eIDlist; //= {"vsbfh-2013", "bbbbbb", "ccccc", "dddddd", "eeeeee"};
    String rawEIDlist;
    Preferences prefs;
    JScrollPane vrfScrollPanel;
    private final Properties prop = new Properties();
    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());

    /**
     * @param args
     */
    public static void main(String[] args) {
        MainGUI gui = new MainGUI();
        gui.start();
    }

    /**
     * instantiates the basic building blocks of the program such as the
     * controllers and displays the window of the GUI.
     */
    public void start() {
        prefs = Preferences.userNodeForPackage(MainGUI.class);
        rawEIDlist = prefs.get("eIDList", "Bern Zurich vsbfh-2013");
        Pattern pattern = Pattern.compile("\\s");
        eIDlist = pattern.split(rawEIDlist);

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

    }

    /**
     * creates the main components of the main window. The main window is
     * divided into three parts: northPanel, and in the middle hte verification
     * panel (vrfPanel) and at the bottom the statusPanel
     *
     * @return a JPanel which will be set as the main content panel of the frame
     */
    public JPanel createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(getNorthPanel());
        panel.add(getVrfPanel());
        panel.add(getStatusPanel());
        return panel;
    }

    /**
     * create the components necessary to display the northPanel
     *
     * @return a JPanel which contains other components to be shown in the main
     * window
     */
    public JPanel getNorthPanel() {
        northPanel = new JPanel();
        northPanel.setBackground(Color.WHITE);
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

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
        createFileSelectButton();
        JButton btnIndVrf = createIndVrfButton();
        btnStart = createStartButton();

        buttonPanel.add(btnUniVrf);
        buttonPanel.add(btnIndVrf);
        buttonPanel.add(btnStart);

        //description panel.  button in above panel changes text in this panel
        //contains button to start verification
        vrfDescPanel = new JPanel();
        vrfDescPanel.setLayout(new GridLayout(1, 1));
        vrfDescPanel.setBackground(darkGrey);
        vrfDescLabel = new JLabel(descDefault);
        vrfDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        vrfDescPanel.add(vrfDescLabel);

        createComboBox();
        createDynamicChoicePanel();

        northPanel.add(titlePanel);
        northPanel.add(buttonPanel);
        northPanel.add(vrfDescPanel);

        return northPanel;
    }

    /**
     * Creates a panel that whose contents are changed based on selections made
     * by the user. This panel will be displayed in the verification area
     * (middle) of the GUI
     */
    public void createDynamicChoicePanel() {
        dynamicChoicePanel = new JPanel();
        dynamicChoicePanel.setBackground(darkGrey);
        dynamicChoicePanel.setPreferredSize(new Dimension(300, 40));

        choiceDescLabel = new JLabel();
        choiceDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dynamicChoicePanel.add(choiceDescLabel);
    }

    /**
     * creates the comboBox that allows new election IDs to be inputed as well
     * as the selection of previously used election IDs
     */
    public void createComboBox() {
        comboBox = new JComboBox(eIDlist);
        comboBox.setEditable(true);
        comboBox.setSelectedIndex(2);
        comboBox.setSize(30, 50);
        comboBox.setFont(new Font("Serif", Font.PLAIN, 10));
    }

    /**
     * creates the JPanel which is displayed in the middle of the GUI and
     * contains either input options for the user or displays the results of the
     * verification process.
     *
     * @return a JPanel which is one of the three main container/structure
     * panels
     */
    public JPanel getVrfPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(696, 450));
        panel.setBorder(new EmptyBorder(10, 30, 10, 30)); //top left bottom right

        innerPanel = new JPanel();
        innerPanelInitialize();

        vrfScrollPanel = new JScrollPane(innerPanel);
        vrfScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(vrfScrollPanel);
        return panel;
    }

    /**
     * initializes the inner panel of the verification panel (middle panel)
     */
    public void innerPanelInitialize() {
        innerPanel.removeAll();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        innerPanel.setBackground(grey);
//        innerPanel.setPreferredSize(new Dimension(500, 300));
    }

    /**
     * removes content of the inner panel and prepares the panel to display
     * verification results. Meaning it creates the 5 smaller panels where
     * results are shown according to their types
     */
    public void innerPanelBeginVrf() {
        innerPanel.removeAll();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setBackground(grey);
        innerPanel.setPreferredSize(new Dimension(500, 300));
        innerPanel.repaint();
    }

    /**
     * creates that status panel which is shown at the bottom of hte GUI and
     * contains a console-like message area
     *
     * @return a JPanel which is one of the three main container/structure
     * panels
     */
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

    /**
     * create the textBox that is used as the console-like message area at the
     * bottom of the GUI
     *
     * @return a JTextArea with scroll bar, non editable
     */
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

    /**
     * A JPanel that is used to show the verification results in the
     * verification panel.
     */
    public class VrfPanel extends JPanel {

        private String name;
        JPanel titlePanel, contentPanel;
        JLabel label;

        VrfPanel(String s) {
            super();
            name = s;
//            dummyEllipsePanel = createDummyEllipsePanel();
            generatePanel();
        }

        /**
         * creates and structures the panel
         */
        public void generatePanel() {
            this.setPreferredSize(new Dimension(600, 100));
            this.setBackground(grey);
            this.setBorder(new EmptyBorder(10, 10, 10, 10));
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            label = new JLabel(name);
            label.setFont(new Font("Serif", Font.PLAIN, 16));
            titlePanel = getBoxPanel();
            titlePanel.add(label);
            contentPanel = getBoxPanel();
            this.add(titlePanel);
            this.add(contentPanel);
        }

        public String getName() {
            return name;
        }

        /**
         * returns the panel responsible for showing the verification results
         *
         * @return the content panel for this instance
         */
        public JPanel getContentPanel() {
            return this.contentPanel;
        }

        /**
         * add content to the results panel. This method is called if message
         * received over observer pattern
         *
         * @param str the text for the verification result to be displayed to in
         * the GUI
         * @param b the result of the verification
         */
        public void addResultPanel(String str, boolean b) {
            JPanel panel = new JPanel();
            panel.setBackground(grey);
            panel.setBorder(new EmptyBorder(2, 20, 2, 10));
            JLabel ellipseContent = new JLabel(str + "........................................... " + b);
            ellipseContent.setFont(new Font("Serif", Font.PLAIN, 12));
            panel.add(ellipseContent);
            if (b) {
                ImageIcon img = new ImageIcon(MainGUI.class
                        .getResource("/check.png").getPath());
                JLabel imgLabel = new JLabel(img);
                panel.add(imgLabel);
            }
            contentPanel.add(panel);
            vrfScrollPanel.validate();
        }

        /**
         * generate a standard panel for this class
         *
         * @return an empty JPanel used by this class only
         */
        public JPanel getBoxPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            return panel;
        }
    }

    /**
     * create the button that shows the information and buttons needed to start
     * universal verification
     *
     * @return button to choose universal verification a grey background and
     * deactivated focused effects
     */
    public JButton createUniVrfButton() {
        final String descUni = "Verify the results of an entire election.  Enter an election ID:";
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
                primeDescPanel();
                dynamicChoicePanel.add(comboBox);
                choiceDescLabel.setText("Type or select an election ID: ");
                innerPanel.repaint();
                uniVrfSelected = true;
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

    /**
     * create the button that shows the information and buttons needed to start
     * individual verification
     *
     * @return JButton to choose individual verification a grey background and
     * deactivated focused effects
     */
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
                primeDescPanel();
                choiceDescLabel.setText("Please drag a QR code into the space below, or select a file:");
                dynamicChoicePanel.add(btnFileSelector);
                innerPanel.repaint();
                uniVrfSelected = false;
                descDefault = descInd;
                btnUni.depress();
                btnInd.press();
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

    /**
     * create the button that shows a pop-up file selection window so that the
     * user can select a file which contains a QRCode image
     */
    public void createFileSelectButton() {
        btnFileSelector = new JButton("select file");
        btnFileSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String decodeResults = "nothing";
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showDialog(innerPanel, "Select");
                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    File file = fc.getSelectedFile();
                    if (file == null) {
                        statusText.append("File invalid");
                    } else {

                        statusText.append("\n" + file.getPath());
                        QRCode qr = new QRCode();
                        try {
                            qr.decode(file);
                        } catch (IOException ex) {
                            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                            statusText.append("The file could not be read, please try again.");
                        }
                    }
                }
            }
        });
    }

    /**
     * sets the content for the panel that changes when universal or individual
     * buttons are pressed
     */
    public void primeDescPanel() {
        if (!selectionMade) {
            innerPanel.add(dynamicChoicePanel);
        }
        selectionMade = true;

        dynamicChoicePanel.removeAll();
        dynamicChoicePanel.add(choiceDescLabel);
    }

    /**
     * create the button that starts the verification process
     *
     * @return JButton the start button
     */
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

                innerPanelBeginVrf();
                innerPanel.repaint();
                btnInd.setEnabled(false);
                btnUni.setEnabled(false);

                String msg = "Beginning verification for ";
                statusText.setFont(new Font("Monospaced", Font.PLAIN, 16));
                if (uniVrfSelected) {
                    String eID = (String) comboBox.getSelectedItem();
                    msg = msg + "the election id " + eID;
                    rawEIDlist = rawEIDlist + " " + eID;
                    prefs.put("eIDList", rawEIDlist);
                    mc.universalVerification(eID);
                    mc.getStatusSubject().addListener(sl);
                    mc.runVerifcation();
                } else {
                    msg += "the provided ballot receipt.";
                    mc.individualVerification(eIDlist[0]);
                }
                statusText.setText(msg);
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

    /**
     * a Button type that contains a description and methods that changes its
     * appearance
     */
    private class VrfButton extends JButton {

        String description;

        public VrfButton(String name, String description) {
            super(name);
            this.setBackground(grey);
            this.setFocusPainted(false);
        }

        /**
         * called when another button is pressed set the color of the button to
         * light grey
         */
        public void depress() {
            this.setBackground(grey);
        }

        /**
         * called when the button is pressed set the color of the button to dark
         * grey
         */
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
        java.net.URL img = MainGUI.class.getResource("/univoteTitle.jpeg");
        if (img != null) {
            ImageIcon logo = new ImageIcon(img);
            JLabel imgLab = new JLabel(logo);
            imgPanel.setMaximumSize(new Dimension(300, 114));
            imgPanel.add(imgLab);
            imgPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        } else {
            LOGGER.log(Level.INFO, "IMAGE NOT FOUND");
        }
        return imgPanel;
    }

    /**
     * This inner class represents the implementation of the observer pattern for
     * the status messages for the console and verification parts of the gui
     *
     * @author prinstin
     */
    class StatusUpdate implements VerificationListener {

        @Override
        public void updateStatus(VerificationEvent ve) {

            Logger.getLogger(StatusUpdate.class.getName()).log(Level.INFO, "status event received.  Type:{0}", ve.getVerificationEnum());
            if (ve.getVerificationEnum() == VerificationEnum.ERROR) {
                statusText.append("\n" + ve.getMessage());
                statusText.setCaretPosition(statusText.getText().length());
            } else {
                String sectionName = ve.getSection().toString();
                if (activeVrfPanel == null) {
                    activeVrfPanel = new VrfPanel(sectionName);
                    innerPanel.add(activeVrfPanel);
                } else if (!activeVrfPanel.getName().equals(sectionName)) {
                    activeVrfPanel = new VrfPanel((sectionName));
                    innerPanel.add(activeVrfPanel);
                }

                Boolean result = ve.getResult();
                int code = ve.getVerificationEnum().getID();
                String vrfType = getTextFromVrfCode(code);
                String outputText = "\n" + vrfType + " ............. " + result;
                //statusText.append(outputText);
                statusText.setText(statusText.getText() + outputText);

                //add to GUI verification area
                Logger.getLogger(StatusUpdate.class.getName()).log(Level.INFO, "console output {0}", outputText);
                activeVrfPanel.addResultPanel(vrfType, result);


            }
        }
    }

    /**
     * turns the vrfCode into a text string that is shown in the GUI
     *
     * @param code int value which corresponds to a verification type
     * @return the user-friendly text the describes a verification step
     */
    public String getTextFromVrfCode(int code) {
        try {
            prop.load(new FileInputStream("src/main/java/ch/bfh/univoteverifier/resources/messages.properties"));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (String) prop.getProperty(String.valueOf(code));
    }
}
