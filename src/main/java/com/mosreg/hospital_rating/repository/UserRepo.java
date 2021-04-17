package com.mosreg.hospital_rating.repository;

import com.mosreg.hospital_rating.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository для работы с БД
 **/
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    List<User> findUserBySendMailIsFalse();

    User findUserByUuid(String uuid);

    List<User> findUsersByFullNameAndEmailAndHospitalNameAndBirthdayAndDischargeDate(String name,
                                                                                     String email,
                                                                                     String hospitalName,
                                                                                     String birthday,
                                                                                     String dischargeDate);
}
