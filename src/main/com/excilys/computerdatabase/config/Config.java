package main.com.excilys.computerdatabase.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;

public class Config {

    private static Properties properties;
    private static final String PROPERTIES_FILE = "config.properties";
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Config.class);

    InputStream inputStream;

    static {
        properties = new Properties();

        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + PROPERTIES_FILE + "' not found in the classpath");
            }

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    public static Properties getProperties() {
        return properties;
    }
}
