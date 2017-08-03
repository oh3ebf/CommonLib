/**
 * Software: common library
 * Module: data message base class
 * Version: 0.1
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 2.2.2011
 */
package oh3ebf.lib.common.protocols;

import oh3ebf.lib.common.crc.crcBase;

public class dataMessageBase {

    private byte[] msg;
    private boolean valid = false;

    /**
     * Creates a new instance of dataObject
     */
    public dataMessageBase() {
    }

    /**
     * Creates a new instance of dataObject
     *
     * @param size of buffer
     *
     */
    public dataMessageBase(int size) {
        // allocate message buffer and set valid allocation flag true
        msg = new byte[size];
        valid = true;
    }

    /**
     * Function returns content of message buffer
     *
     * @return message in byte array format
     *
     */
    public byte[] getMsg() {
        return msg;
    }

    /**
     * Function sets content of message buffer
     *
     * @param msg message in byte array
     *
     */
    public void setMsg(byte[] msg) {
        this.msg = msg;
        valid = true;
    }

    /**
     * Function returns status of message
     *
     * @return true if buffer is allocated, false no allocated buffer
     *
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Function set message status
     *
     * @param valid new state of message buffer
     *
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Function sets content of single byte in buffer
     *
     * @param i buffer location
     * @param data new content of byte
     *
     * @return true on success
     *
     * @exception ArrayIndexOutOfBoundsException is thrown
     *
     */
    public boolean setByte(int i, byte data) throws ArrayIndexOutOfBoundsException {
        if ((msg != null) && (i < msg.length)) {
            msg[i] = data;
            return (true);
        } else {
            // new exception on failure
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Function gets content of single byte in buffer
     *
     * @param i buffer location
     *
     * @return data byte from buffer location
     *
     * @exception ArrayIndexOutOfBoundsException is thrown
     *
     */
    public byte getByte(int i) throws ArrayIndexOutOfBoundsException {
        if ((msg != null) && (i < msg.length)) {
            return (msg[i]);
        } else {
            // new exception on failure
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Function returns message buffer length
     *
     * @return buffer length
     *
     */
    public int length() {
        return (msg.length);
    }

    /**
     * Function sets message crc byte
     *
     * @param crc checksum object
     *
     */
    public void crcBuffer(crcBase crc) {
        byte tmp = crc.crcSum(msg);
        msg[msg.length - 1] = tmp;
    }
}
