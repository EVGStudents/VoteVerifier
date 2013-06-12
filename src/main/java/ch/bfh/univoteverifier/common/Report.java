/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project Independant UniVoteVerifier.
 *
 */
package ch.bfh.univoteverifier.common;

/**
 * This class contains a report of a VerificationResult.
 *
 * @author Scalzi Giuseppe
 */
public class Report {

	private final FailureCode fc;
	private final Exception ex;
	private String additionalInformation;

	/**
	 * Create a new Report with a given failure code.
	 *
	 * @param fc the FailureCode for this report.
	 */
	public Report(FailureCode fc) {
		this.fc = fc;
		this.ex = null;
	}

	/**
	 * Create a new Report with a given exception.
	 *
	 * @param ex the Exceptionfor this report.
	 */
	public Report(Exception ex) {
		this.fc = null;
		this.ex = ex;
	}

	/**
	 * Set the additional information of this report, these can be for
	 * example a longer explanation of the failure.
	 *
	 * @param additionalInformation the additional information as String.
	 */
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	/**
	 * Get the FailureCode.
	 *
	 * @return the FailureCode.
	 */
	public FailureCode getFailureCode() {
		return fc;
	}

	/**
	 * Get the additional information.
	 *
	 * @return a String representing the additional information.
	 */
	public String getAdditionalInformation() {
		return additionalInformation;
	}

	/**
	 * Get the exception for this report.
	 *
	 * @return an Exception containing the message.
	 */
	public Exception getException() {
		return ex;
	}
}
