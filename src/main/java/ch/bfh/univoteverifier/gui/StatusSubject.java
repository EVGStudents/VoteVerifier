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

/**
 * makes up part of the observer patter
 * @author prinstin
 */
public interface StatusSubject {

    /**
     * add a listener to this Subject
     * @param sl a listener to be registered with this subject
     */
    public void addListener(StatusListener sl);

    /**
     * remove a listener from the subject
     * @param sl the listener to be removed from the list
     */
    public void removeListener(StatusListener sl);

    /**
     * sends the status event to all the listeners that have been registered with this subject
     * @param se
     */
    public void notifyListeners(StatusEvent se);
}
