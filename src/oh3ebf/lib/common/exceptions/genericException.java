/**
 * Software: common library
 * Module: generic exception class
 * Version: 0.1
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 19.11.2010
 */
package oh3ebf.lib.common.exceptions;

public class genericException extends java.lang.Exception {

    private static final long serialVersionUID = 7526472295622776147L;

    private int id;                             // a unique id
    private String classname;                   // the name of the class
    private String method;                      // the name of the method
    private String message;                     // a detailed message
    private genericException previous = null;   // the exception which was caught
    private String separator = "\n";            // line separator

    /**
     * Creates a new instance of <code>genericException</code> without detail
     * message.
     *
     * @param id
     * @param classname
     * @param method
     * @param message
     * @param previous
     */
    public genericException(int id, String classname, String method, String message, genericException previous) {
        this.id = id;
        this.classname = classname;
        this.method = method;
        this.message = message;
        this.previous = previous;
    }

    /**
     * Constructs an instance of <code>genericException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public genericException(String msg) {
        super(msg);
    }

    public String traceBack() {
        return traceBack("\n");
    }

    public String traceBack(String sep) {
        this.separator = sep;
        int level = 0;
        genericException e = this;
        String text = line("Calling sequence (top to bottom)");
        while (e != null) {
            level++;
            text += line("--level " + level + "--------------------------------------");
            text += line("Class/Method: " + e.classname + "/" + e.method);
            text += line("Id          : " + e.id);
            text += line("Message     : " + e.message);
            e = e.previous;
        }
        return text;
    }

    private String line(String s) {
        return s + separator;
    }
}
