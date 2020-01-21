package com.server.dao;

import com.server.model.mapper.LogInfoMapper;
import com.server.model.ssh.LogInfo;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(LogInfoMapper.class)
public interface LogInfoDao {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS LOGINFO (" +
            "ID BIGINT AUTO_INCREMENT," +
            "LOGIN VARCHAR(10) NOT NULL," +
            "PASSWORD VARCHAR(10) NOT NULL)")
    void createLogInfoTable();

    @SqlUpdate("INSERT INTO PUBLIC.LOGINFO " +
            "(LOGIN, PASSWORD)" +
            " VALUES(:login, :password)")
    void createRow(@Bind("login") String login,
                @Bind("password") String password);

    @SqlQuery("SELECT * FROM PUBLIC.LOGINFO WHERE ID = :id")
    LogInfo getById(@Bind("id") Integer id);

    void close();
}
