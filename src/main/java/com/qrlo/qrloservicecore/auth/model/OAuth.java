package com.qrlo.qrloservicecore.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Data
@AllArgsConstructor
public class OAuth {
    private OAuthType oAuthType;
    private String connectionId;

    @Getter
    @AllArgsConstructor
    public enum OAuthType {
        kakao("KAKAO"),
        naver("NAVER"),
        google("GOOGLE");
        private final String value;
    }
}
