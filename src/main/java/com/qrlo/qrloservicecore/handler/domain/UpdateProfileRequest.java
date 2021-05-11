package com.qrlo.qrloservicecore.handler.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-09
 */
@Data
@JsonInclude
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
}
