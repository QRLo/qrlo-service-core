package com.qrlo.qrloservicecore.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */

@Setter
@NoArgsConstructor
@JsonInclude
public class KakaoAccessTokenInfoResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("app_id")
    private Integer appId;
}
