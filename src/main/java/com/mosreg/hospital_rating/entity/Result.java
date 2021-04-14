package com.mosreg.hospital_rating.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Модель получения данных из Json.
 **/
@Entity
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_1_1;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_1_2;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_1_3;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_1_4;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_1_5;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_1_6;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_1;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_2;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_3;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_4;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_5;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_6;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_7;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_8;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_9;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_10;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_11;
    @OneToOne(cascade = CascadeType.ALL)
    public Response Result_2_12;
    public String Notes;
    public int Dont_visit;
}
