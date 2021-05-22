package com.qrlo.qrloservicecore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-05
 */
@Table("business_cards")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessCard {
    @Id
    private Integer id;
    @Column("user_id")
    private Integer userId;
    @Column("company")
    private String company;
    @Column("email")
    private String email;
    @Column("phone")
    private String phone;
    @Column("position")
    private String position;
    @Column("email_verified")
    private Boolean emailVerified = false;
    @JsonIgnore
    @Column("version")
    @Version
    private Long version;
}
