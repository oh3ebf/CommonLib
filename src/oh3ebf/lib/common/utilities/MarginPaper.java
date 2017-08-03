/**
 * Software: common library
 * Module: printing paper with margins class
 * Version: 0.1
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 2.1.2013
 */
package oh3ebf.lib.common.utilities;

import java.awt.print.Paper;

public class MarginPaper extends Paper {

    private double minX = 0, minY = 0, maxRX = 0, maxBY = 0;

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void setMinImageableArea(double x, double y, double w, double h) {
        minX = x;
        minY = y;
        maxRX = x + w;
        maxBY = y + h;
        super.setImageableArea(minX, minY, maxRX, maxBY);
    }

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void setImageableArea(double x, double y, double w, double h) {
        if (x < minX) {
            x = minX;
        }

        if (y < minY) {
            y = minY;
        }

        if (x + w > maxRX) {
            w = maxRX - x;
        }

        if (y + h > maxBY) {
            h = maxBY - y;
        }

        super.setImageableArea(x, y, w, h);
    }

    /**
     *
     * @return
     */
    public double getMinX() {
        return minX;
    }

    /**
     *
     * @return
     */
    public double getMinY() {
        return minY;
    }

    /**
     *
     * @return
     */
    public double getMaxRX() {
        return maxRX;
    }

    /**
     *
     * @return
     */
    public double getMaxBY() {
        return maxBY;
    }
}
