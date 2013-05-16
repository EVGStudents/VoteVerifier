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

import ch.bfh.univoteverifier.table.TabBackground;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JComponent;

/**
 * Changes the color of a components text as a background task.
 *
 * @author prinstin
 */
public class PanelColorChanger {

    JComponent fComponent;
    Color fOriginalColor;

    /*
     * Create a new PanelColorChanger with the component for which to change text color.
     */
    public PanelColorChanger(JComponent aComponent) {

        fComponent = aComponent;
        fOriginalColor = aComponent.getBackground();

    }

    /**
     * Start the thread.
     */
    public void start() {

        Thread thread = new Thread(new Worker());
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Run a color changer class in the background.
     */
    private class Worker implements Runnable {

        ColorCalculator cc = new ColorCalculator(fOriginalColor, Color.RED);

        public void run() {
            for (int i = 0; i < 1000; i++) {
                try {

                    EventQueue.invokeLater(new ChangeColor(cc.nextColor()));
                    Thread.sleep(500);

                } catch (InterruptedException ex) {
                }
            }
        }
    }

    /**
     * Changes the color of a component's text.
     */
    private class ChangeColor implements Runnable {

        Color c;

        public ChangeColor(Color c) {
            this.c = c;
        }

        @Override
        public void run() {
            ((TabBackground) fComponent).setFontColor(c);
        }
    }

    /**
     * This class calculates a new color within a certain range to create a fade
     * effect.
     */
    private class ColorCalculator {

        int stepR, stepG, stepB;
        int cbR, cbG, cbB, ceR, ceG, ceB;
        int crntR, crntG, crntB;
        int allHere = 0;
        Color[] colors = {fComponent.getForeground(), Color.RED};
        int index = 0;

        /**
         * Create a new ColorCalculator
         *
         * @param cBegin the color at the beginning.
         * @param cEnd the color at the end.
         */
        public ColorCalculator(Color cBegin, Color cEnd) {

            this.cbR = cBegin.getRed();
            this.cbG = cBegin.getGreen();
            this.cbB = cBegin.getBlue();

            this.ceR = cEnd.getRed();
            this.ceG = cEnd.getGreen();
            this.ceB = cEnd.getBlue();

            crntR = cbR;
            crntG = cbG;
            crntB = cbB;

            stepR = calculateStep(cbR, ceR);
            stepG = calculateStep(cbG, ceG);
            stepB = calculateStep(cbB, ceB);
        }

        /**
         * Calculate the interval between the colors. How much the color should
         * change at each round.
         *
         * @param cb
         * @param ce
         * @return
         */
        public int calculateStep(int cb, int ce) {
            int diff = cb - ce;
//            int step = -1 * (diff / 5);
//            System.out.println("step: " + step);
            int step = 7;
            if (diff > 0) {
                step *= -1;
            }
            return step;
        }

        /**
         * Get the next color to be displayed.
         *
         * @return A new color.
         */
        public Color nextColor() {
            int newR = nextColorComponent(crntR, stepR, cbR, ceR);
            int newG = nextColorComponent(crntG, stepG, cbG, ceG);
            int newB = nextColorComponent(crntB, stepB, cbB, ceB);
            allHere = 0;
            crntR = newR;
            crntG = newG;
            crntB = newB;
            Color newColor = new Color(newR, newG, newB);
            return colors[index++ % 2];
        }

        /**
         * Get the next level of a given color component.
         *
         * @param crnt current level of the component.
         * @param step The level to change the component value.
         * @param begin The intitial level of the color component.
         * @param end The final limit of the color component.
         * @return The new level of the color component.
         */
        public int nextColorComponent(int crnt, int step, int begin, int end) {

            int larger, smaller, direction;
            //which limit is the larger one
            if (begin > end) {
                larger = begin;
                smaller = end;
            } else {
                larger = end;
                smaller = begin;
            }
            //to which limit are we going
            if (step < 0) {
                direction = smaller;
            } else {
                direction = larger;
            }


            //make sure we are not out of bounds
            //if the difference between the current position and direction is less than step, change directions
            if (Math.abs(crnt - direction) < Math.abs(step)) {

//                step *= -1;
                allHere++;
                allHere %= 3;
                if (allHere == 0) {
                    swapSteps();
                }
                return crnt;
            }
            allHere--;
            return crnt += step;
        }

        public void swapSteps() {
            stepR *= -1;
            stepG *= -1;
            stepB *= -1;
        }
    }
}
