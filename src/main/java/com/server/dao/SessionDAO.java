package com.server.dao;

import com.server.model.mapper.SessionMapper;
import com.server.model.ssh.Session;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(SessionMapper.class)
public interface SessionDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS SESSION (" +
            "ID BIGINT AUTO_INCREMENT," +
            "FILE_PROTOCOL VARCHAR(10) NOT NULL," +
            " HOST_NAME VARCHAR(30) NOT NULL, " +
            "PORT_NUMBER INT NOT NULL, " +
            "USER_NAME VARCHAR(20) NOT NULL, " +
            "PASSWORD VARCHAR(20) NOT NULL, " +
            "PROJECT_PATH VARCHAR(200) NULL)")
    void createSessionTable();

    @SqlUpdate("INSERT INTO PUBLIC.SESSION " +
            "(FILE_PROTOCOL, HOST_NAME, PORT_NUMBER, USER_NAME, PASSWORD, PROJECT_PATH)" +
            " VALUES(:file_protocol, :host_name, :port_number, :user_name, :password, :project_path)")
    void create(@Bind("file_protocol") String fileProtocol,
                @Bind("host_name") String hostName,
                @Bind("port_number") Integer portNumber,
                @Bind("user_name") String userName,
                @Bind("password") String password,
                @Bind("project_path") String projectPath);

    @SqlQuery("SELECT * FROM PUBLIC.SESSION WHERE FILE_PROTOCOL = :file_protocol AND HOST_NAME = :host_name AND PORT_NUMBER = :port_number AND USER_NAME = :user_name AND PASSWORD = :password AND PROJECT_PATH = :project_path")
    Session getSessionByParameters(@Bind("file_protocol") String fileProtocol,
                                   @Bind("host_name") String hostName,
                                   @Bind("port_number") Integer portNumber,
                                   @Bind("user_name") String userName,
                                   @Bind("password") String password,
                                   @Bind("project_path") String projectPath);

    @SqlQuery("SELECT * FROM PUBLIC.SESSION WHERE ID = :id")
    Session getById(@Bind("id") Integer id);

    @SqlQuery ("SELECT * FROM PUBLIC.SESSION")
    List<Session> getAllSessions ();

    @SqlUpdate("UPDATE PUBLIC.SESSION SET" +
            " FILE_PROTOCOL = :file_protocol, " +
            "HOST_NAME = :host_name," +
            " PORT_NUMBER = :port_number," +
            " USER_NAME = :user_name, " +
            "PASSWORD = :password, " +
            "PROJECT_PATH = :project_path " +
            "WHERE ID = :id")
    void updateSession(@Bind("id") String id,
                       @Bind("file_protocol") String fileProtocol,
                       @Bind("host_name") String hostName,
                       @Bind("port_number") Integer portNumber,
                       @Bind("user_name") String userName,
                       @Bind("password") String password,
                       @Bind("project_path") String projectPath);

    @SqlUpdate("DELETE FROM PUBLIC.SESSION WHERE ID = :id")
    void removeById(@Bind("id") Integer id);

    void close();
}
