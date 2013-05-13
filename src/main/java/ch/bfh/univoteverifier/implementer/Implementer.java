/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.implementer;

import ch.bfh.univoteverifier.common.ElectionBoardProxy;
import ch.bfh.univoteverifier.common.RunnerName;

/**
 * This abstract class represent an Implementer. An implementer has the
 * implementation of the mathematical operations.
 *
 * @author snake
 */
public abstract class Implementer {

	protected final ElectionBoardProxy ebp;
	protected final RunnerName rn;

	/**
	 * Construct a new Implementer with a given ElectionBoardProxy and
	 * RunnerName.
	 *
	 * @param ebp the ElectionBoardProxy used to get the data.
	 * @param rn the RunnerName that has created this Implementer.
	 */
	public Implementer(ElectionBoardProxy ebp, RunnerName rn) {
		this.ebp = ebp;
		this.rn = rn;
	}
}