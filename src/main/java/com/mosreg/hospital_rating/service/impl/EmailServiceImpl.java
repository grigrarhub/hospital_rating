package com.mosreg.hospital_rating.service.impl;

import com.mosreg.hospital_rating.service.EmailService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс для отправки сообщения
 **/
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String DEFAULT_TOPIC = "Оценка стационара по результатам пребывания";

    @Value("${mail.sent.mail}")
    private String mailSendFrom;

    @Autowired
    private JavaMailSender javaMailSender;

    @SneakyThrows
    @Override
    //Отправить сообщение
    public boolean sendMail(String to, String fullName, String fullDirectorName, String hospitalName, String UUID) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        if (isValidMail(to)) {
            if (!(StringUtils.isBlank(fullName)
                    || StringUtils.isBlank(fullDirectorName)
                    || StringUtils.isBlank(hospitalName))) {
                helper.setTo(to);
                helper.setText(Objects.requireNonNull(requestFromFile())
                        .replace("fullName", fullName)
                        .replace("fullDirectorName", fullDirectorName)
                        .replace("hospitalName", hospitalName)
                        .replace("UUID", UUID), true);
                helper.setFrom(mailSendFrom);
                helper.setSubject(DEFAULT_TOPIC);
                javaMailSender.send(mimeMessage);
                log.info("Mail send to " + fullName + ". Email: " + to);
                return true;
            }
        }
        return false;
    }

    //Проверка на валидность e-mail
    private boolean isValidMail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    //Метод для выкачки текста html из файла
    private String requestFromFile() {
        try (Stream<String> stream = Files.lines(Paths.get("src/main/webapp/WEB_INF/html/mail.html"))) {
            log.debug("HTML file successfully read");
            return stream.map(String::new).collect(Collectors.joining());
        } catch (IOException e) {
            log.error("Html file doesn't found\n", e);
        }
        return null;
    }
}
