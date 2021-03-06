package com.qrlo.qrloservicecore.service.client;

import com.qrlo.qrloservicecore.service.domain.AccessTokenInfoResponse;
import com.qrlo.qrloservicecore.service.exception.OAuthVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Slf4j
@Component
public class KakaoClient implements OAuthClient {
    private static final String BASE_URL = "https://kapi.kakao.com/";
    private static final String ACCESS_TOKEN_INFO_ENDPOINT = "user/access_token_info";
    private final WebClient webClient = WebClient.create(BASE_URL);

    private final String kakaoApiVersion;

    public KakaoClient(@Value("${kakao.oauth.api.version}") String kakaoApiVersion) {
        this.kakaoApiVersion = kakaoApiVersion;
    }

    public Mono<AccessTokenInfoResponse> verifyAccessToken(String accessToken) {
        return webClient
                .get()
                .uri(generateVersionedUri(ACCESS_TOKEN_INFO_ENDPOINT))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=utf-8")
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken))
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.error(new OAuthVerificationException()))
                .bodyToMono(AccessTokenInfoResponse.class);
    }

    private String generateVersionedUri(String endpoint) {
        return String.format("%s/%s", this.kakaoApiVersion, endpoint);
    }
}
