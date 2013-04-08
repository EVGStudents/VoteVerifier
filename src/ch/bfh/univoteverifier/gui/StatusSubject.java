/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.gui;

/**
 *
 * @author prinstin
 */
public interface StatusSubject {

    public void addListener(StatusListener wl);

    public void removeListener(StatusListener wl);

    public void notifyListeners(StatusEvent se);
}
