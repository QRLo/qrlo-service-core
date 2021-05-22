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

    public static Mono<Integer> getUserIdFromRequest(ServerRequest request) {
        return request
                .principal()
                .ofType(UsernamePasswordAuthenticationToken.class)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .map(userId -> Integer.parseInt((String) userId));
    }
}
