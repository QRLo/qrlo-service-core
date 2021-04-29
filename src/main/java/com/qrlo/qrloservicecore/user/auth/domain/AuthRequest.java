package com.qrlo.qrloservicecore.user.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qrlo.qrloservicecore.user.model.OAuthType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Data
@NoArgsConstructor
@JsonInclude
public class AuthRequest {
    @JsonProperty(required = true)
    private OAuthType oAuthType;
    @JsonProperty(required = true)
    private String oAuthAccessToken;
}
