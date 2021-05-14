package com.qrlo.qrloservicecore.repository;

import com.qrlo.qrloservicecore.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-21
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>, CustomUserRepository {
    Mono<User> findByEmail(String email);
}
