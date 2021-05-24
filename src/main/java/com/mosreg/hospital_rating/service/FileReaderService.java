package com.mosreg.hospital_rating.service;

import java.io.InputStream;

/**
 * Interface для использования методов FileReaderServiceImpl
 */
public interface FileReaderService {
    //Метод для выкачки текста из файла
    String requestFromFile(InputStream inputStream);
}
