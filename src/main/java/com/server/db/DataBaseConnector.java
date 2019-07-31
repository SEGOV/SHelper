package com.server.db;

import org.h2.jdbcx.JdbcConnectionPool;
import org.skife.jdbi.v2.DBI;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataBaseConnector {
    
    public DBI getDBI() {
        String url = getPropertyValue("DB_URL");
        String userName = getPropertyValue("DB_USER_NAME");
        String password = getPropertyValue("DB_PASSWORD");

        JdbcConnectionPool connectionPool = JdbcConnectionPool.create(url, userName, password);

        return new DBI(connectionPool);
    }

    private String getPropertyValue(String propertyName) {
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
