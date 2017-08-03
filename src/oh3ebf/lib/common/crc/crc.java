/**
 * Software: common library
 * Module: crc class
 * Version: 0.1
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 21.1.2011
 */
package oh3ebf.lib.common.crc;

import java.util.*;

public class crc extends crcBase {

    protected int checksum = 0;

    /**
     * Creates a new instance of crc
     *
     */
    public crc() {
    }

    /**
     * Function adds byte to crc sum
     *
     * @param c byte to add in crc sum
     *
     */
    @Override
    public void addCrc(byte c) {
        checksum ^= c;
    }

    /**
     * Function calculates checksum for given byte array.
     *
     * @param data array of bytes for which the checksum is to be calculated
     *
     * @return byte containing the checksum
     *
     */
    @Override
    public byte crcSum(final byte[] data) {
        byte crc = 0;

        // calculate simple cecksum
        for (int i = 0; i < data.length; i++) {
            crc ^= data[i];
        }

        return (crc);
    }

    /**
     * Function calculates the checksum.
     *
     * @param data Vector of bytes for which the checksum is to be calculated
     *
     * @return byte containing the checksum
     *
     */
    @Override
    public byte crcSum(final Vector<Byte> data) {
        byte crc = 0;

        // calculate simple cecksum
        for (int i = 0; i < data.size(); i++) {
            crc ^= (byte) data.get(i);
        }

        return (crc);
    }

    /**
     * Function gets checksum value
     *
     * @return crc byte
     *
     */
    public int getCecksum() {
        return (checksum);
    }

    /**
     * Function sets new value to checksum
     *
     * @param crc cecksum new value to set
     *
     */
    public void setCecksum(int crc) {
        this.checksum = crc;
    }
}
