/*
 * *********************************************************
 * Software: common library 
 *
 * Module: crc base class 
 *
 * Version: 0.1 
 *
 * Licence: GPL2
 *
 * Owner: Kim Kristo Date creation : 31.1.2011
 *
 **********************************************************
 */
package oh3ebf.lib.common.crc;

import java.util.*;

public class crcBase {

    /**
     * Creates a new instance of crcBase
     *
     */
    public crcBase() {
    }

    /**
     * Function adds byte to checksum
     *
     * @param c byte to put in checksum
     *
     */
    public void addCrc(byte c) {

    }

    /**
     * Function gets current check sum value
     *
     * @return value of checksum
     *
     */
    public int getChecksum() {
        return (0);
    }

    /**
     * Function sets new value to checksum
     *
     * @param cecksum new value to set
     *
     */
    public void setChecksum(int cecksum) {
    }

    /**
     * Function calculates checksum for given byte array.
     *
     * @param data array of bytes for which the checksum is to be calculated
     *
     * @return byte containing the checksum
     *
     */
    public byte crcSum(final byte[] data) {
        return (0);
    }

    /**
     * Function calculates the checksum.
     *
     * @param data Vector of bytes for which the checksum is to be calculated
     *
     * @return byte containing the checksum
     *
     */
    public byte crcSum(final Vector<Byte> data) {
        return (0);
    }
}
