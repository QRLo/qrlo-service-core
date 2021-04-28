package com.qrlo.qrloservicecore.user.client.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class KakaoAccessTokenInfoResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("app_id")
    private Integer appId;
}
