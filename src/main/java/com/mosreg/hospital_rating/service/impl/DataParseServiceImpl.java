package com.mosreg.hospital_rating.service.impl;

import com.mosreg.hospital_rating.dao.DataParserDao;
import com.mosreg.hospital_rating.entity.User;
import com.mosreg.hospital_rating.repository.UserRepo;
import com.mosreg.hospital_rating.service.DataParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс для получения данных из БД и заполнение этими данными другую БД
 **/
@Service
@Slf4j
public class DataParseServiceImpl implements DataParseService {

    @Autowired
    private DataParserDao dataParserDao;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    public List<User> pullDataFromDB() {
        return dataParserDao.receiveListRequest();
    }

    //Добавление данных полученных JDBC запросом из БД в новую БД для рейтинга больницы
    @Override
    public void addDataToNewBd() {
        for (User user : pullDataFromDB()) {
            user.setFullName(toUpperCaseForFirstLetter(user.getFullName()));
            user.setFullDirectorName(toUpperCaseForFirstLetter(user.getFullDirectorName()));
            userRepo.save(new User(user.getFullName(),
                    user.getEmail(), user.getFullDirectorName(), user.getHospitalName()));
        }
        log.info("Database updated by new users");
    }

    private static String toUpperCaseForFirstLetter(String inputText) {
        String text = inputText.toLowerCase();
        StringBuilder builder = new StringBuilder(text);
        //Выставляем первый символ заглавным, если это буква
        if (Character.isAlphabetic(text.codePointAt(0))) {
            builder.setCharAt(0, Character.toUpperCase(text.charAt(0)));
        }
        //Крутимся в цикле, и меняем буквы, перед которыми пробел на заглавные
        for (int i = 1; i < text.length(); i++)
            if (Character.isAlphabetic(text.charAt(i)) && Character.isSpaceChar(text.charAt(i - 1))) {
                builder.setCharAt(i, Character.toUpperCase(text.charAt(i)));
            }
        return builder.toString();
    }
}
