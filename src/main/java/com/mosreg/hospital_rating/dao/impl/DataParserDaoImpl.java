package com.mosreg.hospital_rating.dao.impl;

import com.mosreg.hospital_rating.config.DatabaseConfig;
import com.mosreg.hospital_rating.dao.DataParserDao;
import com.mosreg.hospital_rating.entity.User;
import com.mosreg.hospital_rating.service.FileReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Класс запросов для получения данных из БД
 */
@Repository
@Slf4j
public class DataParserDaoImpl implements DataParserDao {

    @Value("classpath:request.sql")
    private Resource sql;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DatabaseConfig databaseConfig;

    @Autowired
    private FileReaderService fileReaderService;

    @Override
    public List<User> receiveListRequest() {
        try {
            Connection connection = databaseConfig.dataSource().getConnection();
            log.debug("Connection for DBMS complete");
            jdbcTemplate.setDataSource(databaseConfig.dataSource());
            List<User> list = jdbcTemplate.query(String.format(fileReaderService.requestFromFile(sql.getInputStream()), getData())
                    , new PatientMapper());
            connection.close();
            log.debug("Disconnection for DBMS complete");
            return list;
        } catch (SQLException | IOException throwable) {
            log.error("SQL Error", throwable); // обработка ошибок DriverManager.getConnection
        }
        return null;
    }

    //Метод указывающий дату 2 дня назад для выкачи БД
    private String getData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);
        return "'" + simpleDateFormat.format(calendar.getTime()) + "'";
    }

    private static final class PatientMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            return new User(resultSet.getString("Person_surname")
                    + " " + resultSet.getString("Person_firname")
                    + " " + resultSet.getString("Person_secname"),
                    resultSet.getString("mail"),
                    resultSet.getString("OrgHeadPerson_Fio"),
                    resultSet.getString("lpu_name"),
                    resultSet.getString("Person_birthday"),
                    resultSet.getString("EvnPS_disDT"));
        }
    }
}
