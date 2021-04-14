package com.mosreg.hospital_rating.service;

/**
 * Interface для использования методов EmailServiceImpl
 **/
public interface EmailService {
    //Отправка сообщения
    boolean sendMail(String to, String fullName, String fullDirectorName, String hospitalName, String UUID);
}
