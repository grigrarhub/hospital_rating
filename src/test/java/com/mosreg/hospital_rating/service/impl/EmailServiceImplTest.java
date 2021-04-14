package com.mosreg.hospital_rating.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class EmailServiceImplTest {

    @Test
    void availabilityHtmlFileTest() {
        try (Stream<String> stream = Files.lines(Paths.get("src/main/webapp/WEB_INF/html/mail.html"))) {
            String text = stream.map(String::new).collect(Collectors.joining());
            if ("".equals(text)) {
                text = null;
            }
            Assertions.assertNotNull(text, "HTML file is empty");
        } catch (IOException e) {
            Assertions.fail("HTML file doesn't found", e);
        }
    }
}