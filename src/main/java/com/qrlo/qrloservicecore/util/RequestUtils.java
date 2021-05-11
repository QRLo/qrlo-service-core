package com.qrlo.qrloservicecore.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-07
 */
public final class RequestUtils {
    private RequestUtils() {
        /*
         * Forbidden Instantiation
         */
    }

    public static Mono<String> getUserIdFromRequest(ServerRequest request) {
        return request
                .principal()
                .ofType(UsernamePasswordAuthenticationToken.class)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .ofType(String.class);
    }
}
