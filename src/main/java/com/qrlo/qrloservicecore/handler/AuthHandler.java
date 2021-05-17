package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.handler.domain.AuthRequest;
import com.qrlo.qrloservicecore.handler.domain.AuthResponse;
import com.qrlo.qrloservicecore.handler.domain.OAuthIntegrationRequest;
import com.qrlo.qrloservicecore.model.OAuth;
import com.qrlo.qrloservicecore.model.User;
import com.qrlo.qrloservicecore.security.JwtTokenProvider;
import com.qrlo.qrloservicecore.service.EmailService;
import com.qrlo.qrloservicecore.service.KakaoService;
import com.qrlo.qrloservicecore.service.UserService;
import com.qrlo.qrloservicecore.service.exception.OAuthVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
    private final EmailService emailService;

    public AuthHandler(JwtTokenProvider jwtTokenProvider, KakaoService kakaoService, UserService userService, EmailService emailService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoService = kakaoService;
        this.userService = userService;
        this.emailService = emailService;
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
                .doOnNext(user -> {
                    if (!user.isEnabled()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                })
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
        Mono<User> savedUser = oAuthIntegrationRequestMono
                .map(OAuthIntegrationRequest::getOAuthAccessToken)
                .flatMap(kakaoService::verifyAccessToken)
                .zipWith(oAuthIntegrationRequestMono, (kakaoAccessTokenInfoResponse, oAuthIntegrationRequest) ->
                        new OAuth(oAuthIntegrationRequest.getOAuthType(), kakaoAccessTokenInfoResponse.getId().toString()))
                .zipWith(oAuthIntegrationRequestMono, (oAuth, oAuthIntegrationRequest) ->
                        User.builder().email(oAuthIntegrationRequest.getEmail()).oAuths(List.of(oAuth)).build())
                .flatMap(userService::saveUser)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))))
                .cache();
        savedUser
                .flatMap(emailService::sendVerificationEmail)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
        return savedUser
                .flatMap(jwtTokenProvider::generateTokenMono)
                .map(AuthResponse::new)
                .flatMap((authResponse) -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(authResponse), AuthResponse.class))
                .onErrorResume(e -> e instanceof OAuthVerificationException,
                        e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> verifyActivationToken(ServerRequest request) {
        String token = request.pathVariable("token");
        String userId = jwtTokenProvider.getSubjectFromToken(token);
        return userService
                .findById(userId)
                .doOnNext(user -> user.setVerified(true))
                .flatMap(userService::saveUser)
                .then(ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("verification/checked"));
    }
}