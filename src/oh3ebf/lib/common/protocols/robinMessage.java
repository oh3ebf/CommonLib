/*
 **********************************************************
 * Software: common library 
 * 
 * Module: robin protocol message class 
 * 
 * Version: 0.1
 * 
 * Licence: GPL2
 *
 * Owner: Kim Kristo 
 * 
 * Date creation : 2.2.2011
 *
 **********************************************************
 */
package oh3ebf.lib.common.protocols;

import oh3ebf.lib.common.crc.crcBase;

public class robinMessage extends dataMessageBase {

    public static final byte MSG_OVERHEAD = 5;          // 5 bytes of overhead for each packet
    public static final byte MSG_MAX_PLEN = 64;         // max packet length, give a data length of 59
    public static final byte MSG_MAX_DLEN = (MSG_MAX_PLEN - MSG_OVERHEAD); // max packet data length

    public static final byte MSG_N_LEADING = 4;
    public static final byte MSG_LEADING_BYTE = (byte) 0x99;
    public static final byte MSG_START_BYTE = (byte) 0xAA;

    public static final byte ACK = 0x01;
    public static final byte NACK = 0x02;
    public static final byte REQACK = 0x04;
    public static final byte REQID = 0x08;
    public static final byte COMMAND = 0x10;
    public static final byte SEGMENTED = 0x20;
    public static final byte USER1 = 0x40;
    public static final byte USER2 = (byte) 0x80;

    public static final byte PARAM_STATIC = 0x00;
    public static final byte PARAM_VARIABLE = 0x01;

    public static final byte CMD_SET_NODEID = 0x01;     // set node ID
    public static final byte CMD_SET_MODE = 0x02;       // set 8 or 9 bit mode
    public static final byte CMD_SET_BAUDRATE = 0x03;   // set baudrate
    public static final byte CMD_SET_DATETIME = 0x04;   // set date an time
    public static final byte CMD_SAVE_PARAM = 0x05;     // save parameters to nonvolatile storage
    public static final byte CMD_LOAD_PARAM = 0x06;     // load parameters from nonvolatile storage
    public static final byte CMD_READ_PARAM = 0x07;     // read parameters from target
    public static final byte CMD_WRITE_PARAM = 0x08;    // write parameters to target

    /**
     * Creates a new instance of robinMessage
     *
     * @param i
     */
    public robinMessage(int i) {

        // init data structure
        super(i + MSG_OVERHEAD + MSG_N_LEADING + 1);
        super.setByte(0, MSG_START_BYTE);
        super.setByte(1, MSG_START_BYTE);
        super.setByte(2, MSG_START_BYTE);
        super.setByte(3, MSG_START_BYTE);
        super.setByte(4, MSG_LEADING_BYTE);

        // set initialized length
        super.setByte(8, (byte) i);
    }

    /**
     * Function gets message target address
     *
     * @return target address
     *
     */
    public byte getTargetAddr() {
        return super.getByte(5);
    }

    /**
     * Function sets message target address
     *
     * @param targetAddr target address byte
     *
     */
    public void setTargetAddr(byte targetAddr) {
        super.setByte(5, targetAddr);
    }

    /**
     * Function gets message source address
     *
     * @return source address byte
     *
     */
    public byte getSourceAddr() {
        return super.getByte(6);
    }

    /**
     * Function sets message source address
     *
     * @param sourceAddr source address byte
     *
     */
    public void setSourceAddr(byte sourceAddr) {
        super.setByte(6, sourceAddr);
    }

    /**
     * Function gets message flag byte
     *
     * @return flag byte
     *
     */
    public byte getFlags() {
        return super.getByte(7);
    }

    /**
     * Function sets message flag byte
     *
     * @param flags byte containing flags
     *
     */
    public void setFlags(byte flags) {
        super.setByte(7, flags);
    }

    /**
     * Function gets message data length
     *
     * @return flag byte
     *
     */
    public byte getLength() {
        return super.getByte(8);
    }

    /**
     * Function sets message data length
     *
     * @param length byte containing length
     *
     */
    public void setLength(byte length) {
        super.setByte(8, length);
    }

