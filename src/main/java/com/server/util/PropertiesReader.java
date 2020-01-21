package com.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private static final PropertiesReader INSTANCE = new PropertiesReader();

    public static PropertiesReader getInstance() {
        return INSTANCE;
    }

    public String getPropertyValue(String propertyName) {
        String propertyValue = null;
        Properties properties = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(inputStream);
            propertyValue = properties.getProperty(propertyName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertyValue;
    }
}
