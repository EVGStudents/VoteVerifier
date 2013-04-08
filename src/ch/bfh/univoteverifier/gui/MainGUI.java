package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.common.Config;
import static ch.bfh.univoteverifier.common.Config.CONFIG;
import ch.bfh.univoteverifier.common.MainController;
import ch.bfh.univoteverifier.common.MainTemp;
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
    JTextArea statusText;
    Color grey, darkGrey;
    MainController mc;
    StatusListener sl;
    private final String descUni ="Verify the results of an entire election.  Data is downloaded from a public election board.";   
    private final String descInd = "Verify that a given ballot has been received and the certificate is valid.  A QR Code is required.";
    private final String descDefault = "Please select the type of verification to make";
    JLabel vrfDescLabel; 
    
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
        vrfDescLabel.setHorizontalAlignment( SwingConstants.CENTER );
        vrfDescPanel.setBackground(Color.CYAN);
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
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setBackground(grey);
        innerPanel.setPreferredSize(new Dimension(600, 500));
//        innerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel sysSetupPanel = new VrfPanel("System Setup");
        innerPanel.add(sysSetupPanel);
        JPanel electSetupPanel = new VrfPanel("Election Setup");
        innerPanel.add(electSetupPanel);
        JPanel elecPrepPanel = new VrfPanel("Election Preparation");
        innerPanel.add(elecPrepPanel);
        JPanel elecPeriodPanel = new VrfPanel("Election Period Parameters");
        innerPanel.add(elecPeriodPanel);
        JPanel mixerTallierPanel = new VrfPanel("Mixer and Tallier Parameters");
        innerPanel.add(mixerTallierPanel);

        innerPanel.add(new JLabel("This is a label inside innerPanel"));
       
        JScrollPane vrfScrollPanel = new JScrollPane(innerPanel);
        vrfScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        vrfScrollPanel.setBackground(Color.CYAN);
//        vrfScrollPanel.setPreferredSize(new Dimension(300, 150));
        
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
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//            this.setBorder
            label = new JLabel(name);
            this.add(label);

            titlePanel = getBoxPanel();
//            titlePanel.add(label);
//            
//            contentPanel = getBoxPanel();
//            contentPanel.add(dummyEllipsePanel);
//            contentPanel.add(dummyEllipsePanel);

//            this.add(titlePanel);
//            this.add(contentPanel);
        }

        public JPanel getContentPanel() {
            return this.contentPanel;
        }

        public JPanel createDummyEllipsePanel() {
            JPanel panel = getBoxPanel();
            JLabel ellipseContent = new JLabel("Some criteria.................................... TRUE");
            panel.add(ellipseContent);
            return panel;
        }

        public JPanel getBoxPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            return panel;
        }
    }



    public JButton createUniVrfButton() {


        JButton b = new JButton("Universal Verification");
        b.addMouseListener(
                new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
              
            }

            @Override
            public void mousePressed(MouseEvent e) {
               
            }

            @Override
            public void mouseReleased(MouseEvent e) {
              
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

        return b;
    }
    
    
     public JButton createIndVrfButton() {


        JButton b = new JButton("Individual Verification");
        b.addMouseListener(
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

        return b;
    }

     
      public JButton createStartButton() {


        JButton b = new JButton("START");
        b.addMouseListener(
                new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
              
            }

            @Override
            public void mousePressed(MouseEvent e) {
               
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               mc.runUniversal();
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

        return b;
    }
         
         
    /**
     * Draw the panel with the image
     *
     * @return a JPanel title image 
     */
    private JPanel getTitleImage() {
        JPanel imgPanel = new JPanel();
        java.net.URL img = MainTemp.class
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
                   ArrayList<VerificationResult> results= (ArrayList<VerificationResult>)se.getVerificationResult();
                    for (VerificationResult e: results){
                        Boolean result = e.getResult();
                        int code =e.getVerification().getID();
                        String vrfType = getTextFromVrfCode(code);
                        statusText.append("\n" + vrfType + " ............. " + result);
                    }
                    break;
                case VRF_STATUS:
                    statusText.append("\n" + se.message);
                    statusText.setCaretPosition(statusText.getText().length());
                    break;

            }
        }
    }
    
    
    private  final Properties prop = new Properties();
	
	public String getTextFromVrfCode(int code){
		try {
			prop.load(new FileInputStream("src/ch/bfh/univoteverifier/resources/messages.properties"));
		} catch (IOException ex) {
			Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
		}
                
              return (String)prop.getProperty(String.valueOf(code));
	}

}