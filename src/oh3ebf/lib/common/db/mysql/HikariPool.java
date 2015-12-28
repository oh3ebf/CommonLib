/*
 * *********************************************************
 * Software: common library 
 *
 * Module: Hikari connection pool class 
 *
 * Version: 0.1 
 *
 * Licence: GPL2
 *
 * Owner: Kim Kristo Date creation : 22.1.2015
 *
 **********************************************************
 */

package oh3ebf.lib.common.db.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariPool {

    private static HikariPool instance = null;
    private HikariDataSource ds = null;

    static {
        try {
            instance = new HikariPool();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private HikariPool() {
        HikariConfig config = new HikariConfig("Hikari.properties");
        /*config.setMaximumPoolSize(10);
        config.setJdbcUrl("jdbc:mysql://thorin:3306/datafeed");
        config.setUsername("datafeed");
        config.setPassword("redhat");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        */

        ds = new HikariDataSource(config);
    }

    public static HikariPool getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
