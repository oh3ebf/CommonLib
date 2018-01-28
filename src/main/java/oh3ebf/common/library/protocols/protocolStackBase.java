/**
 * Software: common library
 * Module: serial protocol stack base class
 * Version: 0.1
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 28.2.2011
 */
package oh3ebf.common.library.protocols;

import oh3ebf.common.library.crc.crcBase;

public class protocolStackBase {

    protected volatile boolean msgReceived = false;
    protected int packetLen;
    private int bufferSize;
    protected int rxIndex = 0;
    protected volatile int rxError = 0;

    //protected byte[] msgBuffer; // viimeisin ehjä paketti
    protected byte[] msgRxBuffer; // aktiivinen puskuri

    protected byte[] msgSendBuffer;
    protected crcBase crc = null;
    protected boolean crcInUse = false;

    /**
     * Creates a new instance of protocolStackBase
     */
    public protocolStackBase() {
        // Remember to set buffer size and clear buffer contects        
    }

    /**
     * Function prototype for message encode function
     *
     * @param data message to encode
     *
     */
    public byte[] encode(dataMessageBase data) {
        return (null);
    }

    /**
     * Function prototype for message decode function
     *
     * @param data message to decode
     *
     */
    public dataMessageBase decode(byte[] data) {
        return (null);
    }

    /**
     * Function adds signle byte to rx buffer
     *
     * @param c byte to store
     * @param store boolean flag for storing or calculating crc
     *
     * @return false index out of bounds, true: storing success
     *
     */
    protected boolean addRxBuffer(byte c, boolean store) {
        // byte is not stored to buffer
        if (store == false) {
            // add byte to crc if used
            if (crcInUse) {
                crc.addCrc(c);
            }
            return (true);
        } else {
            // check array bounds
            if (getRxIndex() < msgRxBuffer.length) {
                msgRxBuffer[rxIndex++] = c;
                // add byte to crc if used
                if (crcInUse) {
                    crc.addCrc(c);
                }
                return (true);
            } else {
                return (false);
            }
        }
    }

    /**
     * Function implements prototype of protocol stack
     *
     * @param c received character
     *
     */
    public void receiveByte(byte c) {

    }

    /**
     * Function returns rx buffer content in byte array
     *
     * @return buffer content in byte array
     *
     */
    public byte[] getRxBuffer() {
        return (msgRxBuffer);
    }

    /**
     * Function sets message received flag
     *
     * @param flag state of message received flag
     *
     */
    protected void setReceived(boolean flag) {
        msgReceived = flag;
    }

    /**
     * Function status of meesage received flag
     *
     * @return status of message received flag
     *
     */
    public boolean isReceived() {
        return (msgReceived);
    }

    /**
     * Function returns current value of rx error flag
     *
     * @return current error
     *
     */
    public int getRxError() {
        return rxError;
    }

    /**
     * Function sets rx error flag
     *
     * @param rxError value of error
     *
     */
    protected void setRxError(int rxError) {
        this.rxError = rxError;
    }

    /**
     * Function returns current value of rx buffer pointer
     *
     * @return value of buffer pointer
     *
     */
    protected int getRxIndex() {
        return rxIndex;
    }

    /**
     * Function sets rx buffer index pointer value
     *
     * @param rxIndex new value on buffer pointer
     *
     */
    protected void setRxIndex(int rxIndex) {
        this.rxIndex = rxIndex;
    }

    /**
     * Function returns current rx buffer size
     *
     * @return value of buffer size
     *
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * Function sets rx buffer size by allocation new buffer
     *
     * @param bufferSize new size of buffer
     *
     */
    protected void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        msgRxBuffer = new byte[bufferSize];
    }

    /**
     * Function clears rx buffer contents
     *
     *
     */
    public void clrRxBuffer() {
        // clear buffer
        msgRxBuffer = new byte[bufferSize];
        rxIndex = 0;
        rxError = 0;

        if ((crc != null) && crcInUse) {
            crc.setChecksum(0);
        }
    }

    /* Function return curren value of crc calculation
     *
     * @return crc value if used other wise -1 if no crc available
     *
     */
    public int getCrc() {
        if (crcInUse) {
            return (crc.getChecksum());
        } else {
            return (-1);
        }
    }

    /* Function activates crc usage in stack
     *
     * @param crc instance of crc to be used
     *
     */
    public void setCrc(crcBase crc) {
        this.crc = crc;
        crcInUse = true;
    }
}
