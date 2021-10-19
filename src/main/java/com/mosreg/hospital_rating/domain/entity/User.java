package com.mosreg.hospital_rating.domain.entity;

import com.mosreg.hospital_rating.domain.dto.Result;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.UUID;

/**
 * Entity пользователя для БД
 */
@Entity
@Data
@Table(name = "user_entity")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String fullDirectorName;
    private String hospitalName;
    private String birthday;
    private String dischargeDate;
    private String responseQuestionnaireDate;
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uuid = UUID.randomUUID().toString();
    @Value("false")
    private boolean sendMail;
    @OneToOne(cascade = CascadeType.ALL)
    private Result results;

    public User() {
    }

    public User(String fullName, String email, String fullDirectorName, String hospitalName,
                String birthday, String dischargeDate) {
        this.fullName = fullName;
        this.email = email;
        this.fullDirectorName = fullDirectorName;
        this.hospitalName = hospitalName;
        this.birthday = birthday;
        this.dischargeDate = dischargeDate;
    }
}
