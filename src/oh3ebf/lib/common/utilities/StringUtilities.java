/**
 * Software: common library
 * Module: String utilities class
 * Version: 0.1
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 3.8.2017
 */

package oh3ebf.lib.common.utilities;

public class StringUtilities {

    /**
     * Function converts integer value to its alphabetic equivalent (A,B,C...AA,AB,AC...)
     * 
     * @param i number to covert
     * @return  alphabetic presentation
     * 
     */
    public static String toAlphabetic(int i) {
        if (i < 0) {
            return "-" + toAlphabetic(-i - 1);
        }

        int quot = i / 26;
        int rem = i % 26;
        char letter = (char) ((int) 'A' + rem);
        if (quot == 0) {
            return "" + letter;
        } else {
            return toAlphabetic(quot - 1) + letter;
        }
    }
}
