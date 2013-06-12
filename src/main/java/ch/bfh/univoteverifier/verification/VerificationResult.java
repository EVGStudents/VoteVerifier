/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.verification;

import ch.bfh.univoteverifier.common.EntityType;
import ch.bfh.univoteverifier.common.RunnerName;
import ch.bfh.univoteverifier.common.VerificationType;
import ch.bfh.univoteverifier.common.ImplementerType;
import ch.bfh.univoteverifier.common.Report;

/*
 *This class is a VerificationResult and it represent the result of a single
 * check (for example an RSA Signature). Here we can find some information like
 * the result or if the check is implemented or not.
 * @author Justin Springer
 */
public class VerificationResult {

	private final VerificationType v;
	private final RunnerName rn;
	private boolean impl;
	private final boolean result;
	private Report rep;
	private String entityName;
	private final String eID;
	private final ImplementerType it;
	private final EntityType et;

	/**
	 * Create a new VerificationResult.
	 *
	 * @param v The type of verification that has succeeded.
	 * @param res The result.
	 * @param eID The election ID who need this VerificationResult.
	 * @param rn The name of the runner who wants to have this
	 * VerificationResult.
	 * @param it The implementer that created this VerificationResult
	 * @param et The entity that has verified this VerificationResult
	 */
	public VerificationResult(VerificationType v, boolean res, String eID, RunnerName rn, ImplementerType it, EntityType et) {
		this.v = v;
		this.result = res;
		this.impl = true;
		this.rep = null;
		this.eID = eID;
		this.rn = rn;
		this.it = it;
		this.et = et;
		this.entityName = "notSet";
	}

	/**
	 * The name of the section that has performed this test.
	 *
	 * @return the RunnerName of this VerificationResult.
	 */
	public RunnerName getRunnerName() {
		return rn;
	}

	/**
	 * Get the verification type for this VerificationResult Identifies that
	 * type of message/information that this event contains.
	 *
	 * @return the VerificationType.
	 */
	public VerificationType getVerificationType() {
		return v;
	}

	/**
	 * Get the result for this VerificationResult.
	 *
	 * @return the result as a boolean.
	 */
	public boolean getResult() {
		return result;
	}

	/**
	 * To know if the verification associated with this result is
	 * implemented.
	 *
	 * @return a boolean.
	 */
	public boolean isImplemented() {
		return impl;
	}

	/**
	 * Set the implementation flag for this VerificationResult.
	 *
	 * @param impl the boolean value representing the implementation for
	 * this event.
	 */
	public void setImplemented(boolean impl) {
		this.impl = impl;
	}

	/**
	 * Get the Report for this VerificationResult.
	 *
	 * @return the Report.
	 */
	public Report getReport() {
		return this.rep;
	}

	/**
	 * Set the Report for this VerificationResult.
	 *
	 * @param report the Report.
	 */
	public void setReport(Report report) {
		this.rep = report;
	}

	/**
	 * Set the entity name for this VerificationResult if any. It can be use
	 * to print the name for example of a tallier.
	 *
	 * @param entityName the name for example of a tallier.
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Get the entity name.
	 *
	 * @return a String containing the name.
	 */
	public String getEntityName() {
		return this.entityName;
	}

	/**
	 * Get the election ID.
	 *
	 * @return a String with election ID.
	 */
	public String getElectionID() {
		return this.eID;
	}

	/**
	 * Get the EntityType.
	 *
	 * @return the EntityType.
	 */
	public EntityType getEntityType() {
		return this.et;
	}

	/**
	 * Get the ImplementerType.
	 *
	 * @return the ImplementerType.
	 */
	public ImplementerType getImplementerType() {
		return this.it;
	}
}
