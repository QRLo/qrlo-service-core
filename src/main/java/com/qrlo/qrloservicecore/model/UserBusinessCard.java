package com.qrlo.qrloservicecore.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-10
 */
@Data
public class UserBusinessCard {
    @Id
    private Integer id;
    @Column("user_id")
    private Integer userId;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("email")
    private String email;
    @Column("phone")
    private String phone;
    @Column("position")
    private String position;
    @Column("company")
    private String company;
}
