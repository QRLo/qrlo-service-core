package com.qrlo.qrloservicecore.auth;

import com.qrlo.qrloservicecore.auth.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
public interface UserRepository extends ReactiveMongoRepository<User, Long> {
    Mono<User> findByEmail(String email);
}
