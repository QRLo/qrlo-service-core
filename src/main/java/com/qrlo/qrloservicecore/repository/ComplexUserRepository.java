package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.OAuth;
import com.qrlo.qrloservicecore.model.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-22
 */
@Repository
public interface ComplexUserRepository {
    Mono<User> findByOAuth(OAuth oAuth);
}
