/*
 *
 *  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
 *   Bern University of Applied Sciences, Engineering and Information Technology,
 *   Research Institute for Security in the Information Society, E-Voting Group,
 *   Biel, Switzerland.
 *
 *   Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.table;

import ch.bfh.univote.common.Choice;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This helper class organizes the information that is printed in a result
 * table.
 *
 * @author Justin Springer
 */
public class CandidateResultSet {

    private String eID, processID;
    private static final Logger LOGGER = Logger.getLogger(CandidateResultSet.class.getName());
    Map<Choice, Integer> electionResult;

    /**
     * Create an instance of this class.
     *
     * @param txt The description of the verification performed.
     * @param result The result of the verification.
     */
    public CandidateResultSet(String eID, Map<Choice, Integer> electionResult, String processID) {
        this.eID = eID;
        this.processID = processID;
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
     * Get the tab ID to which this data pertains.
     *
     * @return the tab ID.
     */
    public String getProcessID() {
        return processID;
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
