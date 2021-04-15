package com.mosreg.hospital_rating.controller;

import com.mosreg.hospital_rating.entity.Result;
import com.mosreg.hospital_rating.entity.User;
import com.mosreg.hospital_rating.repository.UserRepo;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Controller по приему данных по опросу
 **/
@RestController
@RequestMapping("/questionnaire")
public class RatingController {

    private static final Logger log = Logger.getLogger(RatingController.class);

    @Autowired
    private UserRepo userRepo;

    //Mapping принимает данные по опросу
    @PostMapping("/user/{uuid}")
    public ResponseEntity<Object> receiveHospitalRating(@RequestBody Result result,
                                                        @PathVariable String uuid) {
        JSONObject json = new JSONObject();
        User user = null;
        try {
            user = userRepo.findUserByUuid(uuid);
            if (user.getResults() == null) {
                if (result.getDont_visit() == 0) {
                    user.setResults(result);
                    userRepo.save(user);
                    log.info("ID: " + user.getId()
                            + ". Full name: " + user.getFullName()
                            + ". Email: " + user.getEmail() + " sent a questionnaire");
                }
                //JSON ответ на запрос
                json.put("isValid", true);
                json.put("code", 200);
                JSONArray jsArr = new JSONArray();
                jsArr.put(json.toMap());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                user.setResponseQuestionnaireDate(simpleDateFormat.format(calendar.getTime()));
                return new ResponseEntity<>(json.toMap(), HttpStatus.OK);
            } else {
                json.put("isValid", false);
                json.put("code", 400);
                json.put("message", "Questionnaire already done");
                json.put("messageCode", 4000);
                log.info("Questionnaire already done");
                return new ResponseEntity<>(json.toMap(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            json.put("isValid", false);
            json.put("code", 400);
            json.put("message", "UUID doesn't found");
            json.put("messageCode", 4000);
            log.info("User UUID doesn't found");
            return new ResponseEntity<>(json.toMap(), HttpStatus.BAD_REQUEST);
        }
    }
}