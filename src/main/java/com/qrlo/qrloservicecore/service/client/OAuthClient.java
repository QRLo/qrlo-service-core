package com.qrlo.qrloservicecore.service.client;

import com.qrlo.qrloservicecore.service.domain.AccessTokenInfoResponse;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-18
 */
public interface OAuthClient {
    Mono<AccessTokenInfoResponse> verifyAccessToken(String accessToken);
}
