package com.server.model.mapper;

import com.server.model.ssh.LogInfo;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInfoMapper implements ResultSetMapper<LogInfo> {
    public LogInfo map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new LogInfo(
                resultSet.getInt("ID"),
                resultSet.getString("LOGIN"),
                resultSet.getString("PASSWORD"));
    }
}

