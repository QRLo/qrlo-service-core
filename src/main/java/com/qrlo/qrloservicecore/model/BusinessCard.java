package com.qrlo.qrloservicecore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessCard {
    @Id
    private String id;
    private String company;
    private String email;
    private String phone;
    private String position;
    private Boolean emailVerified = false;
}
