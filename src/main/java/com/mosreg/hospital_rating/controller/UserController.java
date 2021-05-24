package com.mosreg.hospital_rating.controller;

import com.mosreg.hospital_rating.service.impl.EmailServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller по отображению данных о людях с неполными данными
 * для отправки email по ссылке 10.3.124.13:2220/questionnaire/mail/error/user
 */
@Controller
@RequestMapping("/questionnaire")
public class UserController {

    @GetMapping("/mail/error/user")
    public String user(Model model) {
        model.addAttribute("users", EmailServiceImpl.USERS_WITH_INCORRECT_DATA);
        model.addAttribute("count", EmailServiceImpl.COUNT_OF_UNSENT_MAIL);
        return "user";
    }
}
