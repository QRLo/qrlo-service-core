package com.qrlo.qrloservicecore.handler.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qrlo.qrloservicecore.model.OAuthType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-29
 */
@NoArgsConstructor
@Data
@JsonInclude
public class OAuthIntegrationRequest {
    @JsonProperty(required = true)
    private OAuthType oAuthType;
    @JsonProperty(required = true)
    private String oAuthAccessToken;
    @JsonProperty(required = true)
    private String email;
}
