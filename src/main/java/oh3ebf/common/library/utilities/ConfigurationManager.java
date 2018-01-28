/**
 * Software: common library
 * Module: configuration instance class
 * Version: 0.2
 * Licence: GPL2
 * Owner: Kim Kristo
 * Date creation : 16.4.2013
 *
 */
package oh3ebf.common.library.utilities;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigurationManager {

    private static Logger logger = LogManager.getLogger(ConfigurationManager.class);
    private static ConfigurationManager cfgManager = null;
    private PropertiesConfiguration config = null;

    public ConfigurationManager() throws ConfigurationException {
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(new Parameters().properties()
                        .setFileName("myconfig.properties")
                        .setThrowExceptionOnMissing(true)
                        .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
                        .setIncludesAllowed(false));
        config = builder.getConfiguration();

    }

    /**
     * Function returns configuration instance
     *
     * @return configuration instance
     *
     */
    public static ConfigurationManager getConfiguration() {
        if (cfgManager == null) {
            try {
                // get configuration from file
                cfgManager = new ConfigurationManager();
            } catch (ConfigurationException ex) {
                logger.error("Configuration file not found");
            }
        }

        return (cfgManager);
    }

    /**
     * Function checks parameter existence and reads value
     *
     * @param key to parameter
     * @param value to parameter initialization
     * @return current value as String
     *
     */
    public String checkAndReadStringValue(String key, Object value) {
        if (config.containsKey(key)) {
            return (config.getString(key));
        } else {
            config.addProperty(key, value);
            /*try {
                config..save();
            } catch (Exception ex) {
                logger.error("failed to save configuration file");
            }*/
        }
        return (null);
    }

    /**
     * Function checks parameter existence and reads value
     *
     * @param key to parameter
     * @param value to parameter initialization
     * @return current value as boolean
     *
     */
    public boolean checkAndReadBoolValue(String key, Object value) {
        if (config.containsKey(key)) {
            return (config.getBoolean(key));
        } else {
            config.addProperty(key, value);
            /*try {
                config.save();
            } catch (Exception ex) {
                logger.error("failed to save configuration file");
            }*/
        }
        return (false);
    }

    /**
     * Function checks parameter existence and reads value
     *
     * @param key to parameter
     * @param value to parameter initialization
     * @return current value as integer
     *
     */
    public int checkAndReadIntValue(String key, Object value) {
        if (config.containsKey(key)) {
            return (config.getInt(key));
        } else {

            config.addProperty(key, value);
            /*try {
                config.save();
            } catch (Exception ex) {
                logger.error("failed to save configuration file");
            }*/
        }
        return (0);
    }
}
