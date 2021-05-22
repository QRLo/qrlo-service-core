package com.qrlo.qrloservicecore.service;

import com.qrlo.qrloservicecore.model.OAuth;
import com.qrlo.qrloservicecore.model.OAuthType;
import com.qrlo.qrloservicecore.model.User;
import com.qrlo.qrloservicecore.repository.OAuthRepository;
import com.qrlo.qrloservicecore.repository.UserRepository;
import com.qrlo.qrloservicecore.security.JwtTokenProvider;
import com.qrlo.qrloservicecore.service.client.KakaoClient;
import com.qrlo.qrloservicecore.service.domain.AccessTokenInfoResponse;
import com.qrlo.qrloservicecore.service.exception.OAuthIntegrationRequiredException;
import com.qrlo.qrloservicecore.service.exception.UnverifiedUserException;
import com.qrlo.qrloservicecore.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-17
 */
@Service
public class OAuthService {
    private final KakaoClient kakaoClient;
    private final OAuthRepository oAuthRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuthService(KakaoClient kakaoClient, OAuthRepository oAuthRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.kakaoClient = kakaoClient;
        this.oAuthRepository = oAuthRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Mono<AccessTokenInfoResponse> verifyAccessToken(String accessToken) {
        return kakaoClient.verifyAccessToken(accessToken);
    }

    public Mono<OAuth> findOAuthByConnectionId(OAuthType type, String connectionId) {
        return oAuthRepository.findByOAuthTypeAndConnectionId(type, connectionId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new OAuthIntegrationRequiredException())));
    }

    public Mono<String> generateAuthToken(OAuth oAuth) {
        return Mono.just(oAuth.getUserId())
                .flatMap(userRepository::findById)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserNotFoundException())))
                .doOnNext(user -> {
                    if (!user.isEnabled()) throw new UnverifiedUserException();
                })
                .flatMap(jwtTokenProvider::generateTokenMono);
    }

    @Transactional
    public Mono<User> doIntegrate(String email, OAuth oAuth) {
        Mono<User> userMono = userRepository.findByEmail(email)
                .switchIfEmpty(Mono.defer(() -> userRepository.save(new User(email)))).cache();
        return userMono
                .map(User::getId)
                .doOnNext(oAuth::setUserId)
                .thenReturn(oAuth)
                .flatMap(oAuthRepository::save)
                .then(userMono);
    }
}
