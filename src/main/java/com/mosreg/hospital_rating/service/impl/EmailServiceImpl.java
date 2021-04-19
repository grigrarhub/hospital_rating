package com.mosreg.hospital_rating.service.impl;

import com.mosreg.hospital_rating.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Класс для отправки сообщения
 **/
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String DEFAULT_TOPIC = "Оценка стационара по результатам пребывания";

    @Value("${mail.sent.mail}")
    private String mailSendFrom;

    @Value("classpath:mail.html")
    private Resource html;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    //Отправить сообщение
    public boolean sendMail(String to, String fullName, String fullDirectorName, String hospitalName, String UUID) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
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
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("Mail doesn't send to " + to, e);
        }
        return false;
    }

    //Проверка на валидность e-mail
    private boolean isValidMail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    //Метод для выкачки текста html из файла
    public String requestFromFile() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(html.getInputStream()))) {
            StringBuilder line = new StringBuilder("");
            while (reader.ready()) {
                line.append(reader.readLine());
            }
            log.debug("HTML file successfully read");
            return line.toString();
        } catch (IOException e) {
            log.error("Html file doesn't found\n", e);
        }
        return null;
    }
}
