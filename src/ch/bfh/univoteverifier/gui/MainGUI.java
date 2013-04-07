package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.common.MainTemp;
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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
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
        frame.setMinimumSize(new Dimension(696, 400));    
        masterPanel = createUI();
        masterPanel.setOpaque(true); //content panes must be opaque
        frame.setContentPane(masterPanel);

        frame.setTitle("Independent UniVote Verifier");
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(getNorthPanel());
        panel.add(getVrfPanel());
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
        scrollPane.setPreferredSize(new Dimension(300, 150));

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

    public JPanel getVrfPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(696, 450));
        
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setBackground(grey);
        innerPanel.setPreferredSize(new Dimension(600, 500));
        innerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
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
        
        
        panel.add(innerPanel);
        return panel;
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
        
        public JPanel createDummyEllipsePanel(){
             JPanel panel = getBoxPanel();
             JLabel ellipseContent = new JLabel("Some criteria.................................... TRUE");
             panel.add(ellipseContent);
             return panel;
        }
        
        public JPanel getBoxPanel(){
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); 
            return panel;
        }
    }

    public JPanel getNorthPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel whiteHR = new JPanel();
        JPanel greyHR = new JPanel();
        JPanel darkGreyHR = new JPanel();

        //title panel with white background and image
        whiteHR.setLayout(new GridLayout(1,1));
        whiteHR.setBackground(Color.white);
        whiteHR.add(getTitleImage());
//        whiteHR.setMaximumSize(new Dimension(696, 30));
        whiteHR.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        //button panel with two buttons and grey background
        greyHR.setBackground(grey);
        JButton btnUniVrf= new JButton("Universal Verification");
        
        JButton btnIndVrf= new JButton("Individual Verification");
        greyHR.add(btnUniVrf);
        greyHR.add(btnIndVrf);
        
        //description panel.  button in above panel changes text in this panel
        //contains button to start verification
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
        JPanel imgPanel = new JPanel();
        java.net.URL img = MainTemp.class.getResource("/ch/bfh/univoteverifier/resources/univoteTitle.jpeg");
        
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
