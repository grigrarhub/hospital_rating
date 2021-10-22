package com.mosreg.hospital_rating.controller;

import com.mosreg.hospital_rating.service.DataParseService;
import com.mosreg.hospital_rating.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class testController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private DataParseService dataParseService;

    //Метод для отправки сообщения новым пользователям из БД в 9:00 утра
    @GetMapping("sendMail")
    public String sendMessage() {
        emailService.sendMail();
        return "OK";
    }

    //Метод для обновления БД с пользователями в 7:00 утра
    @GetMapping("addData")
    public int addNewUsers() {
        return dataParseService.addDataToNewBd();
    }
}
