package com.server.service;

import com.server.dao.DAOManager;
import com.server.dao.LogInfoDao;
import com.server.model.ssh.LogInfo;

public class LogInfoService {
    private static final LogInfoService INSTANCE = new LogInfoService();
    private DAOManager daoManager = DAOManager.getInstance();

    public static LogInfoService getInstance() {
        return INSTANCE;
    }

    public LogInfo getById(Integer id) {
        LogInfoDao dao = (LogInfoDao) daoManager.getDao(LogInfoDao.class);
        LogInfo logInfo = dao.getById(id);
        dao.close();
        return logInfo;
    }

    public void createLogInfoTable() {
        LogInfoDao dao = (LogInfoDao) daoManager.getDao(LogInfoDao.class);
        dao.createLogInfoTable();
        dao.close();
    }

    public void createRow(String login, String password) {
        LogInfoDao dao = (LogInfoDao) daoManager.getDao(LogInfoDao.class);
        dao.createRow(login, password);
        dao.close();
    }
}
