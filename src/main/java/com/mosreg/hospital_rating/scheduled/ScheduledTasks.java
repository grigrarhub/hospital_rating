package com.mosreg.hospital_rating.scheduled;

import com.mosreg.hospital_rating.entity.User;
import com.mosreg.hospital_rating.repository.UserRepo;
import com.mosreg.hospital_rating.service.DataParseService;
import com.mosreg.hospital_rating.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Класс методы которого запускаются сами в указанное время.
 **/
@Configuration
@EnableScheduling
@Slf4j
public class ScheduledTasks {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DataParseService dataParseService;

    //Метод для отправки сообщения новым пользователям из БД в 8:00 утра
    @Scheduled(cron = "0 0 8 * * *")
    public void sendMessage() {
        for (User user : userRepo.findUserBySendMailIsFalse()) {
            if (emailService.sendMail(user.getEmail(), user.getFullName(),
                    user.getFullDirectorName(), user.getHospitalName(), user.getUuid())) {
                user.setSendMail(true);
                userRepo.save(user);
            } else log.info("Incorrect email or any parameter is empty. Full name: " + user.getFullName() +
                    ". ID: " + user.getId() + ". Email: " + user.getEmail() + ". Full director name: " + user.getFullDirectorName() +
                    ". Hospital name: " + user.getHospitalName());
        }
    }

    //Метод для обновления БД с пользователями в 7:00 утра
    @Scheduled(cron = "0 0 7 * * *")
    public void addNewUsers() {
        dataParseService.addDataToNewBd();
    }
}
