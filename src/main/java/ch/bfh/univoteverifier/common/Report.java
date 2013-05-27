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
 * This class contains a report of a VerificationResult
 *
 * @author Scalzi Giuseppe
 */
public class Report {

	private final FailureCode fc;
	private String url;
	private String additionalInformation;

	/**
	 * Create a new Report with a given failure code.
	 *
	 * @param fc the FailureCode for this report.
	 */
	public Report(FailureCode fc) {
		this.fc = fc;
	}

	/**
	 * Set the URL of a possible source of explanations for this Report
	 *
	 * @param url the url of the source
	 */
	public void setReportURL(String url) {
		this.url = url;
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
	 * Get the URL of a possible source of explanation.
	 *
	 * @return a String representing the URL.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Get the additional information.
	 *
	 * @return a String representing the additional information.
	 */
	public String getAdditionalInformation() {
		return additionalInformation;
	}
}
