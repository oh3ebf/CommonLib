/*
 **********************************************************
 * Software: common library
 *
 * Module: parameter manager class
 *
 * Version: 0.1
 *
 * Licence: GPL2
 *
 * Owner: Kim Kristo
 *
 * Date creation : 7.1.2011
 *
 **********************************************************
 */
package oh3ebf.lib.common.utilities;

import java.io.*;
import java.util.*;
import oh3ebf.lib.common.exceptions.genericException;

public class ParameterManager {

    private Hashtable<String, String> params;
    private String paramPath;
    private String paramFile;

    /**
     * Creates a new instance of parameterManager
     *
     * @exception genericException thrown when reading parameters failed
     *
     */
    public ParameterManager() throws genericException {
        params = new Hashtable<String, String>();
        String tmp = null;

        // get config file path if defined
        if ((tmp = System.getenv("PARAM_CONFIG")) == null) {
            // no path found, look at current directory
            paramPath = new String(".");
        }

        // chek if path is included with file name
        if (tmp.contains("/")) {
            // get full path name
            paramPath = tmp.substring(0, tmp.lastIndexOf("/") + 1);
            // get file name
            paramFile = tmp.substring(tmp.lastIndexOf("/") + 1, tmp.length());
        } else {
            // set current directory as path
            paramPath = "./";
            // only filename defined
            paramFile = tmp;
        }

        try {
            // read parameters from file
            readParameters();
        } catch (Exception e) {
            // handle exceptions genereted from this method
            throw new genericException(2, "ParameterManager", "ParameterManager", e.getMessage(), null);
        }
    }

    /**
     * Creates a new instance of parameterManager with existing parameters
     *
     * @param path to parater file location
     * @param file parameter file name
     *
     * @throws lib.common.exceptions.genericException thrown when reading failed
     *
     */
    public ParameterManager(String path, String file) throws genericException {
        params = new Hashtable<String, String>();

        // get config file path if defined
        paramPath = path;

        // config file path
        paramFile = file;

        try {
            // read parameters from file
            readParameters();
        } catch (Exception e) {
            // handle exceptions genereted from this method
            throw new genericException(2, "ParameterManager", "ParameterManager", e.getMessage(), null);
        }
    }

    /**
     * Function reads parameters from file
     *
     * @throws lib.common.exceptions.genericException thrown when reading failed
     *
     */
    public void readParameters() throws genericException {
        String str = null;

        try {
            // open file if exist
            BufferedReader br = new BufferedReader(new FileReader(paramPath + paramFile));

            // read line from file
            while ((str = br.readLine()) != null) {

                String[] tmp = str.split("=");
                if (tmp != null) {
                    // jos toinen puuttuu...
                    if (tmp.length == 2) {
                        params.put(tmp[0], tmp[1]);
                    } else {
                        params.put(tmp[0], "");
                    }
                }
            }

            // close file
            br.close();

        } catch (IOException e) {
            // handle exceptions genereted from this method
            throw new genericException(1, "ParameterManager", "readParameters", e.getMessage(), null);
        }
    }

    /**
     * Function writes parameters to config file
     *
     * @throws lib.common.exceptions.genericException thrown when saving failed
     *
     */
    public void saveParameters() throws genericException {
        String tmp = null;
        int point = 0;
        try {
            // check if name contains point
            point = paramFile.lastIndexOf('.');

            if (point > 0) {
                // point found remove tail
                tmp = paramFile.substring(0, point);
            } else {
                // filame has not point
                tmp = paramFile;
            }

            // create backup file
            File f = new File(paramPath + paramFile);
            f.renameTo(new File(paramPath + tmp + ".bak"));

            // open file if exist
            BufferedWriter bw = new BufferedWriter(new FileWriter(paramPath + paramFile));

            // table keys
            Enumeration<String> ee = params.keys();

            // loop all table items
            while (ee.hasMoreElements()) {
                tmp = ee.nextElement();
                if (!tmp.isEmpty()) {
                    // save parameters to file
                    bw.write(tmp + "=" + params.get(tmp));
                    bw.newLine();
                }
            }

            //close file
            bw.flush();
            bw.close();

        } catch (IOException e) {
            // handle exceptions genereted from this method
            throw new genericException(1, "ParameterManager", "saveParameters", e.getMessage(), null);
        }
    }

    /**
     * Function returns single parameter
     *
     * @param parameter name of parameter
     *
     * @return parameter value if parameter is found, null when parameter not
     * found
     *
     */
    public String getParameter(String parameter) {
        if (params.containsKey(parameter)) {
            return (params.get(parameter));
        }

        return (null);
    }

    /**
     * Function sets single parameter value
     *
     * @param parameter name of parameter
     * @param value new value of parameter
     *
     * @return trueif parameter is found otherwise false
     *
     */
    public boolean setParameter(String parameter, String value) {
        if (params.containsKey(parameter)) {
            params.put(parameter, value);
            return (true);
        }

        return (false);
    }

    /**
     * Function adds new parameter value pair
     *
     * @param parameter name of parameter
     * @param value new value of parameter
     *
     * @return true if parameter added, otherwise false
     *
     */
    public boolean createParameter(String parameter, String value) {
        if (!params.containsKey(parameter)) {
            params.put(parameter, value);
            return (true);
        }

        return (false);
    }
}
