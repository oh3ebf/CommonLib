/*
 **********************************************************
 * Software: common library 
 * 
 * Module: mySql connector class 
 * 
 * Version: 0.1 
 * 
 * Licence:GPL2
 *
 * Owner: Kim Kristo 
 * 
 * Date creation : 14.5.2014
 *
 **********************************************************
 */

package oh3ebf.lib.common.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import oh3ebf.lib.common.exceptions.genericException;

public class mySqlConnectorBase {

    protected Connection con;

    /**
     * Creates a new instance of dbConnection
     *
     */
    public mySqlConnectorBase() {

    }

    /**
     * Function open database connection
     *
     * @param dbServer server ip address
     * @param dbUser database user name
     * @param dbPass database password
     * @param dbName database name
     *
     * @return connection status
     * @throws lib.common.exceptions.genericException
     */
    public boolean openConnection(String dbServer, String dbUser, String dbPass, String dbName) throws genericException {
        boolean result = false;

        try {
            // declare database driver
            Class.forName("com.mysql.jdbc.Driver");

            // create new connection
            DriverManager.setLoginTimeout(5);
            con = DriverManager.getConnection("jdbc:mysql://" + dbServer + "/" + dbName + "?user=" + dbUser + "&password=" + dbPass);

            // connection made successfully
            result = true;
        } catch (Exception e) {
            throw new genericException(1, "dbConnection", "openConnection", "Cannot open connection to server:" + dbServer, null);
        }

        return (result);
    }

    /**
     * Function close database connection
     *
     *
     * @throws lib.common.exceptions.genericException
     */
    public void closeConnection() throws genericException {
        try {
            // any open connections
            if (con != null) {
                // close connection
                con.close();
            }
        } catch (Exception e) {
            throw new genericException(1, "dbConnection", "closeConnection", e.getMessage(), null);
        }
    }

    /**
     * Function converts local date to sql date format
     *
     * @param date local date string
     *
     * @return date in sql format
     *
     */
    public String getSqlDate(String date) {
        // convert dates to SQL format
        String[] tmp = date.split("\\.");
        if (tmp[0].length() == 1) {
            tmp[0] = "0" + tmp[0].trim();
        }

        if (tmp[1].length() == 1) {
            tmp[1] = "0" + tmp[1].trim();
        }

        tmp[2] = tmp[2].trim();

        return (tmp[2] + "-" + tmp[1] + "-" + tmp[0]);
    }

    /**
     * Function converts sql date to local date format
     *
     * @param date sql date string
     *
     * @return date in normal format
     *
     */
    public String getNormalDate(String date) {
        // convert dates to normal format
        String[] tmp = date.split("-");

        // convert dd.mm.yyyy format
        return (tmp[2] + "." + tmp[1] + "." + tmp[0]);
    }
}
