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
package ch.bfh.univoteverifier.table;

import ch.bfh.univote.common.Choice;
import ch.bfh.univoteverifier.common.Report;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.verification.VerificationResult;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * This helper class organizes the information that is printed in a result
 * table.
 *
 * @author prinstin
 */
public class ResultSet {

    private String txt, eID, sectionName, processID;
    private Boolean result;
    private ImageIcon img;
    private JLabel label;
    private RunnerName rn;
    private VerificationResult vr;
    private static final Logger LOGGER = Logger.getLogger(ResultSet.class.getName());
    Map<Choice, Integer> electionResult;

    /**
     * Create an instance of this class.
     *
     * @param txt The description of the verification performed.
     * @param result The result of the verification.
     */
    public ResultSet(String txt, ImageIcon img, VerificationResult vr, String processID) {
        this.txt = txt + "  " + generateFiller(txt);
        this.img = img;
        this.vr = vr;
        this.rn = vr.getRunnerName();
        this.eID = vr.getElectionID();
        if (processID.equals("default")) {
            this.processID = this.eID;
        } else {
            this.processID = processID;
        }
        this.result = vr.getResult();
        label = new JLabel(img);
    }

    /**
     * Get the description of this instance.
     *
     * @return String the description.
     */
    public String getTxt() {
        return txt;
    }

    /**
     * Get the ID for the tab for which this data pertains.
     *
     * @return String the ID of a tab.
     */
    public String getProcessID() {
        return processID;
    }

    public Report getResultReport() {
        return vr.getReport();
    }

    /**
     * Get the verification type of this result.
     *
     * @return VerificationType of the result.
     */
    public VerificationType getVerificationType() {
        return vr.getVerificationType();
    }

    /**
     * Get the entity for to which data belongs.
     *
     * @return String the entity.
     */
    public String getEntityType() {
        return vr.getEntityType().toString();
    }

    /**
     * Get the implementer type to which data belongs.
     *
     * @return String the implementer type.
     */
    public String getImplementerType() {
        LOGGER.log(Level.INFO, "GET IMPLEMENTER TYPE: {0}", vr.getImplementerType().toString());
        return vr.getImplementerType().toString();
    }

    public String getEntityName() {
        return vr.getEntityName();
    }

    /**
     * Get the section name for which this data is to be displayed in the GUI.
     *
     * @return String the section name.
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Set the section name for which this data is to be displayed in the GUI.
     *
     */
    public void setSectionName(String str) {
        sectionName = str;
    }

    /**
     * Set the election results for the candidate votes.
     *
     * @param electionResult
     */
    public void setElectionResult(Map<Choice, Integer> electionResult) {
        this.electionResult = electionResult;
    }

    /**
     * Get the election results for the candidate votes.
     *
     * @param electionResult
     */
    public Map<Choice, Integer> getElectionResult() {
        return this.electionResult;
    }

    /**
     * Get the result of this verification.
     *
     * @return Boolean true if the verification was successful.
     */
    public boolean getResult() {
        return result;
    }

    /**
     * Get the image for this verification. The image is an green check mark if
     * it was successful, a red cross if it was unsuccessful, and an orange
     * question mark if the verification has not yet been implemented.
     *
     * @return
     */
    public ImageIcon getImage() {
        return img;
    }

    /**
     * Get the label which contains an the image for this verification.
     *
     * @return JLabel which contains an image.
     */
    public JLabel getLabel() {
        return label;
    }

    /**
     * Get the section name from which these results come.
     *
     * @return String the section name.
     */
    public RunnerName getRunnerName() {
        return rn;
    }

    /**
     * Get the election ID for which this data is meant.
     *
     * @return the election ID.
     */
    public String getEID() {
        return eID;
    }

    /**
     * Generate and append to a description an appropriate, yet perhaps absurd,
     * amount of ellipses.
     *
     * @param txt The description to which ellipses will be appended.
     * @return A String with a description followed by a bunch of ellipses.
     */
    public String generateFiller(String txt) {
        int count = 150 - txt.length();
        String filler = "..";
        for (int i = 0; i < count; i++) {
            filler += "..";
        }
        return filler;
    }
}
