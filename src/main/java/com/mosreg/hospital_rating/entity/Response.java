package com.mosreg.hospital_rating.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Модель получения данных из Json.
 **/
@Entity
@Data
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("grade")
    public byte grade;
    @JsonProperty("comment")
    @Column(length = 500)
    public String comment;
}
