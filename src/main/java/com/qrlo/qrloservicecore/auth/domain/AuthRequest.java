package com.qrlo.qrloservicecore.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qrlo.qrloservicecore.auth.model.OAuth;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Setter
@Getter
@NoArgsConstructor
@JsonInclude
public class AuthRequest {
    @JsonProperty(required = true)
    private OAuth.OAuthType oAuthType;
    @JsonProperty(required = true)
    private String accessToken;
}
