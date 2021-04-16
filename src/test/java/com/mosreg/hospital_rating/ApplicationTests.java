package com.mosreg.hospital_rating;

import com.mosreg.hospital_rating.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class ApplicationTests {

    @Autowired
    EmailServiceImpl emailService;

    @Value("classpath:mail.html")
    private Resource html;

    @Value("classpath:request.sql")
    private Resource sql;

    @Test
    void contextLoads() {
    }

    @Test
    void availabilityHtmlFileTest() {
        try (Stream<String> absolutelyPath =
                     Files.lines(Paths.get("src/main/resources/mail.html"));
             Stream<String> relativePath =
                     Files.lines(Paths.get(Objects.requireNonNull(html.getURI())))) {
            String textByAbsolutelyPath = absolutelyPath.map(String::new).collect(Collectors.joining());
            String textByRelativePath = relativePath.map(String::new).collect(Collectors.joining());
            if ("".equals(textByAbsolutelyPath)) {
                textByAbsolutelyPath = null;
            }
            Assertions.assertNotNull(textByAbsolutelyPath, "HTML file is empty");
            Assertions.assertEquals(textByAbsolutelyPath, textByRelativePath);
        } catch (IOException e) {
            Assertions.fail("HTML file doesn't found", e);
        }
    }

    @Test
    void availabilitySqlFileTest() {
        try (Stream<String> absolutelyPath =
                     Files.lines(Paths.get("src/main/resources/request.sql"));
             Stream<String> relativePath =
                     Files.lines(Paths.get(Objects.requireNonNull(sql.getURI())))) {
            String textByAbsolutelyPath = absolutelyPath.map(String::new).collect(Collectors.joining());
            String textByRelativePath = relativePath.map(String::new).collect(Collectors.joining());
            if ("".equals(textByAbsolutelyPath)) {
                textByAbsolutelyPath = null;
            }
            Assertions.assertNotNull(textByAbsolutelyPath, "SQL file is empty");
            Assertions.assertEquals(textByAbsolutelyPath, textByRelativePath);
        } catch (IOException e) {
            Assertions.fail("SQL file doesn't found", e);
        }
    }
}
