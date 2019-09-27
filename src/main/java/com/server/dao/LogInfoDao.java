package com.server.dao;

import com.server.model.mapper.LogInfoMapper;
import com.server.model.ssh.LogInfo;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(LogInfoMapper.class)
public interface LogInfoDao {

    @SqlQuery("SELECT * FROM PUBLIC.LOGINFO WHERE ID = :id")
    LogInfo getById(@Bind("id") Integer id);

    void close();
}
