package com.mosreg.hospital_rating.service.impl;

import com.mosreg.hospital_rating.entity.User;
import com.mosreg.hospital_rating.repository.UserRepo;
import com.mosreg.hospital_rating.service.EmailService;
import com.mosreg.hospital_rating.service.FileReaderService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;

/**
 * Класс для отправки сообщения
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = Logger.getLogger(EmailServiceImpl.class);

    public static int COUNT_OF_UNSENT_MAIL;

    public static List<User> USERS_WITH_INCORRECT_DATA;

    private static final String DEFAULT_TOPIC = "Оценка стационара по результатам пребывания";

    @Value("${mail.sent.mail}")
    private String mailSendFrom;

    @Value("classpath:mail.html")
    private Resource html;

    @Autowired
    private FileReaderService fileReaderService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepo userRepo;

    @Override
    public void sendMail() {
        COUNT_OF_UNSENT_MAIL = 0;
        for (User user : userRepo.findUserBySendMailIsFalse()) {
            if (isEmptyData(user.getFullName(), user.getFullDirectorName(), user.getHospitalName())) {
                COUNT_OF_UNSENT_MAIL++;
                USERS_WITH_INCORRECT_DATA.add(user);
                log.info("ID: " + user.getId()
                        + ". Incorrect: any parameters is empty. Full name: " + user.getFullName()
                        + ". Full director name: " + user.getFullDirectorName()
                        + ". Hospital name: " + user.getHospitalName());
                continue;
            }
            if (!isValidMail(user.getEmail())) {
                COUNT_OF_UNSENT_MAIL++;
                USERS_WITH_INCORRECT_DATA.add(user);
                log.info("ID: " + user.getId()
                        + "Incorrect email: " + user.getEmail());
                continue;
            }
            if (sendMail(user.getEmail(), user.getFullName(), user.getFullDirectorName(), user.getHospitalName(), user.getUuid())) {
                user.setSendMail(true);
                userRepo.save(user);
            }
        }
        log.info("Number of unsent messages: " + COUNT_OF_UNSENT_MAIL + ".\n" +
                "INFO: You can see this users by: 10.3.124.13:2220/questionnaire/mail/error/user");
    }

    private boolean sendMail(String to, String fullName, String fullDirectorName, String hospitalName, String UUID) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            helper.setTo(to);
            helper.setText(fileReaderService.requestFromFile(html.getInputStream())
                    .replace("fullName", fullName)
                    .replace("fullDirectorName", fullDirectorName)
                    .replace("hospitalName", hospitalName)
                    .replace("UUID", UUID), true);
            helper.setFrom(mailSendFrom);
            helper.setSubject(DEFAULT_TOPIC);
            javaMailSender.send(mimeMessage);
            log.info("Mail send to " + fullName + ". Email: " + to);
            return true;

        } catch (RuntimeException | MessagingException | IOException e) {
            log.error("Mail doesn't send to " + to, e);
        }
        return false;
    }

    //Проверка на валидность e-mail
    private boolean isValidMail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    //Проверка на пустоту данных
    private boolean isEmptyData(String fullName, String fullDirectorName, String hospitalName) {
        return StringUtils.isBlank(fullName) || StringUtils.isBlank(fullDirectorName) || StringUtils.isBlank(hospitalName);

    }
}
