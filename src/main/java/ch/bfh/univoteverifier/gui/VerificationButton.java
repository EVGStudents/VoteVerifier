/*
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

import javax.swing.JButton;

/**
 * A Button type that contains a description and methods that changes its
 * appearance.
 *
 * @author prinstin
 */
public class VerificationButton extends JButton {

        String description;

        /**
         * Create an instance of this class.
         * @param name The name to give the instance of this Button Class.
         * @param description A description of the button.
         */
        public VerificationButton(String name, String description) {
            super(name);
            this.setBackground(GUIconstants.GREY);
            this.setFocusPainted(false);
        }

        /**
         * Called when another button is pressed set the color of the button to
         * light grey.
         */
        public void depress() {
            this.setBackground(GUIconstants.GREY);
        }

        /**
         * Called when the button is pressed set the color of the button to dark
         * grey.
         */
        public void press() {
            this.setBackground(GUIconstants.DARK_GREY);
        }
    }
