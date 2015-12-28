/*
 **********************************************************
 * Software: common library
 *
 * Module: parameter manager class
 *
 * Version: 0.1
 *
 * Licence: GPL2
 *
 * Owner: Kim Kristo
 *
 * Date creation : 11.4.2013
 *
 **********************************************************
 */
package oh3ebf.lib.common.utilities;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class TimeStamp {

    /**
     * Function returns system time stamp in milliseconds
     *
     * @return timestamp as string
     */
    public static String getSysTimeStamp() {
        return (Long.toString(System.currentTimeMillis()));
    }

    /**
     * Function returns system time stamp in seconds
     *
     * @return timestamp as string
     */
    public static String getSysTimeSecondsStamp() {
        return (Long.toString(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
    }

    /**
     * Function returns human readable time stamp in date time format
     *
     * @return timestamp as string
     */
    public static String getDateTimeStamp() {
        java.util.Date date = new java.util.Date();

        return (new Timestamp(date.getTime()).toString());
    }
}
