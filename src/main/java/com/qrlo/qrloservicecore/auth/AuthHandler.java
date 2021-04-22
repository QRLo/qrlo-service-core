package com.qrlo.qrloservicecore.auth;

import com.qrlo.qrloservicecore.auth.domain.AuthRequest;
import com.qrlo.qrloservicecore.auth.domain.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Slf4j
@Component
public class AuthHandler {
    public Mono<ServerResponse> authenticate(ServerRequest request) {
        return request
                .bodyToMono(AuthRequest.class)
                .flatMap(authRequest -> Mono.just(authRequest.getAccessToken()))
                .flatMap(accessToken -> {
                    AuthResponse authResponse = new AuthResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlJvIExlZSIsImlhdCI6MTUxNjIzOTAyMn0.H1vQtdjaNIXFS57EJy4Js0fslcx7ljHENydJJTkWs8c");
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(authResponse), AuthResponse.class);
                });
    }
}
