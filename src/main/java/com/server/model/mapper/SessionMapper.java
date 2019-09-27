package com.server.model.mapper;

import com.server.model.ssh.Session;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionMapper implements ResultSetMapper<Session> {
    public Session map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Session(
                resultSet.getInt("ID"),
                resultSet.getString("SFTP_FILE_PROTOCOL"),
                resultSet.getString("HOST_NAME"),
                resultSet.getInt("PORT_NUMBER"),
                resultSet.getString("USER_NAME"),
                resultSet.getString("PASSWORD"),
                resultSet.getString("PROJECT_PATH"));
    }
}
