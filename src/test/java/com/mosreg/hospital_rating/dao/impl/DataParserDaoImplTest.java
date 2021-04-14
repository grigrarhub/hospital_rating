package com.mosreg.hospital_rating.dao.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DataParserDaoImplTest {

    @Test
    void availabilitySqlFileTest() {
        try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/db/sql/request.sql"))) {
            String text = stream.map(String::new).collect(Collectors.joining());
            if ("".equals(text)) {
                text = null;
            }
            Assertions.assertNotNull(text, "SQL file is empty");
        } catch (IOException e) {
            Assertions.fail("SQL file doesn't found", e);
        }
    }
}