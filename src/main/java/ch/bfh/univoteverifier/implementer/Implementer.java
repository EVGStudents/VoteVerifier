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
package ch.bfh.univoteverifier.implementer;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.StringConcatenator;

/**
 * This abstract class represent an Implementer. An implementer has the
 * implementation of the mathematical operations.
 *
 * @author Scalzi Giuseppe
 */
public abstract class Implementer {

    protected final ElectionBoardProxy ebp;
    protected final RunnerName rn;
    protected final StringConcatenator sc;
    protected final ImplementerType it;

    /**
     * Construct a new Implementer with a given ElectionBoardProxy and
     * RunnerName.
     *
     * @param ebp the ElectionBoardProxy used to get the data.
     * @param rn the RunnerName that has created this Implementer.
     */
    public Implementer(ElectionBoardProxy ebp, RunnerName rn, ImplementerType it) {
        this.ebp = ebp;
        this.rn = rn;
        this.sc = new StringConcatenator();
        this.it = it;
    }
}
