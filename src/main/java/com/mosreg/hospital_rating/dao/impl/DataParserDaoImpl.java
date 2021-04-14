package com.mosreg.hospital_rating.dao.impl;

import com.mosreg.hospital_rating.config.DatabaseConfig;
import com.mosreg.hospital_rating.dao.DataParserDao;
import com.mosreg.hospital_rating.entity.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс запросов для получения данных из БД
 **/
@Repository
@Slf4j
public class DataParserDaoImpl implements DataParserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DatabaseConfig databaseConfig;

    @Override
    public List<User> receiveListRequest() {
        try {
            Connection connection = databaseConfig.dataSource().getConnection();
            log.debug("Connection for DBMS complete");
            jdbcTemplate.setDataSource(databaseConfig.dataSource());
            List<User> list = jdbcTemplate.query(String.format(Objects.requireNonNull(requestFromFile()), getData())
                    , new PatientMapper());
            connection.close();
            log.debug("Disconnection for DBMS complete");
            return list;
        } catch (SQLException throwable) {
            log.error("SQL Error", throwable); // обработка ошибок DriverManager.getConnection
        }
        return null;
    }

    //Реализация JDBC запроса из стороннего файла
    private String requestFromFile() {
        try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/db/sql/request.sql"))) {
            log.debug("HTML file successfully read");
            return stream.map(String::new).collect(Collectors.joining());
        } catch (IOException e) {
            log.error("Html file doesn't found\n", e);
        }
        return null;
    }

    //Метод указывающий дату 2 дня назад для выкачи БД
    private String getData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);
        String text = "'" + simpleDateFormat.format(calendar.getTime()) + "'";
        System.out.println(text);
        return "'" + simpleDateFormat.format(calendar.getTime()) + "'";
    }

    private static final class PatientMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            return new User().setFullName(resultSet.getString("Person_surname")
                    + " " + resultSet.getString("Person_firname")
                    + " " + resultSet.getString("Person_secname"))
                    .setEmail(resultSet.getString("mail"))
                    .setFullDirectorName(resultSet.getString("OrgHeadPerson_Fio"))
                    .setHospitalName(resultSet.getString("lpu_name"));
        }
    }
}
