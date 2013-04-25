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
 *
 * @author prinstin
 */

public interface VerificationListener {
    
    /**
     * receives information from the implementation of the verification classes and decides what should be done with it
     * @param se StatusEvent object: the container of the information
     */
    public void updateStatus(VerificationEvent ve);
}