    /**
     * Function gets data content of message buffer
     *
     * @return data in byte array
     *
     */
    public byte[] getMsgData() {
        int i = 0;

        byte[] data = super.getMsg();

        // set size to message data content length
        byte[] tmp = new byte[data.length - 10];

        for (i = 0; i < tmp.length; i++) {
            tmp[i] = data[i + 9];
        }

        return (tmp);
    }

    /**
     * Function sets content of message buffer
     *
     * @param msg data in byte array
     *
     */
    public void setMsgData(byte[] msg) {
        int i = 0;

        // copy data bytes to message buffer
        for (i = 0; i < msg.length; i++) {
            super.setByte(i + 9, msg[i]);
        }
    }

    /**
     * Function sets message crc byte
     *
     * @param crc checksum object
     *
     */
    @Override
    public void crcBuffer(crcBase crc) {
        int i = 0;
        byte[] data = super.getMsg();

        byte[] tmp = new byte[data.length - MSG_N_LEADING - 1];

        for (i = 0; i < tmp.length; i++) {
            tmp[i] = data[i + 5];
        }

        super.setByte(data.length - 1, crc.crcSum(tmp));
    }

    /* Function returns part from message starting from index
     *
     * @param index stating point of sub message
     *
     * @return sub message
     *
     */
    public robinMessage getSubMessage(byte index) {
        byte i = 0;

        if ((super.getByte(8) > MSG_MAX_DLEN) && ((index * MSG_MAX_DLEN) < super.getByte(8))) {
            // new message
            robinMessage tmp = new robinMessage(MSG_MAX_DLEN);

            // set parameters
            tmp.setTargetAddr(super.getByte(5));
            tmp.setSourceAddr(super.getByte(6));
            tmp.setFlags(super.getByte(7));

            try {
                // calculate packet length
                if (super.getByte(8) > ((index + 1) * MSG_MAX_DLEN)) {
                    // full packet
                    tmp.setLength(MSG_MAX_DLEN);

                    // copy data
                    for (i = 0; i < MSG_MAX_DLEN; i++) {
                        tmp.setByte(i, getByte(i + (index * MSG_MAX_DLEN)));
                    }
                } else {
                    // last bytes
                    tmp.setLength((byte) (super.getByte(8) - (index * MSG_MAX_DLEN)));

                    // copy data
                    for (i = 0; i < MSG_MAX_DLEN; i++) {
                        tmp.setByte(i, getByte(i + (index * MSG_MAX_DLEN)));
                    }
                }
            } catch (Exception e) {

            }

            return (tmp);
        } else {
            return (null);
        }
    }

    /* Function encodes data message to packet format
     *
     * @param data message to encode
     *
     *
     * @return byte array containing encoded message
     *
     */
    public byte[] encode(dataMessageBase data) {
        byte[] packet = new byte[(data.length() + 9)];
        int i = 0, j = 0;

        // check message length
        if (data.length() + 5 > MSG_MAX_PLEN) // TODO: olisiko poikkeuksen paikka??
        {
            return (null);
        }

        // packet header
        packet[i++] = MSG_START_BYTE;
        packet[i++] = MSG_START_BYTE;
        packet[i++] = MSG_START_BYTE;
        packet[i++] = MSG_LEADING_BYTE;

        // packet information part
        packet[i++] = ((robinMessage) data).getTargetAddr();
        packet[i++] = ((robinMessage) data).getSourceAddr();
        packet[i++] = ((robinMessage) data).getFlags();
        packet[i++] = (byte) (((robinMessage) data).length() & 0xFF);

        byte[] tmp = ((robinMessage) data).getMsg();

        // packet data part
        if (((robinMessage) data).length() > 0) {
            for (j = 0; j < ((robinMessage) data).length(); j++) {
                try {
                    packet[i++] = tmp[j];
                } catch (Exception e) {

                }
            }
        }

        //if(crcInUse) {
        // add checksum vähän liikaa tavuja mukana
        //packet[i++] = crc.crcSum(packet);
        //}
        return (packet);
    }
}
