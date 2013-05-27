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
public class CandidateResultSet {

    private String eID;
    private static final Logger LOGGER = Logger.getLogger(CandidateResultSet.class.getName());
    Map<Choice, Integer> electionResult;

    /**
     * Create an instance of this class.
     *
     * @param txt The description of the verification performed.
     * @param result The result of the verification.
     */
    public CandidateResultSet(String eID, Map<Choice, Integer> electionResult) {
        this.eID = generateFiller(eID);
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
