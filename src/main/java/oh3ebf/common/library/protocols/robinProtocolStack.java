/**
 * Software: common library
 * Module: robin protocol stack class
 * Version: 0.1
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 27.1.2011
 */
package oh3ebf.common.library.protocols;

import java.io.*;

public class robinProtocolStack extends protocolStackBase {

    // receive state machine states 
    private static final int MSG_STATE_START = 0;
    private static final int MSG_STATE_LEADIN = 1;
    private static final int MSG_STATE_ADDRESS = 2;
    private static final int MSG_STATE_SRC = 3;
    private static final int MSG_STATE_FLAGS = 4;
    private static final int MSG_STATE_LEN = 5;
    private static final int MSG_STATE_DATA = 6;
    private static final int MSG_STATE_CKSUM = 7;

    private int state;
    private int packetLen;
    private int rxIndex = 0;
    private int rxError = 0;

    private boolean addrValid = false;
    private boolean promiscous = false;
    private byte ownAddr;
    private byte srcAddr;
    private byte flags;
    private robinMessage msg;
    private volatile boolean msgReady = false;

    private boolean ackRequested = false;
    private boolean ackReceived = false;
    private boolean nackReceived = false;

    private static final int TX_STATE_IDLE = 0;
    private static final int TX_STATE_SEND = 1;
    private static final int TX_STATE_WAIT_ACK = 2;
    private static final int TX_STATE_ERROR = 3;

    private robinMessage txMsg;
    private int txState;
    private int txResend;
    private byte subMsgIndex = 0;
    private boolean replyRequest;
    private OutputStream out;

    private robinDataInterface messageEvent;
    private robinFsmInterface fsmEvent;

    /**
     * Creates a new instance of robinProtocolStack
     */
    public robinProtocolStack() {
    }

    /**
     * Function sets output stream used to send data
     *
     * @param o output straem
     *
     */
    public void setOut(OutputStream o) {
        out = o;
    }

    /**
     * Function returns protocol stack listening address in use
     *
     * @return address byte
     *
     */
    public byte getOwnAddr() {
        return ownAddr;
    }

    /**
     * Function sets own address to protocol stack
     *
     * @param ownAddr protocol stack listening address
     *
     */
    public void setOwnAddr(byte ownAddr) {
        this.ownAddr = ownAddr;
    }

    /**
     * Function return current status of promiscous mode feature
     *
     * @return true when mode is in use otherwise false
     *
     */
    public boolean isPromiscous() {
        return promiscous;
    }

    /**
     * Function sets receiving stack in promiscous mode (all messages are
     * received)
     *
     * @param promiscous boolean state used to activate feature
     *
     */
    public void setPromiscous(boolean promiscous) {
        this.promiscous = promiscous;
    }

    /**
     * Function return message available status
     *
     * @return true when message is received, false no message available
     *
     */
    public boolean isAvailable() {
        return (msgReady);
    }

    /**
     * Function returns last received message
     *
     * @return received message
     *
     */
    public robinMessage getMessage() {
        return (msg);
    }

    /**
     * Function returns ACK received status
     *
     * @return true when NACK received
     *
     */
    public boolean isAckReceived() {
        return ackReceived;
    }

    /**
     * Function returns NACK received status
     *
     * @return true when NACK received
     *
     */
    public boolean isNackReceived() {
        return nackReceived;
    }

