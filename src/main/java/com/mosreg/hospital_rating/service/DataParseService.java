package com.mosreg.hospital_rating.service;

import com.mosreg.hospital_rating.entity.User;

import java.util.List;

/**
 * Interface для использования методов DataParseServiceImpl
 */
public interface DataParseService {
    //Добавление пользователей из стационара в новую БД
    void addDataToNewBd();
    //Получение списка пользователей из БД
    List<User> pullDataFromDB();
}
