package com.mosreg.hospital_rating.service.scheduled;

import com.mosreg.hospital_rating.service.DataParseService;
import com.mosreg.hospital_rating.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Класс методы которого запускаются сами в указанное время.
 */
@Configuration
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private EmailService emailService;

    @Autowired
    private DataParseService dataParseService;

    //Метод для отправки сообщения новым пользователям из БД в 9:00 утра
    @Scheduled(cron = "0 0 9 * * *")
    public void sendMessage() {
        emailService.sendMail();
    }

    //Метод для обновления БД с пользователями в 7:00 утра
    @Scheduled(cron = "0 0 7 * * *")
    public void addNewUsers() {
        dataParseService.addDataToNewBd();
    }
}
