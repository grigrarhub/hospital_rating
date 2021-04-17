package com.mosreg.hospital_rating.scheduled;

import com.mosreg.hospital_rating.entity.User;
import com.mosreg.hospital_rating.repository.UserRepo;
import com.mosreg.hospital_rating.service.DataParseService;
import com.mosreg.hospital_rating.service.EmailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Класс методы которого запускаются сами в указанное время.
 **/
@Configuration
@EnableScheduling
public class ScheduledTasks {

    public static int count = 0;

    private static final Logger log = Logger.getLogger(ScheduledTasks.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DataParseService dataParseService;

    //Метод для отправки сообщения новым пользователям из БД в 8:00 утра
    @Scheduled(cron = "0 0 8 * * *")
    public void sendMessage() {
        count = 0;
        for (User user : userRepo.findUserBySendMailIsFalse()) {
            if (emailService.sendMail(user.getEmail(), user.getFullName(),
                    user.getFullDirectorName(), user.getHospitalName(), user.getUuid())) {
                user.setSendMail(true);
                userRepo.save(user);
                log.info("Mail send to " + user.getFullName() + ". Email: " + user.getEmail());
            } else {
                count++;
                log.info("ID: " + user.getId()
                        + ". Incorrect email or any parameter is empty. Full name: " + user.getFullName()
                        + ". Email: " + user.getEmail()
                        + ". Full director name: " + user.getFullDirectorName()
                        + ". Hospital name: " + user.getHospitalName());
            }
        }
        log.info("Number of unsent messages: " + count + ".\nINFO: You can see this users by: 10.3.124.13:2220/questionnaire/mail/error/user");
    }

    //Метод для обновления БД с пользователями в 7:00 утра
    //Сервер отстает на 3 часа
    @Scheduled(cron = "0 0 4 * * *")
    public void addNewUsers() {
        dataParseService.addDataToNewBd();
    }
}
