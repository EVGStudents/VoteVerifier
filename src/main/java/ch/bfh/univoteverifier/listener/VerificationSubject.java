/**
 *
 * Copyright (c) 2013 Berner Fachhochschule, Switzerland. Bern University of
 * Applied Sciences, Engineering and Information Technology, Research Institute
 * for Security in the Information Society, E-Voting Group, Biel, Switzerland.
 *
 * Project independent VoteVerifier.
 *
 */
package ch.bfh.univoteverifier.listener;

/**
 * makes up part of the observer pattern
 *
 * @author Justin Springer
 */
public interface VerificationSubject {

	/**
	 * Add a listener to this Subject.
	 *
	 * @param vl A listener to be registered with this subject.
	 */
	public void addListener(VerificationListener vl);

	/**
	 * Remove a listener from the subject.
	 *
	 * @param vl The listener to be removed from the list.
	 */
	public void removeListener(VerificationListener vl);

	/**
	 * Sends the verification event to all the listeners that have been
	 * registered with this subject.
	 *
	 * @param ve The verification event sent.
	 */
	public void notifyListeners(VerificationEvent ve);
}
