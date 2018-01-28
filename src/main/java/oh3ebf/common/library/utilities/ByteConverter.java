/**
 * Software: common library
 * Module: generic number converter class class
 * Version: 0.1
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 20.5.2013
 */
package oh3ebf.common.library.utilities;

public class ByteConverter {

    private boolean endianess = false; // default no conversion big endian

    public ByteConverter() {

    }

    public ByteConverter(boolean value) {
        endianess = value;
    }

    /* Function sets parser to use big or little endian format
     *
     * Parameters:
     * value: false = little endian, true = big endian
     *
     * Returns:
     *
     */
    public void setEndianess(boolean value) {
        endianess = value;
    }

    /* Function returns endianes used currently
     *
     * Parameters:
     * 
     *
     * Returns:
     * false = little endian, true = big endian
     *
     */
    public boolean getEndianess() {
        return (endianess);
    }
    /* Function returns string parsed from message 
     *
     * Parameters:
     * index: starting point to parsing
     * m: message where string is parsed from
     *
     * Returns:
     * parsed string
     *
     */

    public String getString(int index, byte[] data) {
        String tmp = new String(data).trim();

        if (tmp.indexOf(0x0) != -1) {
            // Cut from first '\0' character
            return (tmp.substring(0, tmp.indexOf(0)));
        } else {
            // no formatting needed
            return (tmp);
        }
    }

    /* Function returns single byte from message
     *
     * Parameters:
     * index: starting point to parsing
     * m: message where byte is parsed from
     *
     * Returns:
     * signed byte
     *
     */
    public byte getByte(int index, byte[] data) {
        byte tmp = 0;

        // chech array bounds
        if (index > data.length) {
            //logger.error("Accessing data out of message in getByte.");
            return (0);
        }

        if (endianess == true) {
            // littleendian to bigendian
            tmp = data[index];
        } else {
            // default bigendian
            tmp = data[index];
        }

        return (tmp);
    }

    /* Function returns short value parsed from message
     *
     * Parameters:
     * index: starting point to parsing
     * m: message where byte is parsed from
     *
     * Returns:
     * signed short
     *
     */
    public short getShort(int index, byte[] data) {
        short tmp = 0;

        // chech array bounds
        if (index + 1 >= data.length) {
            //logger.error("Accessing data out of message in getShort.");
            return (0);
        }

        if (endianess == true) {
            // littleendian to bigendian
            tmp = (short) ((0x000000FF & ((int) data[index])) << 8 | (0x000000FF & ((int) data[index + 1])));
        } else {
            // default bigendian
            tmp = (short) ((0x000000FF & ((int) data[index + 1])) << 8 | (0x000000FF & ((int) data[index])));
        }

        return (tmp);
    }

    /* Function returns integer value parsed from message 
     *
     * Parameters:
     * index: starting point to parsing
     * m: message where byte is parsed from 
     *
     * Returns:
     * signed integer
     *
     */
    public int getInt(int index, byte[] data) {
        int tmp = 0;

        // chech array bounds
        if (index + 3 >= data.length) {
            //logger.error("Accessing data out of message in getInt.");
            return (0);
        }

        if (endianess == true) {
            // littleendian to bigendian
            tmp = ((int) ((0x000000FF & ((int) data[index])) << 24
                    | (0x000000FF & ((int) data[index + 1])) << 16
                    | (0x000000FF & ((int) data[index + 2])) << 8
                    | (0x000000FF & ((int) data[index + 3]))))
                    & 0xFFFFFFFF;
        } else {
            // default bigendian
            tmp = ((int) ((0x000000FF & ((int) data[index + 3])) << 24
                    | (0x000000FF & ((int) data[index + 2])) << 16
                    | (0x000000FF & ((int) data[index + 1])) << 8
                    | (0x000000FF & ((int) data[index]))))
                    & 0xFFFFFFFF;
        }

        return (tmp);
    }

    /* Function return long value parsed from message
     *
     * Parameters:
     * index: starting point to parsing
     * m: message where byte is parsed from 
     *
     * Returns:
     * signed long
     *
     */
    public long getLong(int index, byte[] data) {
        long tmp = 0;

        // check array bounds
        if (index + 7 >= data.length) {
            //logger.error("Accessing data out of message in getLong.");
            return (0);
        }

        if (endianess == true) {
            // littleendian to bigendian
            tmp = ((long) ((0x000000FF & ((long) data[index])) << 56
                    | (0x000000FF & ((long) data[index + 1])) << 48
                    | (0x000000FF & ((long) data[index + 2])) << 40
                    | (0x000000FF & ((long) data[index + 3])) << 32
                    | (0x000000FF & ((long) data[index + 4])) << 24
                    | (0x000000FF & ((long) data[index + 5])) << 16
                    | (0x000000FF & ((long) data[index + 6])) << 8
                    | (0x000000FF & ((long) data[index + 7])))) & 0xFFFFFFFFFFFFFFFFL;
        } else {
            // default bigendian
            tmp = ((long) ((0x000000FF & ((long) data[index + 7])) << 56
                    | (0x000000FF & ((long) data[index + 6])) << 48
                    | (0x000000FF & ((long) data[index + 5])) << 40
                    | (0x000000FF & ((long) data[index + 4])) << 32
                    | (0x000000FF & ((long) data[index + 3])) << 24
                    | (0x000000FF & ((long) data[index + 2])) << 16
                    | (0x000000FF & ((long) data[index + 1])) << 8
                    | (0x000000FF & ((long) data[index])))) & 0xFFFFFFFFFFFFFFFFL;
        }

        return (tmp);
    }
}
