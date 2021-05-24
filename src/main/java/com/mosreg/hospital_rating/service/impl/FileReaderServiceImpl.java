package com.mosreg.hospital_rating.service.impl;

import com.mosreg.hospital_rating.service.FileReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Класс для подгрузки данных с файла
 */
@Service
@Slf4j
public class FileReaderServiceImpl implements FileReaderService {

    @Override
    public String requestFromFile(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder line = new StringBuilder("");
            while (reader.ready()) {
                line.append(reader.readLine());
            }
            log.debug("File successfully read");
            return line.toString();
        } catch (IOException e) {
            log.error("File doesn't found\n", e);
        }
        return null;
    }
}
