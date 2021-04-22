package com.qrlo.qrloservicecore.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@JsonInclude
@AllArgsConstructor
public class AuthResponse {
    @JsonProperty
    private final String token;
}
