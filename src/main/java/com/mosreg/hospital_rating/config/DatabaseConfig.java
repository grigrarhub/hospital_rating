package com.mosreg.hospital_rating.config;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Конфигурация для подключения ко второй БД для доступа к людям выписанным из стационара
 */
@Configuration
public class DatabaseConfig {

    @Value("${spring.second-datasourse.serverName}")
    private String servername;
    @Value("${spring.second-datasource.databaseName}")
    private String databaseName;
    @Value("${spring.second-datasource.username}")
    private String userName;
    @Value("${spring.second-datasource.password}")
    private String password;
    @Value("${spring.second-datasource.port}")
    private Integer port;

    public DataSource dataSource() {
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String[] servers = {servername};
        dataSource.setServerNames(servers);
        dataSource.setDatabaseName(databaseName);
        dataSource.setUser(userName);
        dataSource.setPassword(password);
        int[] ports = {port};
        dataSource.setPortNumbers(ports);
        return dataSource;
    }
}