    /**
     * Function sets callback interface for message receiving
     *
     * @param e reference to interface to use
     *
     * @return true if event added, otherwise false
     */
    public boolean addMessageEvent(robinDataInterface e) {
        if (messageEvent == null) {
            messageEvent = e;
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Function returns sets current callback interface for message handling
     * state machine
     *
     * @return reference to interface in use
     *
     */
    public robinFsmInterface getFsmEvent() {
        return fsmEvent;
    }

    /**
     * Function sets callback interface for message handling state machine
     *
     * @param fsmEvent reference to interface to use
     *
     */
    public void setFsmEvent(robinFsmInterface fsmEvent) {
        this.fsmEvent = fsmEvent;
    }

    /**
     * Function is used to send messages to target
     *
     * @param m message to send
     *
     */
    public void sendData(robinMessage m) {
        // start sending engine
        txMsg = m;
        subMsgIndex = 0;
        txResend = 0;
        txState = TX_STATE_SEND;
    }

    /**
     * Function implements upper level state machine for received message
     * handling
     *
     *
     */
    public void fsm() {
        replyRequest = false;
        ackRequested = false;
        ackReceived = false;
        nackReceived = false;
        robinMessage response = null;

        // get data if available
        if (msgReady) {
            // mark message handled
            msgReady = false;

            switch (msg.getFlags()) {
                case robinMessage.REQACK | robinMessage.REQID:
                    ackRequested = true;
                case robinMessage.REQID:
                    // send product information if available                    
                    if (fsmEvent != null) {
                        response = fsmEvent.productInfoRequest();
                    } else {
                        // nack pitäisi lähteä tästä....
                    }

                    replyRequest = true;
                    break;
                case robinMessage.COMMAND | robinMessage.REQACK:
                    ackRequested = true;
                case robinMessage.COMMAND:
                    // command message handler
                    if (fsmEvent != null) {
                        response = fsmEvent.command(msg);
                    }

                    // palauttaa arvon mutta minkä???
                    break;
                case robinMessage.SEGMENTED | robinMessage.REQACK:
                    ackRequested = true;
                case robinMessage.SEGMENTED:
                    // tässäpä onkin pohdittavaa...

                    // kootaanko paketti tässä ja sitten annetaan eteenpäin vai kuinka???
                    break;
                case robinMessage.USER1 | robinMessage.REQACK:
                    ackRequested = true;
                case robinMessage.USER1:
                    // user defined message handler
                    if (fsmEvent != null) {
                        fsmEvent.userMessage1(msg);
                    }
                    break;
                case robinMessage.USER2 | robinMessage.REQACK:
                    ackRequested = true;
                case robinMessage.USER2:
                    // user defined message handler
                    if (fsmEvent != null) {
                        fsmEvent.userMessage2(msg);
                    }
                    break;
                case robinMessage.REQACK:
                    ackRequested = true;
                    break;
                case robinMessage.ACK:
                    // got ACK response message
                    ackReceived = true;

                    // ack message handler
                    if (fsmEvent != null) {
                        fsmEvent.ackReceived();
                    }

                    if (txState == TX_STATE_WAIT_ACK) // got ack, send next part
                    {
                        txState = TX_STATE_SEND;
                    }
                    break;
                case robinMessage.NACK:
                    // got NACK response message
                    nackReceived = true;

                    // nack message handler
                    if (fsmEvent != null) {
                        fsmEvent.nackReceived();
                    }

                    // check if tx engine is waiting for ack
                    if (txState == TX_STATE_WAIT_ACK) {
                        // resend packet counter
                        txResend++;
                        if (txResend == 10) {
                            // max resend coun reached
                            txState = TX_STATE_ERROR;
                        } else {
                            // send again
                            txState = TX_STATE_SEND;
                        }
                    }
                    break;
                default:
                    break;
            }

            if (replyRequest) {
                if (response == null) {
                    // create NACK message and send it
                    response = new robinMessage(0);
                    response.setTargetAddr(msg.getSourceAddr());
                    response.setSourceAddr(msg.getTargetAddr());
                    response.setFlags(robinMessage.NACK);
                    response.setLength((byte) 0);
                    tx(response.getMsg());
                } else {
                    if (ackRequested) // if needed set ack flag
                    {
                        response.setFlags((byte) (response.getFlags() | robinMessage.ACK));
                    }
                    // send response message
                    tx(response.getMsg());
                }
            } else {

                // ACK / RESPONSE is based on receive packet error status info
                if (ackRequested) {
                    // create ACK message and send it
                    response = new robinMessage(0);
                    response.setTargetAddr(msg.getSourceAddr());
                    response.setSourceAddr(msg.getTargetAddr());
                    response.setFlags(robinMessage.ACK);
                    response.setLength((byte) 0);
                    tx(response.getMsg());

                } else {
                    if (getRxError() != 0) {
                        // create NACK message and send it
                        response = new robinMessage(0);
                        response.setTargetAddr(msg.getSourceAddr());
                        response.setSourceAddr(msg.getTargetAddr());
                        response.setFlags(robinMessage.NACK);
                        response.setLength((byte) 0);
                        tx(response.getMsg());
                    }
                }
            }

            // callmessage handler
            if (fsmEvent != null) {
                fsmEvent.messageEvent();
            }
        }
    }

    /**
     * Function implements sending state machine
     *
     *
     */
    public void txFSM() {
        robinMessage r = null;

        switch (txState) {
            case TX_STATE_IDLE:
                break;
            case TX_STATE_SEND:
                // tässä pitää laskea pakettien määrä ja pyytää alipaketti...
                if (txMsg.getLength() > robinMessage.MSG_MAX_PLEN) {
                    // get next part of message
                    r = txMsg.getSubMessage(subMsgIndex++);

                    if (r == null) {
                        // back to waiting next message to send
                        subMsgIndex = 0;
                        txResend = 0;
                        txState = TX_STATE_IDLE;
                        txMsg = null;
                        break;
                    } else {
                        // send sub message
                        tx(r.getMsg());
                    }

                    if ((txMsg.getFlags() & robinMessage.REQACK) == robinMessage.REQACK) {
                        // go to wait reply packet
                        txState = TX_STATE_WAIT_ACK;
                    }
                } else {
                    // send message
                    tx(txMsg.getMsg());
                    txState = TX_STATE_IDLE;
                }
                break;
            case TX_STATE_WAIT_ACK:
                break;
            case TX_STATE_ERROR:
                break;
        }
    }

    /**
     * Function implements routine to send data from byte array
     *
     * @param tmp array to send
     *
     */
    private void tx(byte[] data) {
        int i = 0;

        if (data != null) {
            // write bytes to output stream
            while (i < data.length) {
                try {
                    out.write(data[i++]);
                } catch (IOException e) {

                }
            }
        } else {
            // tähän poikkeus
        }
    }

    /**
     * Function implements message receiving state machine
     *
     * @param c received character
     *
     */
    @Override
    public void receiveByte(byte c) {

        switch (state) {
            case MSG_STATE_START:
                // start state - 1 or more start bytes
                if (c == robinMessage.MSG_START_BYTE) {
                    state = MSG_STATE_LEADIN;
                }
                break;
            case MSG_STATE_LEADIN:
                // start state - 1 leadin byte
                if (c == robinMessage.MSG_LEADING_BYTE) // next byte is the address
                {
                    state = MSG_STATE_ADDRESS;
                } else if (c != robinMessage.MSG_START_BYTE) {
                    state = MSG_STATE_START;
                }
                break;
            case MSG_STATE_ADDRESS:
                // expecting an address packet
                if ((ownAddr == c) || promiscous) {
                    // clear packet variables
                    clrRxBuffer();
                    setReceived(false);

                    // packet is targetet for us
                    addrValid = true;

                    /* destination address to buffer*/
                    addRxBuffer(c, false);
                }

                state = MSG_STATE_SRC;
                break;
            case MSG_STATE_SRC:
                // expecting sender address
                srcAddr = c;
                if (addrValid || promiscous) {
                    // source address
                    addRxBuffer(c, false);
                }

                state = MSG_STATE_FLAGS;
                break;
            case MSG_STATE_FLAGS:
                // expecting packet flags
                if (addrValid || promiscous) {
                    // message flags
                    addRxBuffer(c, false);
                    flags = c;
                }

                state = MSG_STATE_LEN;
                break;
            case MSG_STATE_LEN:
                if (addrValid || promiscous) {
                    // expecting data length
                    addRxBuffer(c, false);

                    packetLen = c;
                    if (packetLen == 0) {
                        // zero length packet, skip data state
                        state = MSG_STATE_CKSUM;
                    } else {
                        // packet has 'packetLen' bytes of data to follow + a cksum
                        if (packetLen + 4 > robinMessage.MSG_MAX_PLEN) {
                            /* packet too long for use */
                            state = MSG_STATE_START;
                            addrValid = false;
                            break;
                        }

                        setBufferSize(packetLen);
                        state = MSG_STATE_DATA;
                    }
                }
                break;
            case MSG_STATE_DATA:
                if (addrValid || promiscous) {
                    // expecting 'packetLen' data bytes
                    addRxBuffer(c, true);
                    System.out.print((c & 0x000000ff) + " ");
                    if (--packetLen == 0) {
                        // that was the last of the data
                        state = MSG_STATE_CKSUM;
                    }
                }
                break;
            case MSG_STATE_CKSUM:
                System.out.println();
                // expecting a checksum
                //addRxBuffer(c);
                if (addrValid || promiscous) {
                    // compare checksums
                    if ((getCrc() & 0x000000ff) != (c & 0x000000ff)) {
                        setRxError(1);
                        System.out.println("CRC virhe: laskettu " + (getCrc() & 0xFF) + " != vastaanotettu " + (byte) c);
                    }

                    // encapsulate received data
                    msg = new robinMessage(getBufferSize());
                    msg.setSourceAddr(srcAddr);
                    msg.setTargetAddr(ownAddr);
                    msg.setFlags(flags);
                    msg.setLength((byte) getBufferSize());
                    msg.setMsgData(getRxBuffer());
                    // TODO crc is zero ???
                    msgReady = true;

                    // tell listening clients about received data
                    if (messageEvent != null) {
                        messageEvent.robinMessageEvent(msg);
                    }
                }
                // start over waiting for a new packet
                state = MSG_STATE_START;
                addrValid = false;
                break;
            default:
                /* unknown state - state machine error */
                state = MSG_STATE_START;
                addrValid = false;
                break;
        }
    }
}
