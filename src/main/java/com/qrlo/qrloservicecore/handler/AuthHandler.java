package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.security.JwtTokenProvider;
import com.qrlo.qrloservicecore.service.UserService;
import com.qrlo.qrloservicecore.handler.domain.AuthRequest;
import com.qrlo.qrloservicecore.handler.domain.AuthResponse;
import com.qrlo.qrloservicecore.handler.domain.OAuthIntegrationRequest;
import com.qrlo.qrloservicecore.service.KakaoService;
import com.qrlo.qrloservicecore.service.exception.OAuthVerificationException;
import com.qrlo.qrloservicecore.model.OAuth;
import com.qrlo.qrloservicecore.model.Role;
import com.qrlo.qrloservicecore.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Slf4j
@Component
public class AuthHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;
    private final UserService userService;

    public AuthHandler(JwtTokenProvider jwtTokenProvider, KakaoService kakaoService, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoService = kakaoService;
        this.userService = userService;
    }

    public Mono<ServerResponse> authenticate(ServerRequest request) {
        Mono<AuthRequest> authRequestMono = request
                .bodyToMono(AuthRequest.class)
                .cache()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED))));
        return authRequestMono
                .map(AuthRequest::getOAuthAccessToken)
                .flatMap(kakaoService::verifyAccessToken)
                .zipWith(authRequestMono, (kakaoAccessTokenInfoResponse, authRequest) ->
                        new OAuth(authRequest.getOAuthType(), kakaoAccessTokenInfoResponse.getId().toString()))
                .flatMap(userService::findByOAuth)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))))
                .flatMap(jwtTokenProvider::generateTokenMono)
                .map(AuthResponse::new)
                .flatMap((authResponse) -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(authResponse), AuthResponse.class));
    }

    public Mono<ServerResponse> integrateOAuth(ServerRequest request) {
        Mono<OAuthIntegrationRequest> oAuthIntegrationRequestMono = request
                .bodyToMono(OAuthIntegrationRequest.class)
                .cache()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST))));
        return oAuthIntegrationRequestMono
                .map(OAuthIntegrationRequest::getOAuthAccessToken)
                .flatMap(kakaoService::verifyAccessToken)
                .zipWith(oAuthIntegrationRequestMono, (kakaoAccessTokenInfoResponse, oAuthIntegrationRequest) ->
                        new OAuth(oAuthIntegrationRequest.getOAuthType(), kakaoAccessTokenInfoResponse.getId().toString()))
                .zipWith(oAuthIntegrationRequestMono, (oAuth, oAuthIntegrationRequest) ->
                        User.builder().email(oAuthIntegrationRequest.getEmail()).oAuths(List.of(oAuth)).roles(List.of(Role.ROLE_USER)).build())
                .flatMap(userService::saveUser)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))))
                .flatMap(jwtTokenProvider::generateTokenMono)
                .map(AuthResponse::new)
                .flatMap((authResponse) -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(authResponse), AuthResponse.class))
                .onErrorResume(e -> e instanceof OAuthVerificationException,
                        e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(e.getMessage()));
    }
}