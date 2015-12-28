/*
 **********************************************************
 * Software: common library 
 * 
 * Module: robin protocol FSM class 
 * 
 * Version: 0.1
 * 
 * Licence: GPL2
 *
 * Owner: Kim Kristo 
 * 
 * Date creation : 31.3.2011
 *
 **********************************************************
 */
package oh3ebf.lib.common.protocols;


public interface robinFsmInterface {
    
    /** protype for reguesting product info
     * 
     * @return robin message object
     *
     */
    
    public abstract robinMessage productInfoRequest();
    
    /** prototype for command
     * 
     * @param msg robin message object
     * @return robin message object
     *
     */
    
    public abstract robinMessage command(robinMessage msg);
    
    /** prototype for user message 1
     * 
     * @param msg robin message object
     * @return operation status
     */
    
    public abstract boolean userMessage1(robinMessage msg);
    
    /** prototype for user message 2
     * 
     * @param msg robin message object
     * @return operation status
     */
    
    public abstract boolean userMessage2(robinMessage msg);
    
    /** protype for ACK received action
     *
     */
    
    public abstract void ackReceived();
    
    /** protype for NACK received action
     *
     */
    
    public abstract void nackReceived();
    
    /** protype for message received action
     *
     */
    
    public abstract void messageEvent();
}
