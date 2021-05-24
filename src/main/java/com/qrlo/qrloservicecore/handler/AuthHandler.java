package com.qrlo.qrloservicecore.handler;

import com.qrlo.qrloservicecore.handler.domain.AuthRequest;
import com.qrlo.qrloservicecore.handler.domain.AuthResponse;
import com.qrlo.qrloservicecore.handler.domain.OAuthIntegrationRequest;
import com.qrlo.qrloservicecore.model.OAuth;
import com.qrlo.qrloservicecore.model.User;
import com.qrlo.qrloservicecore.config.security.JwtTokenProvider;
import com.qrlo.qrloservicecore.service.EmailService;
import com.qrlo.qrloservicecore.service.OAuthService;
import com.qrlo.qrloservicecore.service.UserService;
import com.qrlo.qrloservicecore.service.exception.OAuthIntegrationRequiredException;
import com.qrlo.qrloservicecore.service.exception.OAuthVerificationException;
import com.qrlo.qrloservicecore.service.exception.UnverifiedUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Slf4j
@Component
public class AuthHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final EmailService emailService;
    private final OAuthService oAuthService;

    public AuthHandler(JwtTokenProvider jwtTokenProvider, UserService userService,
                       EmailService emailService, OAuthService oAuthService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.emailService = emailService;
        this.oAuthService = oAuthService;
    }

    public Mono<ServerResponse> authenticate(ServerRequest request) {
        Mono<AuthRequest> authRequestMono = request
                .bodyToMono(AuthRequest.class)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED))))
                .cache();
        return authRequestMono
                .flatMap(authRequest -> oAuthService.verifyAccessToken(authRequest.getOAuthAccessToken()))
                .zipWith(authRequestMono, (accessTokenInfoResponse, authRequest) ->
                        oAuthService.findOAuthByConnectionId(authRequest.getOAuthType(), accessTokenInfoResponse.getId().toString()))
                .flatMap(Function.identity())
                .flatMap(oAuthService::generateAuthToken)
                .map(AuthResponse::new)
                .flatMap((authResponse) -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(authResponse), AuthResponse.class))
                .onErrorResume(UnverifiedUserException.class, e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
                .onErrorResume(OAuthIntegrationRequiredException.class, e -> ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }

    public Mono<ServerResponse> integrateOAuth(ServerRequest request) {
        Mono<OAuthIntegrationRequest> oAuthIntegrationRequestMono = request
                .bodyToMono(OAuthIntegrationRequest.class)
                .cache()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST))));
        Mono<User> savedUser = oAuthIntegrationRequestMono
                .map(OAuthIntegrationRequest::getOAuthAccessToken)
                .flatMap(oAuthService::verifyAccessToken)
                .zipWith(oAuthIntegrationRequestMono, (accessTokenInfoResponse, oAuthIntegrationRequest) ->
                        new OAuth(oAuthIntegrationRequest.getOAuthType().name(), accessTokenInfoResponse.getId().toString()))
                .zipWith(oAuthIntegrationRequestMono, (oAuth, oAuthIntegrationRequest) ->
                        oAuthService.doIntegrate(oAuthIntegrationRequest.getEmail(), oAuth))
                .flatMap(Function.identity())
                .cache();
        return savedUser
                .flatMap(emailService::sendVerificationEmail)
                .subscribeOn(Schedulers.boundedElastic())
                .then(savedUser)
                .flatMap(jwtTokenProvider::generateTokenMono)
                .map(AuthResponse::new)
                .flatMap((authResponse) -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(authResponse), AuthResponse.class))
                .onErrorResume(e -> e instanceof OAuthVerificationException,
                        e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> verifyActivationToken(ServerRequest request) {
        return jwtTokenProvider.getSubjectFromToken(request.pathVariable("token"))
                .flatMap(userService::findById)
                .doOnNext(user -> user.setVerified(true))
                .flatMap(userService::saveUser)
                .then(ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("verification/checked"));
    }
}