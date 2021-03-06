package com.mosreg.hospital_rating.repository.dao;

import com.mosreg.hospital_rating.domain.entity.User;

import java.util.List;

/**
 * Interface для использования методов DataParseDaoImpl
 */
public interface DataParserDao {
    //Получение данных из БД
    List<User> receiveListRequest();
}
