/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * This helper class organizes the information that is printed in a result table.
 * @author prinstin
 */
public class ResultSet {

    private String txt;
    private Boolean result;
    private ImageIcon img;
    private JLabel label;
    private String sectionName;
    
    /**
     * Create an instance of this class.
     * @param txt The description of the verification performed.
     * @param result The result of the verification.
     */
    public ResultSet(String txt, Boolean result, String sectionName) {
        this.txt = txt +"  "+ generateFiller(txt);
        this.result = result;
        this.sectionName=sectionName;
       java.net.URL urlImg = MainGUI.class.getResource("/check.png");
        this.img = new ImageIcon(urlImg);
        label=new JLabel(img);
    }

    /**
     * Get the description of this instance.
     * @return String the description.
     */
    public String getTxt() {
        return txt;
    }

    /**
     * Get the result of this verification.
     * @return Boolean true if the verification was successful.
     */
    public boolean getResult() {
        return result;
    }
    
    /**
     * Get the image for this verification. The image is an green check mark if it was successful, a red cross if it was unsuccessful, and an orange question mark if the verification has not yet been implemented.
     * @return 
     */
    public ImageIcon getImage(){
    return img;
    }
    
    /**
     * Get the label which contains an the image for this verification.
     * @return JLabel which contains an image.
     */
    public JLabel getLabel(){
        return label;
    }
    
        /**
     * Get the section name from which these results come.
     * @return String the section name.
     */
    public String getSectionName(){
        return sectionName;
    }
    
    /**
     * Generate and append to a description an appropriate, yet perhaps absurd, amount of ellipses.
     * @param txt The description to which ellipses will be appended.
     * @return A String with a description followed by a bunch of ellipses.
     */
    public String generateFiller(String txt){
        int count = 150 - txt.length();
        String filler="..";
        for (int i=0;i<count;i++)
            filler += "..";
        return filler;
    }
}
