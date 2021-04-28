package com.qrlo.qrloservicecore.user.handler;

import com.qrlo.qrloservicecore.common.security.JwtTokenProvider;
import com.qrlo.qrloservicecore.user.client.KakaoOAuthClient;
import com.qrlo.qrloservicecore.user.service.UserService;
import com.qrlo.qrloservicecore.user.handler.domain.AuthRequest;
import com.qrlo.qrloservicecore.user.handler.domain.AuthResponse;
import com.qrlo.qrloservicecore.user.exception.InvalidOAuthException;
import com.qrlo.qrloservicecore.user.model.OAuth;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserService userService;

    public AuthHandler(JwtTokenProvider jwtTokenProvider, KakaoOAuthClient kakaoOAuthClient, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoOAuthClient = kakaoOAuthClient;
        this.userService = userService;
    }

    public Mono<ServerResponse> authenticate(ServerRequest request) {
        Mono<AuthRequest> authRequestMono = request
                .bodyToMono(AuthRequest.class)
                .cache()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new InvalidOAuthException("Empty request body is not valid"))));
        return authRequestMono
                .map(AuthRequest::getOAuthAccessToken)
                .flatMap(kakaoOAuthClient::verifyAccessToken)
                .zipWith(authRequestMono, (kakaoAccessTokenInfoResponse, authRequest) ->
                        new OAuth(authRequest.getOAuthType(), kakaoAccessTokenInfoResponse.getId().toString()))
                .flatMap(userService::findByOAuthOrInsert)
                .flatMap(jwtTokenProvider::generateTokenMono)
                .map(AuthResponse::new)
                .flatMap((authResponse) -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(authResponse), AuthResponse.class))
                .onErrorResume(e -> ServerResponse.status(401).bodyValue(e.getMessage()));
    }

//    public Mono<ServerResponse> refreshToken(ServerRequest request) {
//        HttpCookie oAuthType = request.cookies().getFirst(COOKIE_KEY_QRLO_REFRESH_TOKEN_TYPE);
//        HttpCookie refreshToken = request.cookies().getFirst(COOKIE_KEY_QRLO_REFRESH_TOKEN_VALUE);
//
//        if (oAuthType == null || refreshToken == null || oAuthType.getValue().isBlank()|| refreshToken.getValue().isBlank()) {
//            return ServerResponse.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).build();
//        } else {
//            return Mono.just(refreshToken.getValue())
//                    .flatMap(kakaoOAuthClient::verifyAccessToken)
//                    .map(kakaoAccessTokenInfoResponse -> new OAuth(OAuthType.getByValue(oAuthType.getValue()), kakaoAccessTokenInfoResponse.getId().toString()))
//                    .flatMap(userService::findByOAuthOrInsert)
//                    .flatMap(jwtTokenProvider::generateTokenMono)
//                    .map(AuthResponse::new)
//                    .flatMap(authResponse -> ServerResponse.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .body(Mono.just(authResponse), AuthResponse.class))
//                    .onErrorResume(e -> Mono.just(e.getMessage())
//                            .flatMap(s -> ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(s)));
//        }
//    }
//
//    public Mono<ServerResponse> logOut(ServerRequest request) {
//        return ServerResponse.noContent()
//                .cookie(ResponseCookie.from(COOKIE_KEY_QRLO_REFRESH_TOKEN_TYPE, "").build())
//                .cookie(ResponseCookie.from(COOKIE_KEY_QRLO_REFRESH_TOKEN_VALUE, "").build())
//                .build();
//    }
}