package com.qrlo.qrloservicecore.user.handler.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qrlo.qrloservicecore.user.model.OAuthType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@JsonInclude
public class AuthRequest {
    @JsonProperty(required = true)
    private OAuthType oAuthType;
    @JsonProperty(required = true)
    private String oAuthAccessToken;
}
