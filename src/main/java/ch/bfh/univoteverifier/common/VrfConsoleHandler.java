/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.univoteverifier.common;

import java.util.logging.ConsoleHandler;

/**
 *
 * @author prinstin
 */
public class VrfConsoleHandler extends ConsoleHandler {

    public VrfConsoleHandler() {
        setOutputStream(System.out);
    }
}
