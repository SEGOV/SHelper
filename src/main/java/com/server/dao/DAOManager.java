package com.server.dao;

import com.server.db.DataBaseConnector;
import org.skife.jdbi.v2.DBI;

public class DAOManager {
    private static final DAOManager INSTANCE = new DAOManager();

    public static DAOManager getInstance() {
        return INSTANCE;
    }

    public Object getDao(Class dao) {
        DBI dbi = new DataBaseConnector().getDBI();
        return dbi.open(dao);
    }
}
