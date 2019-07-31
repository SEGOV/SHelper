package com.server.service;

import com.server.dao.DAOManager;
import com.server.dao.SessionDAO;
import com.server.model.ssh.Session;

import java.util.List;

public class SessionService {
    private static final SessionService INSTANCE = new SessionService();
    private DAOManager daoManager = DAOManager.getInstance();

    public static SessionService getInstance() {
        return INSTANCE;
    }

    public void createSessionTable() {
        SessionDAO dao = (SessionDAO) daoManager.getDao(SessionDAO.class);
        dao.createSessionTable();
        dao.close();
    }

    public void createSession(Session session) {
        SessionDAO dao = (SessionDAO) daoManager.getDao(SessionDAO.class);
        dao.create(session.getFileProtocol(), session.getHostName(), session.getPortNumber(), session.getUserName(), session.getPassword());
        dao.close();
    }

    public void createSession(String fileProtocol, String hostName, Integer portNumber, String userName, String userPassword) {
        SessionDAO dao = (SessionDAO) daoManager.getDao(SessionDAO.class);
        dao.create(fileProtocol, hostName, portNumber, userName, userPassword);
        dao.close();
    }

    public Session getById(Integer id) {
        SessionDAO dao = (SessionDAO) daoManager.getDao(SessionDAO.class);
        Session session = dao.getById(id);
        dao.close();
        return session;
    }

    public List<Session> getAllSessions() {
        SessionDAO dao = (SessionDAO) daoManager.getDao(SessionDAO.class);
        List<Session> allSessions = dao.getAllSessions();
        dao.close();
        return allSessions;
    }

    public void removeSessionById(Integer id) {
        SessionDAO dao = (SessionDAO) daoManager.getDao(SessionDAO.class);
        dao.removeById(id);
        dao.close();
    }
}
