package by.golik.webcrawler.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Nikita Golik
 */
public class PropertiesLoader {

    private static final Logger logger = LogManager.getLogger(PropertiesLoader.class);

    private static Properties prop;
    static{
        InputStream is = null;
        try {
            prop = new Properties();
            is = PropertiesLoader.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(is);
        } catch (IOException e) {
           logger.error("Can't read properties from : {}", is);
        }
    }
    public static final String TERMS = prop.getProperty("terms");
    public static final String START_URL= prop.getProperty("startURL");
    public static final Integer MAX_DEPTH= Integer.parseInt(prop.getProperty("maxDepth"));
    public static final Integer MAX_URL= Integer.parseInt(prop.getProperty("maxURL"));

}
