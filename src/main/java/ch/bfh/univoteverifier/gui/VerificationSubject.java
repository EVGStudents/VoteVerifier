/**
*
*  Copyright (c) 2013 Berner Fachhochschule, Switzerland.
*   Bern University of Applied Sciences, Engineering and Information Technology,
*   Research Institute for Security in the Information Society, E-Voting Group,
*   Biel, Switzerland.
*
*   Project independent UniVoteVerifier.
*
*/
package ch.bfh.univoteverifier.gui;

import ch.bfh.univoteverifier.verification.VerificationEvent;

/**
 * makes up part of the observer pattern
 * @author prinstin
 */
public interface VerificationSubject {

    /**
     * add a listener to this Subject
     * @param vl a listener to be registered with this subject
     */
    public void addListener(VerificationListener vl);

    /**
     * remove a listener from the subject
     * @param vl the listener to be removed from the list
     */
    public void removeListener(VerificationListener vl);

    /**
     * sends the verification event to all the listeners that have been registered with this subject
     * @param se
     */
    public void notifyListeners(VerificationEvent ve);
}
