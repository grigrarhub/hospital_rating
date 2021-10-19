package com.mosreg.hospital_rating.controller;

import com.mosreg.hospital_rating.domain.dto.Result;
import com.mosreg.hospital_rating.domain.entity.User;
import com.mosreg.hospital_rating.repository.UserRepo;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Controller по приему данных по опросу
 */
@RestController
@RequestMapping("/questionnaire")
public class RatingController {

    private static final Logger log = Logger.getLogger(RatingController.class);

    private static JSONObject json;

    @Autowired
    private UserRepo userRepo;

    //Mapping принимает данные по опросу
    @PostMapping("/user/{uuid}")
    public ResponseEntity<Object> receiveHospitalRating(@RequestBody Result result,
                                                        @PathVariable String uuid) {
        json = new JSONObject();
        try {
            return saveUserQuestionnaireAndSendJsonAnswer(result, uuid);
        } catch (NullPointerException e) {
            return uuidNotFound();
        }
    }

    private ResponseEntity<Object> saveUserQuestionnaireAndSendJsonAnswer(Result result, String uuid) {
        User user = userRepo.findUserByUuid(uuid);
        if (user == null) {
            throw new NullPointerException();
        }
        if (user.getResults() == null) {
            //Если опрашиваемый нажал на странице опросника ссылку "Я не посещал ...", то Dont_visit = 1.
            if (result.getDont_visit() == 0) {
                user.setResults(result);
            }
            setResponseQuestionnaireDate(user);
            userRepo.save(user);

            log.info("ID: " + user.getId()
                    + ". Full name: " + user.getFullName()
                    + ". Email: " + user.getEmail() + " sent a questionnaire");

            json.put("isValid", true);
            json.put("code", 200);
            return new ResponseEntity<>(json.toMap(), HttpStatus.OK);
        } else {
            log.info("Questionnaire already done. User: id - "
                    + user.getId() + ". Email - "
                    + user.getEmail() + " try to reply second time");

            return new ResponseEntity<>(fillJsonModel
                    (json, "Questionnaire already done").toMap(), HttpStatus.BAD_REQUEST);
        }
    }

    private void setResponseQuestionnaireDate(User user) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        user.setResponseQuestionnaireDate(simpleDateFormat.format(calendar.getTime()));
    }

    private JSONObject fillJsonModel(JSONObject json, String message) {
        json.put("isValid", false);
        json.put("code", 400);
        json.put("message", message);
        json.put("messageCode", 400);
        return json;
    }

    private ResponseEntity<Object> uuidNotFound() {
        log.info("User UUID doesn't found");

        return new ResponseEntity<>(fillJsonModel
                (json, "UUID doesn't found").toMap(), HttpStatus.BAD_REQUEST);
    }
}