package com.server.db;

import com.server.util.PropertiesReader;
import org.h2.jdbcx.JdbcConnectionPool;
import org.skife.jdbi.v2.DBI;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataBaseConnector {
    
    public DBI getDBI() {
        PropertiesReader propertiesReader = PropertiesReader.getInstance();

        String url = propertiesReader.getPropertyValue("DB_URL");
        String userName = propertiesReader.getPropertyValue("DB_USER_NAME");
        String password = propertiesReader.getPropertyValue("DB_PASSWORD");

        JdbcConnectionPool connectionPool = JdbcConnectionPool.create(url, userName, password);

        return new DBI(connectionPool);
    }
}
