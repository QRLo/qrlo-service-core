package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.OAuth;
import com.qrlo.qrloservicecore.model.OAuthType;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-18
 */
@Repository
public interface CustomOAuthRepository {
    Mono<OAuth> findByOAuthTypeAndConnectionId(OAuthType oAuthType, String connectionId);
}
