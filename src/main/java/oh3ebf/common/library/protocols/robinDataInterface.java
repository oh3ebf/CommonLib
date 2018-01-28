/**
 * Software: common library
 * Module: robin protocol data interface class
 * Version: 0.1
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 26.3.2011
 */
package oh3ebf.common.library.protocols;

public interface robinDataInterface {

    //public boolean isAvailable();           
    //public robinMessage getMessage();    
    /**
     * Prototype for event object
     *
     * @param m reference to robin message
     * 
     */
    public void robinMessageEvent(robinMessage m);

}
