package com.qrlo.qrloservicecore.user.handler.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Getter
@JsonInclude
@AllArgsConstructor
public class AuthResponse {
    @JsonProperty
    private final String token;
}
