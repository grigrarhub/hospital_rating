package com.mosreg.hospital_rating.controller;

import com.mosreg.hospital_rating.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller по отображению данных о людях с неполными данными для отправки email
 **/
@Controller
@RequestMapping("/questionnaire")
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/mail/error/user")
    public String user(Model model) {
        model.addAttribute("users", userRepo.findUserBySendMailIsFalse());
        return "user";
    }
}
