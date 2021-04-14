package com.mosreg.hospital_rating.controller;

import com.mosreg.hospital_rating.entity.Result;
import com.mosreg.hospital_rating.entity.User;
import com.mosreg.hospital_rating.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller по приему данных по опросу
 **/
@RestController
@Slf4j
public class RatingController {

    @Autowired
    private UserRepo userRepo;

    //Mapping принимает данные по опросу
    @PostMapping("/questionnaire/user/{uuid}")
    public ResponseEntity<Object> receiveHospitalRating(@RequestBody Result result,
                                                        @PathVariable String uuid) {
        JSONObject json = new JSONObject();
        User user = userRepo.findUserByUuid(uuid);
        if (user.getResults() == null) {
            if (result.getDont_visit() == 0) {
                user.setResults(result);
                userRepo.save(user);
                log.info("Full name: " + user.getFullName() + ". Email: " + user.getEmail() + " sent a questionnaire");
            }
            //JSON ответ на запрос
            json.put("isValid", true);
            json.put("code", 200);
            JSONArray jsArr = new JSONArray();
            jsArr.put(json.toMap());
            return new ResponseEntity<>(json.toMap(), HttpStatus.OK);
        }
        json.put("isValid", false);
        json.put("code", 400);
        json.put("message", "Опрос уже существует");
        json.put("messageCode", 4000);
        return new ResponseEntity<>(json.toMap(), HttpStatus.BAD_REQUEST);
    }
}