package com.qrlo.qrloservicecore.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-10
 */
@Data
public class UnwoundUserBusinessCard {
    private String userId;
    private String businessCardId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
}
