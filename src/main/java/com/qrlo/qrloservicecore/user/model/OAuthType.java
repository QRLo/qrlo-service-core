package com.qrlo.qrloservicecore.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */

@Getter
@AllArgsConstructor
public enum OAuthType {
    KAKAO("KAKAO"),
    NAVER("NAVER"),
    GOOGLE("GOOGLE");
    private final String value;

    private static final Map<String, OAuthType> oAuthTypeMap;

    static {
        oAuthTypeMap = Map.of(
                KAKAO.value, KAKAO,
                NAVER.value, NAVER,
                GOOGLE.value, GOOGLE
        );
    }

    public static OAuthType getByValue(String value) {
        return oAuthTypeMap.get(value);
    }
}
